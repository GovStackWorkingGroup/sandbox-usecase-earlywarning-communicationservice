package global.govstack.communication_service.service;

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

    public CommunicationService(RapidProAPi rapidProAPi, UserServiceApi userServiceApi) {
        this.rapidProAPi = rapidProAPi;
        this.userServiceApi = userServiceApi;
    }

    public void handleIncomingBroadcastFromIM(String broadcastMessage) {
        final IncomingBroadcastMessageDto broadcast = this.mapIncomingMessage(broadcastMessage);
        final List<EndUserResponseDto> endUsers = this.fetchEndUsersForBroadcast(broadcast.countryId(), broadcast.countyId());
        this.rapidProAPi.sendMessage(broadcastMessage, endUsers);
    }

    private List<EndUserResponseDto> fetchEndUsersForBroadcast(int countryId, int countyId) {
        return this.userServiceApi.getEndUsers(countryId, countyId);
    }

    private IncomingBroadcastMessageDto mapIncomingMessage(String broadcastMessage) {
        return IncomingBroadcastMessageDto.builder().build();
    }
}
