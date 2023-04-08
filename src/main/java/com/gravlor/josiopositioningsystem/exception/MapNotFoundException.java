package com.gravlor.josiopositioningsystem.exception;

public class MapNotFoundException extends Exception  {

    public MapNotFoundException(String name) {
        super("Map '" + name + "' does not exists");
    }
}
