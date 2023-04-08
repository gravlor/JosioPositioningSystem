package com.gravlor.josiopositioningsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "maps")
public class MapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private MapType type;

    public MapEntity() {
    }

    public MapEntity(String name, MapType type)  {
        this.name = name;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MapType getType() {
        return type;
    }

    public void setType(MapType type) {
        this.type = type;
    }
}
