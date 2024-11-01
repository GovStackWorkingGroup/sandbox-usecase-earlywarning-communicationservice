package global.govstack.communication_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record IncomingBroadcastMessageDto(String flowUUID, String broadcastTitle, int broadcastChannel,
                                          LocalDate startDate, LocalDate endDate, String textPrimaryLang,
                                          String textSecondaryLang, int countryId, int countyId) {
}
