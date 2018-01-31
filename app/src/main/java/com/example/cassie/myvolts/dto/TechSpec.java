package com.example.cassie.myvolts.dto;

import java.io.Serializable;

/**
 * Created by cassie on 13/06/2017.
 */

public class TechSpec implements Serializable{
    private String vol;
    private String amp;
    private String tip;

    public TechSpec(String vol, String amp, String tip) {
        this.vol = vol;
        this.amp = amp;
        this.tip = tip;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getAmp() {
        return amp;
    }

    public void setAmp(String amp) {
        this.amp = amp;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
