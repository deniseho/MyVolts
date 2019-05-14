package com.example.cassie.myvolts.dto;

import android.support.annotation.NonNull;

/**
 * Created by cassie on 23/05/2017.
 */

public class DeviceData implements Comparable<DeviceData> {
    private String pi_id;
    private String manufacturer;
    private String type;
    private String name;
    private String asin;
    private String model;
    private TechSpec tech;

    public DeviceData(String manufacturer, String type){
        this.manufacturer =manufacturer;
        this.type = type;
    }

    public DeviceData(String name, String manufacturer, String type){
        this.name = name;
        this.manufacturer =manufacturer;
        this.type = type;
    }

    public DeviceData(String name, String manufacturer, String asin, TechSpec tech) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.asin = asin;
        this.tech = tech;
    }

    public DeviceData(String pi_id, String name, String manufacturer, String asin, String model) {
        this.pi_id = pi_id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.asin = asin;
        this.model = model;
    }

    public String getPi_id() {
        return pi_id;
    }

    public void setPi_id(String pi_id) {
        this.pi_id = pi_id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public TechSpec getTech() {
        return tech;
    }

    public void setTech(TechSpec tech) {
        this.tech = tech;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceData that = (DeviceData) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull DeviceData o) {
        return this.getName().compareTo(o.getName());
    }
}
