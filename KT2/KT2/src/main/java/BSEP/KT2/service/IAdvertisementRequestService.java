package BSEP.KT2.service;

import java.util.List;

import BSEP.KT2.model.AdvertisementRequest;

public interface IAdvertisementRequestService {
    public void saveAdvertisementRequest(AdvertisementRequest advertisementRequest);
    public List<AdvertisementRequest> getUnprocessedRequests();
    public void changeRequestStatus(Integer id);
}
