package global.govstack.communication_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.govstack.communication_service.api.RapidProAPi;
import global.govstack.communication_service.api.UserServiceApi;
import global.govstack.communication_service.dto.EndUserResponseDto;
import global.govstack.communication_service.dto.IncomingBroadcastMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommunicationService {

    private final RapidProAPi rapidProAPi;
    private final UserServiceApi userServiceApi;
    private final ObjectMapper mapper;

    public CommunicationService(RapidProAPi rapidProAPi, UserServiceApi userServiceApi, ObjectMapper mapper) {
        this.rapidProAPi = rapidProAPi;
        this.userServiceApi = userServiceApi;
        this.mapper = mapper;
    }

    public void handleIncomingBroadcastFromIM(String broadcastMessage) {
        log.info("Handling message from broadcast-topic: " + broadcastMessage);
        final IncomingBroadcastMessageDto broadcast = this.mapIncomingMessage(broadcastMessage);
        final List<EndUserResponseDto> endUsers = this.fetchEndUsersForBroadcast(broadcast.countryId(), broadcast.countyId());
        this.rapidProAPi.sendMessage(broadcast.textPrimaryLang(), broadcast.flowUUID(), endUsers);
    }

    private List<EndUserResponseDto> fetchEndUsersForBroadcast(int countryId, int countyId) {
        return this.userServiceApi.getEndUsers(countryId, countyId);
    }

    private IncomingBroadcastMessageDto mapIncomingMessage(String broadcastMessage) {
        try {
            return this.mapper.readValue(broadcastMessage, IncomingBroadcastMessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping incoming message failed" + e);
        }

    }
}
