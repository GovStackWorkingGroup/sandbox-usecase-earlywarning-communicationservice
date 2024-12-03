package global.govstack.communication_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ConfigException extends RuntimeException {

    public ConfigException(String message) {
        super(message);
    }
}
