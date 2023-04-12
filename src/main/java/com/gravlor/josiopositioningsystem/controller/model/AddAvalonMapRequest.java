package com.gravlor.josiopositioningsystem.controller.model;

import com.gravlor.josiopositioningsystem.entity.MapType;


public class AddAvalonMapRequest extends AddMapRequest {

    public AddAvalonMapRequest() {}

    public AddAvalonMapRequest(String name) {
        super(name, MapType.AVALON.name());
    }

}
