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
        this.from = from;
        this.to = to;
        this.until = until;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private MapEntity from;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private MapEntity to;

    private Date until;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getUntil() {
        return until;
    }

    public void setUntil(Date until) {
        this.until = until;
    }
}
