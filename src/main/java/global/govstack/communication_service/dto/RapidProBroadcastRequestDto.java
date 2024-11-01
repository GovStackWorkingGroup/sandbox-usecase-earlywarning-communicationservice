package global.govstack.communication_service.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record RapidProBroadcastRequestDto(List<String> urns, List<String> contacts, InternalTextDto message,
                                          List<String> groups, String baseLanguage) {

//    @JsonProperty("urns")
//    private List<String> urns;
//    @JsonProperty("contacts")
//    private List<String> contacts;
//    @JsonProperty("text")
//    private InternalTextDto text;
//    @JsonProperty("groups")
//    private List<String> groups;
//    @JsonProperty("base_language")
//    private String baseLanguage;
}
