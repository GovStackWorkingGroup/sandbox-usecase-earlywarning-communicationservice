package global.govstack.communication_service.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastRequestDto {

    @JsonProperty("urns")
    private List<String> urns;
    @JsonProperty("contacts")
    private List<String> contacts;
    @JsonProperty("text")
    private InternalTextDto text;
    @JsonProperty("groups")
    private List<String> groups;
    @JsonProperty("base_language")
    private String baseLanguage;
}
