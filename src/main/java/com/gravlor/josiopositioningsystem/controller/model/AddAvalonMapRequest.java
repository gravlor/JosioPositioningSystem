package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AddAvalonMapRequest {

    public AddAvalonMapRequest() {}

    public AddAvalonMapRequest(String name) {
        this.name = name;
    }

    @NotNull
    @Pattern(regexp="^[A-Za-z\\-\\ ]*$", message = "Invalid Input")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
