package global.govstack.communication_service.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.dto.EndUserRequestDto;
import global.govstack.communication_service.dto.EndUserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class UserServiceApi {

    private final APIUtil apiUtil;
    private  final ObjectMapper mapper;
    private final HttpHeaders httpHeaders;

    @Value("${user-service.url}")
    private String USER_SERVICE_URL;

    public UserServiceApi(APIUtil apiUtil) {
        this.apiUtil = apiUtil;
        mapper = new ObjectMapper();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<EndUserResponseDto> getEndUsers(int countryId, int countyId) {
        log.info("Fetching end users");
        try {
        final String requestBody = mapper.writeValueAsString(EndUserRequestDto.builder().countryId(countryId).countyId(countyId).build());
            final ResponseEntity<String> response = this.apiUtil.callAPI(USER_SERVICE_URL + "/getEndUsersForCounty", HttpMethod.POST, httpHeaders,  requestBody, String.class);
            log.info(response.getStatusCode().toString());
            log.info(response.getBody());
            return mapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (Exception ex) {
            log.error("Something went wrong with user-service cross-connection: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }
    }
}
