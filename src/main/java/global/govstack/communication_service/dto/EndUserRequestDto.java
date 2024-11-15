package global.govstack.communication_service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record EndUserRequestDto(int countryId, List<Integer> countyId) {
}
