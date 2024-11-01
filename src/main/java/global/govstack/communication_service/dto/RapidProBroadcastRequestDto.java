package global.govstack.communication_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record RapidProBroadcastRequestDto(@JsonProperty("flow") String flowUUID, List<String> urns,
                                          List<String> contacts, InternalTextDto extra,
                                          List<String> groups, String baseLanguage) {
}
