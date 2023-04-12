package com.gravlor.josiopositioningsystem.service;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.UnknowNodeException;
import com.gravlor.josiopositioningsystem.service.model.Node;
import com.gravlor.josiopositioningsystem.service.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MappingService {

    @Autowired
    private MapService mapService;

    @Autowired
    private GateService gateService;

    public List<GateEntity> findTheWay(MapEntity from, MapEntity to) throws UnknowNodeException {
        List<MapEntity> allMaps = mapService.findAllMaps();
        List<GateEntity> allGates = gateService.findAllGates();

        Tree tree = buildTree(allMaps, allGates);
        exploreTree(tree, from, to);

        return getResult(tree, to);
    }

    private List<GateEntity> getResult(Tree tree, MapEntity to) {
        //tree.getNode()

        return new ArrayList<>();
    }

    private void exploreTree(Tree tree, MapEntity from, MapEntity to) throws UnknowNodeException {
        Node start = tree.getNode(from);
        start.setWeight(0);

        //HashSet<Node> nodes = new HashSet<>(tree.getAllNodes());

        exploreNode(tree, start, 1);
    }

    private void exploreNode(Tree tree, Node exploringNode, int currentDist) throws UnknowNodeException {

        boolean keepExploring = false;
        for (MapEntity map : exploringNode.getLinks()) {
            Node currentNode = tree.getNode(map);
            if (currentNode.getWeight() != null) {
                continue;
            }
            currentNode.setWeight(currentDist);
            keepExploring = true;
        }

        for (MapEntity map : exploringNode.getLinks()) {
            Node currentNode = tree.getNode(map);
            if (keepExploring) {
                exploreNode(tree, currentNode, currentDist + 1);
            }
        }
    }

    private void setNodeWeight(Tree tree, Node node, int dist) throws UnknowNodeException {


    }

    private Tree buildTree(List<MapEntity> allMaps, List<GateEntity> allGates) {
        Tree tree = new Tree();

        allMaps.forEach(map -> tree.addNode(buildNode(map, allGates)));

        return tree;
    }

    private Node buildNode(MapEntity map, List<GateEntity> allGates) {
        return new Node(map, findLink(map, allGates));
    }

    private Set<MapEntity> findLink(MapEntity from, List<GateEntity> allGates) {
        return allGates.stream().filter(gate ->
                        from.getName().equals(gate.getFrom().getName()) || from.getName().equals(gate.getTo().getName()))
                .map(gate -> {
                    if (from.getName().equals(gate.getFrom().getName())) {
                        return gate.getTo();
                    } else {
                        return gate.getFrom();
                    }
                })
                .collect(Collectors.toSet());
    }

}
