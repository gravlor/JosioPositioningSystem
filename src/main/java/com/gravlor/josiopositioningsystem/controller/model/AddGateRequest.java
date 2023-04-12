package com.gravlor.josiopositioningsystem.controller.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

public class AddGateRequest {

    public AddGateRequest() {}

    public AddGateRequest(String from, String to, int hoursLeft, int minutesLeft) {
        this.from = from;
        this.to = to;
        this.hoursLeft = hoursLeft;
        this.minutesLeft = minutesLeft;
    }

    @NotNull(message = "Value must not be null")
    private String from;
    @NotNull(message = "Value must not be null")
    private String to;

    @Min(value = 0, message = "Value must be positive")
    private int hoursLeft;

    @Range(min = 0, max = 59, message = "Value must be between 0 and 59")
    private int minutesLeft;

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

    public int getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(int hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
}
