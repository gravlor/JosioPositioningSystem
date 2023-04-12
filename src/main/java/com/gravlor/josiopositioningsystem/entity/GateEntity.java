package com.gravlor.josiopositioningsystem.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "gates")
@JsonSerialize(using = GateSerializer.class)
public class GateEntity {

    public GateEntity() {}

    public GateEntity(MapEntity from, MapEntity to, Date until) {
        this.key = new GateKey(from, to);
        this.until = until;
    }

    @EmbeddedId
    private GateKey key;

    private Date until;

    public GateKey getKey() {
        return key;
    }

    public void setKey(GateKey key) {
        this.key = key;
    }

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }
}
