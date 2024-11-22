package global.govstack.communication_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.api.RapidProAPi;
import global.govstack.communication_service.api.UserServiceApi;
import global.govstack.communication_service.dto.EndUserResponseDto;
import global.govstack.communication_service.dto.IncomingBroadcastMessageDto;
import global.govstack.communication_service.dto.LogInfoDto;
import global.govstack.communication_service.pub_sub.IMPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@PropertySource("classpath:.env")
public class CommunicationService {

    private final RapidProAPi rapidProAPi;
    private final UserServiceApi userServiceApi;
    private final ObjectMapper mapper;
    private final IMPublisher publisher;
    @Value("${USUUID}")
    private String USUUID;

    public CommunicationService(RapidProAPi rapidProAPi, UserServiceApi userServiceApi, ObjectMapper mapper, IMPublisher publisher) {
        this.rapidProAPi = rapidProAPi;
        this.userServiceApi = userServiceApi;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    public void handleIncomingBroadcastFromIM(String broadcastMessage) {
        log.info("Handling message from broadcast-topic: " + broadcastMessage);
        final IncomingBroadcastMessageDto broadcast = this.mapIncomingMessage(broadcastMessage);
        final List<EndUserResponseDto> endUsers = this.fetchEndUsersForBroadcast(broadcast.countryId(), broadcast.countyId());
        boolean canSend = false;
        if (canSend) {
            this.rapidProAPi.sendMessage(broadcast.textPrimaryLang(), endUsers);
        }
        this.buildAndSendLogEvents(broadcast.textPrimaryLang(), broadcast.broadcastId());
    }

    private List<EndUserResponseDto> fetchEndUsersForBroadcast(int countryId, List<Integer> countyId) {
        return this.userServiceApi.getEndUsers(countryId, countyId);
    }

    private IncomingBroadcastMessageDto mapIncomingMessage(String broadcastMessage) {
        try {
            return this.mapper.readValue(broadcastMessage, IncomingBroadcastMessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping incoming message failed" + e);
        }
    }

    private void buildAndSendLogEvents(String broadcastMessage, String broadcastId) {
        log.info("sending to log topic");
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
                    .content(broadcastMessage)
                    .timeStamp(LocalDateTime.now())
                    .broadcastId(broadcastId)
                    .build()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping log messages failed" + e);
        }
    }
}
