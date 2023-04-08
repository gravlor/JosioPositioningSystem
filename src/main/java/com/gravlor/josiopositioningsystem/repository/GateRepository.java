package com.gravlor.josiopositioningsystem.repository;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GateRepository extends JpaRepository<GateEntity, Integer> {
}
