package BSEP.KT2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import BSEP.KT2.model.ClientRegistrationRequest;

public interface ClientRegistrationRequestRepository extends JpaRepository<ClientRegistrationRequest, Integer> {
    List<ClientRegistrationRequest> findAllByEmail(String email);
}