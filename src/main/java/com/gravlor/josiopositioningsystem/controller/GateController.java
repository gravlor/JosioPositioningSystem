package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.controller.model.AddAvalonGateRequest;
import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.exception.GateAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.GateInvalidDurationException;
import com.gravlor.josiopositioningsystem.exception.SameMapForGateException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.service.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;

@RestController
public class GateController {

    @Autowired
    private GateService gateService;

    @PostMapping(Constants.PATH_API_GATE_AVALON)
    public ResponseEntity<GateEntity> addAvalonGate(@Valid @RequestBody AddAvalonGateRequest request)
            throws MapNotFoundException, GateInvalidDurationException, SameMapForGateException, GateAlreadyExistsException {

        if (request.getHoursLeft() <= 0 && request.getMinutesLeft() <= 0) {
            throw new GateInvalidDurationException();
        }

        String nameMapFrom = request.getFrom();
        String nameMapTo = request.getTo();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, request.getHoursLeft());
        calendar.add(Calendar.MINUTE, request.getMinutesLeft());

        GateEntity gateEntity = gateService.createNewGate(nameMapFrom, nameMapTo, calendar.getTime());
        return new ResponseEntity<>(gateEntity, HttpStatus.CREATED);
    }

    @PostMapping(Constants.PATH_API_GATE)
    public ResponseEntity<GateEntity> addGate(@Valid @RequestBody AddGateRequest request)
            throws MapNotFoundException, SameMapForGateException, GateAlreadyExistsException {

        String nameMapFrom = request.getFrom();
        String nameMapTo = request.getTo();

        GateEntity gateEntity = gateService.createNewGate(nameMapFrom, nameMapTo);
        return new ResponseEntity<>(gateEntity, HttpStatus.CREATED);
    }
}
