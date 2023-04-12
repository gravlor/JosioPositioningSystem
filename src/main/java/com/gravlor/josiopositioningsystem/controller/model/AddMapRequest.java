package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AddMapRequest {

    public AddMapRequest() {}

    public AddMapRequest(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp="^[A-Za-z\\-\\ ]*$", message = "Name contains invalid characters")
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
