package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.GateKey;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.GateAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.GateInvalidDurationException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.repository.GateRepository;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RestController
public class GateController {
    @Autowired
    private GateRepository gateRepository;

    @Autowired
    private MapRepository mapRepository;

    @PostMapping(Constants.PATH_API_GATE)
    public ResponseEntity<GateEntity> addGate(@Valid @RequestBody AddGateRequest request) throws MapNotFoundException, GateInvalidDurationException, GateAlreadyExistsException {

        String nameMapFrom = request.getFrom();
        Optional<MapEntity> optMapFrom = mapRepository.findByName(nameMapFrom);
        if (optMapFrom.isEmpty()) {
            throw new MapNotFoundException(nameMapFrom);
        }

        String nameMapTo = request.getTo();
        Optional<MapEntity> optMapTo = mapRepository.findByName(nameMapTo);
        if (optMapTo.isEmpty()) {
            throw new MapNotFoundException(nameMapTo);
        }

        if (request.getHoursLeft() <= 0 && request.getMinutesLeft() <= 0) {
            throw new GateInvalidDurationException();
        }

        MapEntity mapFrom = optMapFrom.get();
        MapEntity mapTo = optMapTo.get();

        if (gateRepository.existsById(new GateKey(mapFrom, mapTo)) || gateRepository.existsById(new GateKey(mapTo, mapFrom))) {
            throw new GateAlreadyExistsException(nameMapFrom, nameMapTo);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, request.getHoursLeft());
        calendar.add(Calendar.MINUTE, request.getMinutesLeft());

        GateEntity gateEntity = new GateEntity(mapFrom, mapTo, calendar.getTime());
        gateEntity = gateRepository.save(gateEntity);

        return new ResponseEntity<>(gateEntity, HttpStatus.CREATED);
    }
}
