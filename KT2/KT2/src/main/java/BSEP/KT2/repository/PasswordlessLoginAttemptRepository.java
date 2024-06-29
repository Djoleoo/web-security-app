package BSEP.KT2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import BSEP.KT2.model.PasswordlessLoginAttempt;

public interface PasswordlessLoginAttemptRepository extends JpaRepository<PasswordlessLoginAttempt, Integer> {
}