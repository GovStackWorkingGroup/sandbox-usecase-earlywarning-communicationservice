package global.govstack.communication_service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.dto.EndUserResponseDto;
import global.govstack.communication_service.dto.InternalTextDto;
import global.govstack.communication_service.dto.RapidProBroadcastRequestDto;
import global.govstack.communication_service.pub_sub.IMPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:.env")
public class RapidProAPi {

    private final HttpHeaders httpHeaders;
    private final APIUtil apiUtil;
    private final ObjectMapper mapper;

    @Value("${RAPID_PRO_FLOW_URL}")
    private String RAPID_PRO_FLOW_URL;

    @Value("${AUTH_TOKEN}")
    private String AUTH_TOKEN;

    @Value("${FLOW_ID}")
    private String FLOW_ID;

    @Value("${PHONE_NUMBER}")
    private String PHONE_NUMBER;


    public RapidProAPi(APIUtil apiUtil, ObjectMapper mapper, IMPublisher publisher) {
        this.apiUtil = apiUtil;
        this.mapper = mapper;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    }

    public void sendMessage(String broadcastMessage, List<EndUserResponseDto> recipients) {
        final String broadcast = this.buildRapidProMessage(broadcastMessage, recipients);
        try {
            log.info("Sending a message to RapidPro: {}", broadcast);
            httpHeaders.add("Authorization", String.format("Token %s", AUTH_TOKEN));
            final String response = this.apiUtil.callAPI(RAPID_PRO_FLOW_URL, HttpMethod.POST, httpHeaders, broadcast, String.class).getBody();
            log.info(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    private String buildRapidProMessage(String broadcastMessage, List<EndUserResponseDto> recipients) {
        log.info("Building RapidPro message");
        final InternalTextDto textDto = InternalTextDto.builder().description(broadcastMessage).build();
        try {
            return this.mapper.writeValueAsString(RapidProBroadcastRequestDto.builder()
                    .flowUUID(FLOW_ID)
                    .extra(textDto)
                    .urns(Collections.singletonList(PHONE_NUMBER))
                    .baseLanguage("eng")
                    .groups(Collections.emptyList())
                    .contacts(Collections.emptyList())
                    .build());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Something went wrong with building the RP message" + e);
        }
    }
}
