package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.NotNull;

public class AddGateRequest {

    public AddGateRequest() {}

    public AddGateRequest(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @NotNull(message = "Value must not be null")
    private String from;
    @NotNull(message = "Value must not be null")
    private String to;

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

}
