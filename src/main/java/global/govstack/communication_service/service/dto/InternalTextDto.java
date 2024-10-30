package global.govstack.communication_service.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalTextDto {

    @JsonProperty("eng")
    private String eng;
    @JsonProperty("spa")
    private String spa;
}
