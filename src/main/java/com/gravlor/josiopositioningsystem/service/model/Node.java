package com.gravlor.josiopositioningsystem.service.model;

import com.gravlor.josiopositioningsystem.entity.MapEntity;

import javax.validation.constraints.NotNull;
import java.util.Set;


public class Node {

    public Node(@NotNull MapEntity map, Set<MapEntity> links) {
        this.map = map;
        this.links = links;
    }

    private final MapEntity map;

    private final Set<MapEntity> links;

    private Integer distance = null;

    public MapEntity getMap() {
        return map;
    }

    public Set<MapEntity> getLinks() {
        return links;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }
}
