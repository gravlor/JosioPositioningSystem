package com.gravlor.josiopositioningsystem.service;


import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapService {

    @Autowired
    private MapRepository mapRepository;

    public static String cleanMapName(String name) {
        return name.strip().replaceAll(" +", " ");
    }

    public boolean checkMapExists(String name) {
        Optional<MapEntity> optMap = mapRepository.findByName(name);
       return optMap.isPresent();
    }

    public MapEntity createNewMap(String name, MapType type) throws MapAlreadyExistsException {
        String normalizeName = MapService.cleanMapName(name);
        if (checkMapExists(normalizeName)) {
            throw new MapAlreadyExistsException(normalizeName);
        }

        return mapRepository.save(new MapEntity(normalizeName, type));
    }

    public MapEntity findMapByName(String name) throws MapNotFoundException {
        String mapName = cleanMapName(name);
        Optional<MapEntity> optMap = mapRepository.findByName(mapName);
        if (optMap.isEmpty()) {
            throw new MapNotFoundException(name);
        }

        return optMap.get();
    }

    public List<MapEntity> findAllMaps() {
        return mapRepository.findAll();
    }
}
