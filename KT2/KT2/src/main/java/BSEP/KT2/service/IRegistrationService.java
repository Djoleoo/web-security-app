package BSEP.KT2.service;

import java.util.List;

import BSEP.KT2.dto.ClientRegistrationRequestCreationDto;
import BSEP.KT2.dto.ClientRegistrationRequestRejectionDto;
import BSEP.KT2.dto.ClientRegistrationRequestViewDto;
import BSEP.KT2.dto.UserActivationDto;

public interface IRegistrationService {
    public void requestClientRegistration(ClientRegistrationRequestCreationDto requestDto);
    public void acceptClientRegistration(int requestId);
    public void rejectClientRegistration(int requestId, ClientRegistrationRequestRejectionDto rejection);
    public void activateUser(UserActivationDto activation);
    public List<ClientRegistrationRequestViewDto> getAllClientRegistrationRequests();
}
