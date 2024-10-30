package global.govstack.communication_service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rapid-pro")
public record RapidProProperties(
    String url,
    String authToken) {}