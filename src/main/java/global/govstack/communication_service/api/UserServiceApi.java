package global.govstack.communication_service.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
public class UserServiceApi {

    private final APIUtil apiUtil;
    private final HttpHeaders httpHeaders;

    @Value("${user-service.url}")
    private String USER_SERVICE_URL;

    public UserServiceApi(APIUtil apiUtil) {
        this.apiUtil = apiUtil;
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    public boolean checkUser(String userId) {
        try {
            this.restRequest(userId);
            return Boolean.TRUE;
        } catch (ResponseStatusException e) {
            if (e.getStatusCode().equals(HttpStatus.SERVICE_UNAVAILABLE)) {
                return Boolean.FALSE;
            }
            throw new RuntimeException("Communication to user-service failed");
        }
    }

    private void restRequest(String userId) {
        try {
            this.apiUtil.callAPI(USER_SERVICE_URL + "/checkUser?userId=" + UUID.fromString(userId), HttpMethod.GET, httpHeaders, new HttpEntity<>(null), Void.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }
}
