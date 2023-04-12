package com.gravlor.josiopositioningsystem.exception;

public class GateAlreadyExistsException extends Exception {

    public GateAlreadyExistsException(String nameMapFrom, String nameMapTo) {
        super("A gate already exists from '" + nameMapFrom + "' to '" + nameMapTo + "'");
    }
}
