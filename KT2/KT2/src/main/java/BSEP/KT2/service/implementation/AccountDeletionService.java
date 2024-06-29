package BSEP.KT2.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BSEP.KT2.model.Advertisement;
import BSEP.KT2.model.AdvertisementRequest;
import BSEP.KT2.model.ClientRegistrationRequest;
import BSEP.KT2.model.User;
import BSEP.KT2.model.enums.ClientPackage;
import BSEP.KT2.repository.AdvertisementRepository;
import BSEP.KT2.repository.AdvertisementRequestRepository;
import BSEP.KT2.repository.ClientRegistrationRequestRepository;
import BSEP.KT2.repository.UserRepository;
import BSEP.KT2.service.IAccountDeletionService;

@Service
public class AccountDeletionService implements IAccountDeletionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientRegistrationRequestRepository registrationRequestRepository;

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    AdvertisementRequestRepository advertisementRequestRepository;
    
    @Override
    public void delete(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        if(!user.getClientPackage().equals(ClientPackage.GOLD)) {
            throw new SecurityException("Users with the Base or Standard service packages do not have access to account deletion. Please consider purchasing the Gold service package.");
        }

        deleteAdvertisements(username);
        deleteAdvertisementRequests(username);
        deleteRegistrationRequests(username);

        userRepository.delete(user);
    }

    private void deleteAdvertisements(String username) {
        List<Advertisement> advertisements = advertisementRepository.findAllByUsername(username);

        for (Advertisement advertisement : advertisements) {
            advertisementRepository.delete(advertisement);
        }
    }

    private void deleteAdvertisementRequests(String username) {
        List<AdvertisementRequest> requests = advertisementRequestRepository.findAllByUsername(username);

        for (AdvertisementRequest request : requests) {
            advertisementRequestRepository.delete(request);
        }
    }

    private void deleteRegistrationRequests(String username) {
        List<ClientRegistrationRequest> requests = registrationRequestRepository.findAllByEmail(username);

        for (ClientRegistrationRequest request : requests) {
            registrationRequestRepository.delete(request);
        }
    }
}
