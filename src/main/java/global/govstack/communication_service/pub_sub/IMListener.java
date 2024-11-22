package global.govstack.communication_service.pub_sub;

import global.govstack.communication_service.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IMListener {

    private final String BROADCAST_TOPIC = "broadcast-topic";

    private final CommunicationService communicationService;

    public IMListener(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @KafkaListener(
            groupId = "communication-service-listener",
            topics = BROADCAST_TOPIC,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleIncomingThreatFromIM(GenericMessage<String> broadcast, Acknowledgment acknowledgment) {
        log.info("Incoming message from broadcast-topic");
        this.communicationService.handleIncomingBroadcastFromIM(broadcast.getPayload());
        acknowledgment.acknowledge();
    }
}
