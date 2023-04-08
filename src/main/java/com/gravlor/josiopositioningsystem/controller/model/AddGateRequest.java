package com.gravlor.josiopositioningsystem.controller.model;

import javax.validation.constraints.*;

public class AddGateRequest {

    public AddGateRequest() {}

    public AddGateRequest(String map1, String map2, int hoursLeft, int minutesLeft) {
        this.map1 = map1;
        this.map2 = map2;
        this.hoursLeft = hoursLeft;
        this.minutesLeft = minutesLeft;
    }

    @NotNull
    private String map1;
    @NotNull
    private String map2;

    @Min(value = 0, message = "The value must be positive")
    private int hoursLeft;

    @Min(value = 0, message = "The value must be positive")
    private int minutesLeft;

    public String getMap1() {
        return map1;
    }

    public void setMap1(String map1) {
        this.map1 = map1;
    }

    public String getMap2() {
        return map2;
    }

    public void setMap2(String map2) {
        this.map2 = map2;
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
