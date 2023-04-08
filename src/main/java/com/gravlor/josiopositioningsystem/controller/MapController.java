package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.controller.model.AddStaticMapRequest;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MapController {

    @Autowired
    private MapRepository mapRepository;

    @PostMapping(Constants.PATH_API_MAP)
    public ResponseEntity<MapEntity> addAvalonMap(@RequestBody String name) throws MapAlreadyExistsException {
        Optional<MapEntity> optMap = mapRepository.findByName(name);
        if (optMap.isPresent()) {
            throw new MapAlreadyExistsException(name);
        }
        MapEntity map = mapRepository.save(new MapEntity(name, MapType.AVALON));
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping(Constants.PATH_API_MAP  +  "/static")
    public ResponseEntity<MapEntity> addMap(@RequestBody AddStaticMapRequest request) throws MapAlreadyExistsException {
        String name = request.getName();

        Optional<MapEntity> optMap = mapRepository.findByName(name);
        if (optMap.isPresent()) {
            throw new MapAlreadyExistsException(name);
        }

        MapEntity map = mapRepository.save(new MapEntity(name, MapType.valueOf(request.getType())));
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping(Constants.PATH_API_MAP)
    public List<MapEntity> getAllMaps() {
        return mapRepository.findAll();
    }

}
