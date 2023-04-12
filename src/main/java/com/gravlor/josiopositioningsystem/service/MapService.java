package com.gravlor.josiopositioningsystem.service;


import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.entity.MapType;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapService {

    @Autowired
    private MapRepository mapRepository;

    public static String normalizeMapName(String name) {
        return name.strip().replaceAll(" +", " ");
    }

    public boolean checkMapExists(String name) {
        Optional<MapEntity> optMap = mapRepository.findByName(name);
       return optMap.isPresent();
    }

    public MapEntity createNewMap(String name, MapType type) throws MapAlreadyExistsException {
        String normalizeName = MapService.normalizeMapName(name);
        if (checkMapExists(normalizeName)) {
            throw new MapAlreadyExistsException(normalizeName);
        }

        return mapRepository.save(new MapEntity(normalizeName, type));
    }

    public List<MapEntity> findAllMaps() {
        return mapRepository.findAll();
    }
}
