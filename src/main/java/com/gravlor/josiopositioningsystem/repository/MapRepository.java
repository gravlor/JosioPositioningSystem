package com.gravlor.josiopositioningsystem.repository;

import com.gravlor.josiopositioningsystem.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Integer> {
    Optional<MapEntity> findByName(String name);
}
