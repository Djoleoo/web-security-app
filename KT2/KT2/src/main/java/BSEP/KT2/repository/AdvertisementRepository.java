package BSEP.KT2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import BSEP.KT2.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer>{
    public List<Advertisement> findAllByUsername(String username);
}
