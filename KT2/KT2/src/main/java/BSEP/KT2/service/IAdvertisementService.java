package BSEP.KT2.service;

import java.util.List;

import BSEP.KT2.model.Advertisement;

public interface IAdvertisementService {
    public Advertisement saveAdvertisementRequest(Advertisement advertisement);
    public List<Advertisement> loadAdvertisements();
    public List<Advertisement> loadClientsAdvertisements(String username);
    public Advertisement getAdvertisementById(Integer id);
}
