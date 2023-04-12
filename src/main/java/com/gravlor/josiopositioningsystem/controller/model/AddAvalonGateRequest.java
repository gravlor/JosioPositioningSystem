package com.gravlor.josiopositioningsystem.controller.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

public class AddAvalonGateRequest extends AddGateRequest {

    public AddAvalonGateRequest() {}

    public AddAvalonGateRequest(String from, String to, int hoursLeft, int minutesLeft) {
        super(from, to);
        this.hoursLeft = hoursLeft;
        this.minutesLeft = minutesLeft;
    }

    @Min(value = 0, message = "Value must be positive")
    private int hoursLeft;

    @Range(min = 0, max = 59, message = "Value must be between 0 and 59")
    private int minutesLeft;

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
