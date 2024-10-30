package global.govstack.communication_service.pub_sub;

import com.fasterxml.jackson.core.JsonProcessingException;
import global.govstack.communication_service.service.RapidProService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IMListener {
  private static final String THREAT_TOPIC = "broadcast-topic";

  private final RapidProService rapidProService;

  public IMListener(RapidProService rapidProService) {
      this.rapidProService = rapidProService;
  }

  @KafkaListener(groupId = "test", topics = THREAT_TOPIC)
  public void handleIncomingThreatFromIM(String threatMessage) throws JsonProcessingException {
    // TODO currently works with localhost:9092 kafka, but not with the cross container
    // communication
   this.rapidProService.handleIncomingThreatFromIM(threatMessage);
//    weatherThreadService.saveWeatherThread(weatherThreatDto);
  }
}
