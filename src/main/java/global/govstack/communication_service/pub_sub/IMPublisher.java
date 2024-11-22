package global.govstack.communication_service.pub_sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IMPublisher {

    private static final String LOGGING_TOPIC = "log-topic";
    private static final Integer PARTITION = 0;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishServiceLogging(String logInfoDto) {
        log.info("Sending service logging to IM");
        try {
            this.kafkaTemplate.send(
                    LOGGING_TOPIC,
                    PARTITION,
                    "some key",
                    logInfoDto);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong with publishing message: " + e);
        }
    }
}
