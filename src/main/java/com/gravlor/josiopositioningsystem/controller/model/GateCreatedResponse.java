package com.gravlor.josiopositioningsystem.controller.model;

public class GateCreatedResponse {

    public GateCreatedResponse() {}
    private String from;

    private String to;

    private String until;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}
