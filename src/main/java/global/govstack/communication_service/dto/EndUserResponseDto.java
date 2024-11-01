package global.govstack.communication_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.util.UUID;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record EndUserResponseDto(UUID endUserUUID, String fullName, String email, String phoneNumber) {
}
