package com.example.newnest;

public class EstateOptions {
    private Boolean airConditioner;
    private Boolean boiler;
    private Boolean accessibility;
    private int balconies;
    private Boolean elevator;
    private Boolean furniture;
    private Boolean renovated;
    private Boolean painted;
    private Boolean renovationPerms;


    public EstateOptions(){
        this.airConditioner = false;
        this.boiler = false;
        this.accessibility = false;
        this.balconies = 0;
        this.elevator = false;
        this.furniture = false;
        this.renovated = false;
        this.painted = false;
        this.renovationPerms = false;
    }

    public EstateOptions(Boolean airConditioner, Boolean boiler, Boolean accessibility, int balconies, Boolean elevator, Boolean furniture, Boolean renovated, Boolean painted, Boolean renovationPerms) {

        this.airConditioner = airConditioner;
        this.boiler = boiler;
        this.accessibility = accessibility;
        this.balconies = balconies;
        this.elevator = elevator;
        this.furniture = furniture;
        this.renovated = renovated;
        this.painted = painted;
        this.renovationPerms = renovationPerms;
    }

    public Boolean getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(Boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public Boolean getBoiler() {
        return boiler;
    }

    public void setBoiler(Boolean boiler) {
        this.boiler = boiler;
    }

    public Boolean getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(Boolean accessibility) {
        this.accessibility = accessibility;
    }

    public int getBalconies() {
        return balconies;
    }

    public void setBalconies(int balconies) {
        this.balconies = balconies;
    }

    public Boolean getElevator() {
        return elevator;
    }

    public void setElevator(Boolean elevator) {
        this.elevator = elevator;
    }

    public Boolean getFurniture() {
        return furniture;
    }

    public void setFurniture(Boolean furniture) {
        this.furniture = furniture;
    }

    public Boolean getRenovated() {
        return renovated;
    }

    public void setRenovated(Boolean renovated) {
        this.renovated = renovated;
    }

    public Boolean getPainted() {
        return painted;
    }

    public void setPainted(Boolean painted) {
        this.painted = painted;
    }

    public Boolean getRenovationPerms() {
        return renovationPerms;
    }

    public void setRenovationPerms(Boolean renovationPerms) {
        this.renovationPerms = renovationPerms;
    }
}
