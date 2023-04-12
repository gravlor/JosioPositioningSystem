package com.gravlor.josiopositioningsystem.exception;

public class SameMapForGateException extends Exception {

    public SameMapForGateException() {
        super("Cannot create a gate between same map");
    }
}
