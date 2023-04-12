package com.gravlor.josiopositioningsystem.repository;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GateRepository extends JpaRepository<GateEntity, Integer> {
    Optional<GateEntity> findByFromNameAndToName(String from, String to);
    boolean existsByFromNameAndToName(String from, String to);
}
