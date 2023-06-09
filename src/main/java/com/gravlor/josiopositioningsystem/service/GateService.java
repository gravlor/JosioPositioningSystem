package com.gravlor.josiopositioningsystem.service;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.GateAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.SameMapForGateException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GateService {

    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private MapRepository mapRepository;

    public boolean checkGateExists(MapEntity mapFrom, MapEntity mapTo) {
        return gateRepository.existsByFromNameAndToName(mapFrom.getName(), mapTo.getName()) || gateRepository.existsByFromNameAndToName(mapTo.getName(), mapFrom.getName());
    }

    public GateEntity createNewGate(String nameMapFrom, String nameMapTo) throws MapNotFoundException, SameMapForGateException, GateAlreadyExistsException {
        return createNewGate(nameMapFrom, nameMapTo, null);
    }

    public GateEntity createNewGate(String nameMapFrom, String nameMapTo, Date until) throws MapNotFoundException, SameMapForGateException, GateAlreadyExistsException {

        if (nameMapFrom.equals(nameMapTo)) {
            throw new SameMapForGateException();
        }

        Optional<MapEntity> optMapFrom = mapRepository.findByName(nameMapFrom);
        if (optMapFrom.isEmpty()) {
            throw new MapNotFoundException(nameMapFrom);
        }

        Optional<MapEntity> optMapTo = mapRepository.findByName(nameMapTo);
        if (optMapTo.isEmpty()) {
            throw new MapNotFoundException(nameMapTo);
        }

        if (checkGateExists(optMapFrom.get(), optMapTo.get())) {
            throw new GateAlreadyExistsException(nameMapFrom, nameMapTo);
        }

        GateEntity gateEntity = new GateEntity(optMapFrom.get(), optMapTo.get(), until);
        gateEntity = gateRepository.save(gateEntity);
        return gateEntity;
    }

    public List<GateEntity> findAllGates() {
        return gateRepository.findAll();
    }
}
