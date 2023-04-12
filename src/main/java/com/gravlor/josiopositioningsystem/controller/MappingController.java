package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.controller.model.AddGateRequest;
import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.service.MapService;
import com.gravlor.josiopositioningsystem.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MappingController {

    @Autowired
    private MappingService mappingService;

    @Autowired
    private MapService mapService;

    @PostMapping(Constants.PATH_API_MAPPING)
    public ResponseEntity<Object> addAvalonGate(@Valid @RequestBody AddGateRequest request) throws MapNotFoundException {

        MapEntity mapFrom = mapService.findMapByName(request.getFrom());
        MapEntity mapTo = mapService.findMapByName(request.getTo());

        List<GateEntity> theWay = mappingService.findTheWay(mapFrom, mapTo);

        return new ResponseEntity<>(theWay, HttpStatus.OK);
    }
}
