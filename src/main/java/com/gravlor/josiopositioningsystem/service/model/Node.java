package com.gravlor.josiopositioningsystem.service.model;

import com.gravlor.josiopositioningsystem.entity.MapEntity;

import java.util.List;

public class Node {

    public Node(MapEntity map, List<MapEntity> links) {
        this.map = map;
        this.links = links;
    }

    private final MapEntity map;

    private final List<MapEntity> links;

    private Integer weight = null;

    public MapEntity getMap() {
        return map;
    }

    public List<MapEntity> getLinks() {
        return links;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return weight;
    }
}
