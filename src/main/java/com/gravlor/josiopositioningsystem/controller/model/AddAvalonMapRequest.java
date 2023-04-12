package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class AddAvalonMapRequest {

    public AddAvalonMapRequest() {}

    public AddAvalonMapRequest(String name) {
       this.name = name;
    }

    @NotBlank(message = "Name must not be empty")
    @Pattern(regexp="^[A-Za-z\\-\\ ]*$", message = "Name contains invalid characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
