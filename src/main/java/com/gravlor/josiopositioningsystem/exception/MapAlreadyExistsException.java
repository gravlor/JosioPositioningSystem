package com.gravlor.josiopositioningsystem.exception;

public class MapAlreadyExistsException extends Exception {

    public MapAlreadyExistsException(String name) {
        super("A map with the name '" + name + "' already  exists");
    }
}
