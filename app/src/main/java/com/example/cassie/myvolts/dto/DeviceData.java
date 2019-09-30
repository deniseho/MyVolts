package com.example.cassie.myvolts.dto;

import android.support.annotation.NonNull;

/**
 * Created by cassie on 23/05/2017.
 */

public class DeviceData implements Comparable<DeviceData> {
    private String p_id;
    private String manufacturer;
    private String name;
    private String model;
    private String mv_uk;
    private String mv_de;
    private String mv_us;

    public DeviceData(String p_id, String manufacturer, String name, String model,
                      String mv_uk, String mv_de, String mv_us) {
        this.p_id = p_id;
        this.manufacturer = manufacturer;
        this.name = name;
        this.model = model;
        this.mv_uk = mv_uk;
        this.mv_de = mv_de;
        this.mv_us = mv_us;
    }

    public String getP_id() {
        return p_id;
    }
    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getMv_uk() {
        return mv_uk;
    }
    public void setMv_uk(String mv_uk) {
        this.mv_uk = mv_uk;
    }

    public String getMv_de() {
        return mv_de;
    }
    public void setMv_de(String mv_de) {
        this.mv_de = mv_de;
    }

    public String getMv_us() {
        return mv_us;
    }
    public void setMv_us(String mv_us) {
        this.mv_de = mv_us;
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
