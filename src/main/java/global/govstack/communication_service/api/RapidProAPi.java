package global.govstack.communication_service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.dto.InternalTextDto;
import global.govstack.communication_service.dto.RapidProBroadcastRequestDto;
import global.govstack.communication_service.exception.ConfigException;
import global.govstack.communication_service.repository.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RapidProAPi {

    private final HttpHeaders httpHeaders;
    private final APIUtil apiUtil;
    private final ObjectMapper mapper;

    public RapidProAPi(APIUtil apiUtil, ObjectMapper mapper) {
        this.apiUtil = apiUtil;
        this.mapper = mapper;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    }

    public void sendMessage(String broadcastMessage, List<Config> settings) {
        final Config flowUrl = settings.stream().filter(s -> s.getKey().equalsIgnoreCase("FLOW_URL")).findFirst().orElseThrow(() -> new ConfigException("Flow url not found"));
        final Config flowId = settings.stream().filter(s -> s.getKey().equalsIgnoreCase("FLOW_ID")).findFirst().orElseThrow(() -> new ConfigException("Flow id not found"));
        final Config phone = settings.stream().filter(s -> s.getKey().equalsIgnoreCase("PHONE")).findFirst().orElseThrow(() -> new ConfigException("Phone number not found"));
        final Config token = settings.stream().filter(s -> s.getKey().equalsIgnoreCase("TOKEN")).findFirst().orElseThrow(() -> new ConfigException("Token not found"));
        final String broadcast = this.buildRapidProMessage(broadcastMessage, flowId.getValue(), phone.getValue());
        try {
            log.info("Sending a message to RapidPro: {}", broadcast);
            httpHeaders.add("Authorization", String.format("Token %s", token.getValue()));
            final String response = this.apiUtil.callAPI(flowUrl.getValue(), HttpMethod.POST, httpHeaders, broadcast, String.class).getBody();
            log.info(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    private String buildRapidProMessage(String broadcastMessage, String flowId, String phoneNumber) {
        log.info("Building RapidPro message");
        final InternalTextDto textDto = InternalTextDto.builder().description(broadcastMessage).build();
        try {
            return this.mapper.writeValueAsString(RapidProBroadcastRequestDto.builder()
                    .flowUUID(flowId)
                    .extra(textDto)
                    .urns(Collections.singletonList(phoneNumber))
                    .baseLanguage("eng")
                    .groups(Collections.emptyList())
                    .contacts(Collections.emptyList())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong with building the RP message" + e);
        }
    }
}
