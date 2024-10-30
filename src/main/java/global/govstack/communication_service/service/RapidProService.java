package global.govstack.communication_service.service;

import global.govstack.communication_service.configuration.RapidProProperties;
import global.govstack.communication_service.service.dto.BroadcastRequestDto;
import global.govstack.communication_service.service.dto.InternalTextDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class RapidProService {

    private RestTemplate restTemplate;
    private final RapidProProperties rapidProProperties;
    private final HttpHeaders httpHeaders;


    public RapidProService(RapidProProperties rapidProProperties) {
        this.rapidProProperties = rapidProProperties;
        this.restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Token " + rapidProProperties.authToken());
    }

    public void handleIncomingThreatFromIM(String broadcastMessage)  {


        System.out.println("Mapped Threat object: " + broadcastMessage);
        System.out.println("Mapped Threat object: " + broadcastMessage);
        sendMessage(broadcastMessage);
    }
    public void sendMessage(String message)  {
        var textDto = new InternalTextDto("Hello from java world", "Hola Java");
        var requestDto = new BroadcastRequestDto();
        requestDto.setText(textDto);
        requestDto.setUrns(List.of("tel:+"));
        requestDto.setBaseLanguage("eng");
        requestDto.setGroups(List.of());
        requestDto.setContacts(List.of());

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Token " + rapidProProperties.authToken());


        HttpEntity<BroadcastRequestDto> request =
                new HttpEntity<>(requestDto, httpHeaders);

        log.info("Send a message to RapidPro:  {}", request);
        try {
            String response = restTemplate.exchange(rapidProProperties.url(), HttpMethod.POST, request, String.class).getBody();
            System.out.println(response);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }
}
