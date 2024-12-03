package global.govstack.communication_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.api.RapidProAPi;
import global.govstack.communication_service.api.UserServiceApi;
import global.govstack.communication_service.dto.EndUserResponseDto;
import global.govstack.communication_service.dto.IncomingBroadcastMessageDto;
import global.govstack.communication_service.dto.LogInfoDto;
import global.govstack.communication_service.pub_sub.IMPublisher;
import global.govstack.communication_service.repository.CommunicationRepository;
import global.govstack.communication_service.repository.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunicationService {

    private final RapidProAPi rapidProAPi;
    private final UserServiceApi userServiceApi;
    private final ObjectMapper mapper;
    private final IMPublisher publisher;
    private final CommunicationRepository repository;

    public void handleIncomingBroadcastFromIM(String broadcastMessage) {
        log.info("Handling message from broadcast-topic: " + broadcastMessage);
        final IncomingBroadcastMessageDto broadcast = this.mapIncomingMessage(broadcastMessage);
        final List<Config> settings = this.repository.findAll();
        boolean checkUser = this.checkUser(broadcast.publisher());
        log.info("user ok " + checkUser);
        log.info("settings ok " + !settings.isEmpty());
        if (checkUser && !settings.isEmpty()) {
            this.rapidProAPi.sendMessage(broadcast.textPrimaryLang(), settings);
        } else {
            this.buildAndSendLogEvents(broadcast.textPrimaryLang(), broadcast.broadcastId());
        }
    }

    private boolean checkUser(String userId) {
        return this.userServiceApi.checkUser(userId);
    }

    private IncomingBroadcastMessageDto mapIncomingMessage(String broadcastMessage) {
        try {
            return this.mapper.readValue(broadcastMessage, IncomingBroadcastMessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping incoming message failed" + e);
        }
    }

    private void buildAndSendLogEvents(String broadcastMessage, String broadcastId) {
        log.info("Sending to log topic");
        try {
            //simulate broadcast from IM
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .from("Information Mediator BB")
                    .to("Messaging BB")
                    .content("Received broadcast from Information Mediator")
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
            //simulate user service call
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .from("Messaging BB")
                    .to("User Service")
                    .content("Fetching broadcast recipients")
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
            //simulate message building
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .from("User Service")
                    .to("Messaging BB")
                    .content("Preparing broadcast delivery based on recipient data")
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
            //simulate initialisation
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .from("Messaging BB")
                    .to("Recipients")
                    .content("Initiating end user delivery")
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
            //simulate delivery
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .content("Broadcast sent")
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
            //send broadcast message content
            this.publisher.publishServiceLogging(this.mapper.writeValueAsString(LogInfoDto.builder()
                    .to("mobile")
                    .content(broadcastMessage)
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping log messages failed" + e);
        }
    }
}
