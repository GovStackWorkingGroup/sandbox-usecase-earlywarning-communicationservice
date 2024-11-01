package global.govstack.communication_service.api;

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

    @Value("${rapid-pro.url}")
    private String RAPID_PRO_URL;

    @Value("${rapid-pro.token}")
    private String AUTH_TOKEN;


    public RapidProAPi(APIUtil apiUtil) {
        this.apiUtil = apiUtil;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Token " + AUTH_TOKEN);
    }

    public void sendMessage(String broadcastMessage, List<EndUserResponseDto> recipients) {
        final RapidProBroadcastRequestDto broadcast = this.buildRapidProMessage(broadcastMessage, recipients);
        log.info("Sending a message to RapidPro:  {}", broadcast);
        try {
            final String response = this.apiUtil.callAPI(RAPID_PRO_URL, HttpMethod.POST, httpHeaders, broadcast, String.class).getBody();
            log.info(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }

    private RapidProBroadcastRequestDto buildRapidProMessage(String broadcastMessage, List<EndUserResponseDto> recipients) {
        final InternalTextDto textDto = InternalTextDto.builder().text(broadcastMessage).build();
        return RapidProBroadcastRequestDto.builder()
                .message(textDto)
                .urns(List.of("tel:"))
                .baseLanguage("eng")
                .groups(Collections.emptyList())
                .contacts(recipients.stream().map(EndUserResponseDto::phoneNumber).collect(Collectors.toList()))
                .build();
    }
}
