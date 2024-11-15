package global.govstack.communication_service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.dto.EndUserResponseDto;
import global.govstack.communication_service.dto.InternalTextDto;
import global.govstack.communication_service.dto.RapidProBroadcastRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RapidProAPi {

    private final HttpHeaders httpHeaders;
    private final APIUtil apiUtil;
    private final ObjectMapper mapper;

    @Value("${rapid-pro.flow.url}")
    private String RAPID_PRO_FLOW_URL;

    @Value("${rapid-pro.token}")
    private String AUTH_TOKEN;

    @Value("${rapid.pro.flow.id}")
    private String FLOW_ID;


    public RapidProAPi(APIUtil apiUtil, ObjectMapper mapper) {
        this.apiUtil = apiUtil;
        this.mapper = mapper;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    }

    public void sendMessage(String broadcastMessage, String flowUUID, List<EndUserResponseDto> recipients) {
        final String broadcast = this.buildRapidProMessage(broadcastMessage, flowUUID, recipients);
        try {
            log.info("Sending a message to RapidPro:  {}", broadcast);
            httpHeaders.add("Authorization", String.format("Token %s", AUTH_TOKEN));
            final String response = this.apiUtil.callAPI(RAPID_PRO_FLOW_URL, HttpMethod.POST, httpHeaders, broadcast, String.class).getBody();
            log.info(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    private String buildRapidProMessage(String broadcastMessage, String flowUUID, List<EndUserResponseDto> recipients) {
        log.info("Building RapidPro message");
        final InternalTextDto textDto = InternalTextDto.builder().description(broadcastMessage).build();
        try {
            return this.mapper.writeValueAsString(RapidProBroadcastRequestDto.builder()
                    .flowUUID(FLOW_ID)
                    .extra(textDto)
                    .urns(recipients.stream().map(EndUserResponseDto::phoneNumber).collect(Collectors.toList()))
                    .baseLanguage("eng")
                    .groups(Collections.emptyList())
                    .contacts(Collections.emptyList())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong with building the RP message" + e);
        }
    }
}
