package global.govstack.communication_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record IncomingBroadcastMessageDto(String flowUUID, String broadcastTitle, int broadcastChannel,
                                          LocalDate startDate, LocalDate endDate, String textPrimaryLang,
                                          String textSecondaryLang, int countryId, List<Integer> countyId, String publisher) {
}
