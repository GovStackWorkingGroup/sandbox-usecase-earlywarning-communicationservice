package global.govstack.communication_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LogInfoDto (String from, String to, String content, LocalDateTime timeStamp) {
}
