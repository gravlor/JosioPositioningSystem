package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotNull;

public class AddMapRequest extends AddAvalonMapRequest{

    public AddMapRequest() {
        super();
    }

    public AddMapRequest(String name, String type) {
        super(name);
        this.type = type;
    }

    @NotNull
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
