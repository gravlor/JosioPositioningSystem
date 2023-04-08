package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotNull;

public class AddStaticMapRequest {

    public AddStaticMapRequest() {}

    public AddStaticMapRequest(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @NotNull
    private String name;

    @NotNull
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
