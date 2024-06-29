package BSEP.KT2.repository;

import BSEP.KT2.model.AdvertisementRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRequestRepository extends JpaRepository<AdvertisementRequest, Integer> {
    public List<AdvertisementRequest> findAllByUsername(String username);
}
