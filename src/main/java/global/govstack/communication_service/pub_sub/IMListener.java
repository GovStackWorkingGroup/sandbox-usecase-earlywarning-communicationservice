package global.govstack.communication_service.pub_sub;

import global.govstack.communication_service.service.CommunicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class IMListener {

    private final String BROADCAST_TOPIC = "broadcast-topic";

    private final CommunicationService communicationService;

    public IMListener(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @KafkaListener(groupId = "test", topics = BROADCAST_TOPIC)
    public void handleIncomingThreatFromIM(String broadcast) {
        // TODO currently works with localhost:9092 kafka, but not with the cross container communication
        this.communicationService.handleIncomingBroadcastFromIM(broadcast);
    }
}
