package com.example.cassie.myvolts.dto;

/**
 * Created by cassie on 08/06/2017.
 */

public class ManufactorData extends DataBase{

    public ManufactorData(String name) {
        this.name = name;
    }

    public String name;

    @Override
    public String getItemName() {
        return name;
    }
}
