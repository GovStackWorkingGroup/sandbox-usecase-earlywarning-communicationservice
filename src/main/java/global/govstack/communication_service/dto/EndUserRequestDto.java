package global.govstack.communication_service.dto;

import lombok.Builder;

@Builder
public record EndUserRequestDto(int countryId, int countyId) {
}
