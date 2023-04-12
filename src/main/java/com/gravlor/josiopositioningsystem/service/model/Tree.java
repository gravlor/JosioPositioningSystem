package com.gravlor.josiopositioningsystem.service.model;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    public Tree() {
    }

    private Node startingNode;

    private List<Node> allNodes = new ArrayList<>();

    public Node getStartingNode() {
        return startingNode;
    }

    public void addNode(Node node) {

        if (allNodes.isEmpty()) {
            this.startingNode = node;
        }

        allNodes.add(node);
    }

}
