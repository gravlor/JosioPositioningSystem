package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.controller.model.AddAvalonMapRequest;
import com.gravlor.josiopositioningsystem.controller.model.AddMapRequest;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MapController {

    @Autowired
    private MapService mapService;

    @PostMapping(Constants.PATH_API_MAP_AVALON)
    public ResponseEntity<MapEntity> addAvalonMap(@Valid @RequestBody AddAvalonMapRequest request) throws MapAlreadyExistsException {
        MapEntity map = mapService.createNewMap(request.getName(), MapType.AVALON);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping(Constants.PATH_API_MAP)
    public ResponseEntity<MapEntity> addMap(@Valid @RequestBody AddMapRequest request) throws MapAlreadyExistsException {
        MapEntity map = mapService.createNewMap(request.getName(), MapType.valueOf(request.getType()));
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping(Constants.PATH_API_MAP)
    public List<MapEntity> getAllMaps() {
        return mapService.findAllMaps();
    }

}
