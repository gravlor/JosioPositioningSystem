package com.gravlor.josiopositioningsystem.service;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.service.model.Node;
import com.gravlor.josiopositioningsystem.service.model.Tree;
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

        Tree tree = buildTree(from, allMaps, allGates);
        exploreTree(tree);

        return getResult(tree);
    }

    private List<GateEntity> getResult(Tree tree) {

    }

    private void exploreTree(Tree tree) {
        tree.getStartingNode();
    }

    private Tree buildTree(MapEntity from, List<MapEntity> allMaps, List<GateEntity> allGates) {
        Tree tree = new Tree();

        Node startingNode = buildNode(from, allGates);
        tree.addNode(startingNode);
        allMaps.remove(from);

        allMaps.forEach(map -> tree.addNode(buildNode(map, allGates)));

        return tree;
    }

    private Node buildNode(MapEntity map, List<GateEntity> allGates) {
        return new Node(map, findLink(map, allGates));
    }

    private List<MapEntity> findLink(MapEntity from, List<GateEntity> allGates) {
        return allGates.stream().filter(gate ->
                        from.getName().equals(gate.getFrom().getName()) || from.getName().equals(gate.getTo().getName()))
                .map(gate -> {
                    if (from.getName().equals(gate.getFrom().getName())) {
                        return gate.getTo();
                    } else {
                        return gate.getFrom();
                    }
                })
                .collect(Collectors.toList());
    }

}
