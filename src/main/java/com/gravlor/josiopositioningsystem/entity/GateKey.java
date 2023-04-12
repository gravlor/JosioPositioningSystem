package com.gravlor.josiopositioningsystem.entity;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class GateKey implements Serializable {

    public GateKey() {}

    public GateKey(MapEntity from, MapEntity to) {
        this.from = from;
        this.to = to;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MapEntity from;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private MapEntity to;

    public MapEntity getFrom() {
        return from;
    }

    public void setFrom(MapEntity from) {
        this.from = from;
    }

    public MapEntity getTo() {
        return to;
    }

    public void setTo(MapEntity to) {
        this.to = to;
    }
}
