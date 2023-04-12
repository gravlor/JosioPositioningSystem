package com.gravlor.josiopositioningsystem.service;

import com.gravlor.josiopositioningsystem.entity.GateEntity;
import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.UnknowNodeException;
import com.gravlor.josiopositioningsystem.service.model.Node;
import com.gravlor.josiopositioningsystem.service.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MappingService {

    @Autowired
    private MapService mapService;

    @Autowired
    private GateService gateService;

    public List<Node> findTheWay(MapEntity start, MapEntity end) throws UnknowNodeException {
        List<MapEntity> allMaps = mapService.findAllMaps();
        List<GateEntity> allGates = gateService.findAllGates();

        Tree tree = buildTree(allMaps, allGates);
        exploreTree(tree, start, end);

        return getResult(tree, start, end);
    }

    public @Nullable List<Node> getResult(Tree tree, MapEntity start, MapEntity end) throws UnknowNodeException {
        Node endNode = tree.getNode(end);
        if (endNode.getDistance() == null) {
            return null;
        }

        List<Node> path = new ArrayList<>();

        rewindPath(tree, start, endNode, path);

        //Collections.reverse(path);
        return path;
    }

    private Node rewindPath(Tree tree, MapEntity start, Node end, List<Node> path) throws UnknowNodeException {

        if (end.getMap().getId() == start.getId()) {
            path.add(end);
            return end;
        }

        for (MapEntity link : end.getLinks()) {
            Node node = tree.getNode(link);
            if (node.getDistance() != null && (end.getDistance() - 1) == node.getDistance()) {
                Node nodeFound = rewindPath(tree, start, node, path);
                if (nodeFound != null) {
                    path.add(node);
                    return node;
                }
            }
        }
        return null;
    }

    public void exploreTree(Tree tree, MapEntity from, MapEntity to) throws UnknowNodeException {
        Node start = tree.getNode(from);
        start.setDistance(0);
        exploreNode(tree, start, 1);
    }

    private void exploreNode(Tree tree, Node exploringNode, int currentDist) throws UnknowNodeException {

        for (MapEntity map : exploringNode.getLinks()) {
            Node currentNode = tree.getNode(map);
            if (currentNode.getDistance() == null || currentNode.getDistance() > currentDist) {
                currentNode.setDistance(currentDist);
                exploreNode(tree, currentNode, currentDist + 1);
            }
        }
    }

    public Tree buildTree(List<MapEntity> allMaps, List<GateEntity> allGates) {
        Tree tree = new Tree();

        //build the tree better starting with starting node and following links ... ? not sure
        allMaps.forEach(map -> tree.addNode(buildNode(map, allGates)));

        return tree;
    }

    private Node buildNode(MapEntity map, List<GateEntity> allGates) {
        return new Node(map, findLink(map, allGates));
    }

    private Set<MapEntity> findLink(MapEntity from, List<GateEntity> allGates) {
        return allGates.stream()
                .filter(gate -> from.getId() == gate.getFrom().getId() || from.getId() == gate.getTo().getId())
                .map(gate -> from.getId() == gate.getFrom().getId() ? gate.getTo() : gate.getFrom())
                .collect(Collectors.toSet());
    }

}
