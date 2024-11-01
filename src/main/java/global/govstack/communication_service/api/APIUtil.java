package global.govstack.communication_service.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class APIUtil { //NOTE: for the POC it's ok to go with sync call, but for production version async approach should be used

    private final RestTemplate restTemplate;

    public APIUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public <T, R> ResponseEntity<R> callAPI(
            String apiEndpoint,
            HttpMethod httpMethod,
            HttpHeaders httpHeaders,
            T requestBody,
            Class<R> classToConvertBodyTo) {

        final HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            return restTemplate.exchange(
                    apiEndpoint,
                    httpMethod,
                    requestEntity,
                    classToConvertBodyTo);
        } catch (RestClientException e) {
            log.error(e.getMessage());
            throw new RestClientException(e.getMessage());
        }
    }
}
