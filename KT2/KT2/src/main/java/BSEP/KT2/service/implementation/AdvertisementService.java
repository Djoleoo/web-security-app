package BSEP.KT2.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import BSEP.KT2.model.Advertisement;
//import BSEP.KT2.model.AdvertisementRequest;
import BSEP.KT2.repository.AdvertisementRepository;
import BSEP.KT2.service.IAdvertisementService;

@Service
public class AdvertisementService implements IAdvertisementService{
    @Autowired
    AdvertisementRepository advertisementRepository;

    @Override
    public Advertisement saveAdvertisementRequest(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }
    @Override
    public List<Advertisement> loadAdvertisements(){
        return advertisementRepository.findAll();
    }

    @Override
    public List<Advertisement> loadClientsAdvertisements(String username){
        List<Advertisement> allAdvertisements = loadAdvertisements();
        List<Advertisement> clientsAdvertisements = new ArrayList<>();

        for (Advertisement advertisement : allAdvertisements) {
            if(advertisement.getUsername().equals(username)){
                clientsAdvertisements.add(advertisement);
            }
        }

        return clientsAdvertisements;
    }

    @Override
    public Advertisement getAdvertisementById(Integer id){
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow();

        return advertisement;
    }
}
