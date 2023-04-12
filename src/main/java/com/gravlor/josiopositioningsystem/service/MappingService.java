package com.gravlor.josiopositioningsystem.service;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingService {

    @Autowired
    private MapService mapService;

    @Autowired
    private GateService gateService;

    public List<GateEntity> findTheWay(MapEntity from, MapEntity to) {
        List<MapEntity> allMaps = mapService.findAllMaps();
        List<GateEntity> allGates = gateService.findAllGates();



        return new ArrayList<>();
    }

    private void buildGlobalMap(List<MapEntity> allMaps, List<GateEntity> allGates) {

    }

    private List<GateEntity> findLink(MapEntity from, List<GateEntity> allGates) {
        return allGates.stream().filter(gate ->
                from.getName().equals(gate.getFrom().getName()) || from.getName().equals(gate.getTo().getName()))
                .collect(Collectors.toList());
    }

}
