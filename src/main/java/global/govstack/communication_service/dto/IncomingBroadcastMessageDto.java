package global.govstack.communication_service.dto;

import lombok.Builder;

import java.time.LocalDate;
@Builder
public record IncomingBroadcastMessageDto(String broadcastTitle, int broadcastChannel, LocalDate startDate, LocalDate endDate, String textPrimaryLang, String textSecondaryLang, int countryId, int countyId) {
}
