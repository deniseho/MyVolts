package com.example.cassie.myvolts.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class ProductData extends DataBase implements Serializable, Comparable<ProductData>{
    public ProductData(){}

    public ProductData(String name) {
        this.name = name;
    }

    public ProductData(String name, String productId) {
        this.productId = productId;
        this.name = name;
    }

    public ProductData(String productId, String name, List<TechSpec> tech) {
        this.productId = productId;
        this.name = name;
        this.tech = tech;
    }

//    public ProductData(String productId, String name, String tech) {
//        this.productId = productId;
//        this.name = name;
//        this.tech = tech;
//    }

    private String name;
    private String des;
    private String made;
    private String type;
    private String productId;
    private List<TechSpec> tech;
//    private String tech;
    private float similarity;
    private String type_id;
    private String images_id;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TechSpec> getTech() {
        return tech;
    }

    public void setTech(List<TechSpec> tech) {
        this.tech = tech;
    }

//    public String getTech() {
//        return tech;
//    }
//
//    public void setTech(String tech) {
//        this.tech = tech;
//    }


    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this .similarity = similarity;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getImages_id() {
        return images_id;
    }

    public void setImages_id(String images_id) {
        this.images_id = images_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductData that = (ProductData) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull ProductData o) {
        if (this.similarity > o.getSimilarity())
            return -1;
        if (this.similarity < o.getSimilarity())
            return 1;
        return 0;
    }
}
