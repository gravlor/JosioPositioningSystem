package com.gravlor.josiopositioningsystem.service.model;

import com.gravlor.josiopositioningsystem.entity.MapEntity;
import com.gravlor.josiopositioningsystem.exception.UnknowNodeException;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Tree {

    private final Set<Node> allNodes = new HashSet<>();

    public Set<Node> getAllNodes() {
        return allNodes;
    }

    public void addNode(@NotNull Node node) {
        allNodes.add(node);
    }

    public Node getNode(@NotNull MapEntity map) throws UnknowNodeException {
        Optional<Node> optMapNode = allNodes.stream().filter(node -> map.getId() == node.getMap().getId()).findFirst();
        if (optMapNode.isEmpty()) {
            throw new UnknowNodeException();
        }
        return optMapNode.get();
    }

}
