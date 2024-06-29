package BSEP.KT2.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import BSEP.KT2.model.AdvertisementRequest;
import BSEP.KT2.repository.AdvertisementRequestRepository;
import BSEP.KT2.service.IAdvertisementRequestService;

@Service
public class AdvertisementRequestService implements IAdvertisementRequestService {

    @Autowired
    AdvertisementRequestRepository advertisementRequestRepository;

    @Override
    public void saveAdvertisementRequest(AdvertisementRequest advertisementRequest) {
        advertisementRequest.validateData();
        advertisementRequestRepository.save(advertisementRequest);
    }

    @Override 
    public List<AdvertisementRequest> getUnprocessedRequests(){
        List<AdvertisementRequest> allRequests = advertisementRequestRepository.findAll();
        List<AdvertisementRequest> unprocessedRequests = new ArrayList<>();

        for (AdvertisementRequest advertisementRequest : allRequests) {
            if(!advertisementRequest.isCreated()){
                unprocessedRequests.add(advertisementRequest);
            }
        }

        return unprocessedRequests;
    }

    @Override
    public void changeRequestStatus(Integer id){
        Optional<AdvertisementRequest> optionalRequest = advertisementRequestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            AdvertisementRequest request = optionalRequest.get();
            request.setCreated(true);
            advertisementRequestRepository.save(request);
        } 


       
    }
}
