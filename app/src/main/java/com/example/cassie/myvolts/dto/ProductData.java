package com.example.cassie.myvolts.dto;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ProductData extends DataBase implements Serializable, Comparable<ProductData>{
    public ProductData(){}

    public ProductData(String name) {
        this.name = name;
    }

    public ProductData(String name, String productId) {
        this.productId = productId;
        this.name = name;
    }
//
//    public ProductData(String productId, String name, List<TechSpec> tech) {
//        this.productId = productId;
//        this.name = name;
//        this.tech = tech;
//    }

    public ProductData(String name, String desc, String file, String productId) {
        this.name = name;
        this.desc = desc;
        this.file = file;
        this.productId = productId;
    }

    private String name;
    private String desc;
    private String file;
    private String productId;


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

    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }


    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

//    public String getMade() {
//        return made;
//    }
//
//    public void setMade(String made) {
//        this.made = made;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public List<TechSpec> getTech() {
//        return tech;
//    }

//    public void setTech(List<TechSpec> tech) {
//        this.tech = tech;
//    }

//    public String getTech() {
//        return tech;
//    }
//
//    public void setTech(String tech) {
//        this.tech = tech;
//    }

//
//    public float getSimilarity() {
//        return similarity;
//    }
//
//    public void setSimilarity(float similarity) {
//        this .similarity = similarity;
//    }
//
//    public String getType_id() {
//        return type_id;
//    }
//
//    public void setType_id(String type_id) {
//        this.type_id = type_id;
//    }
//
//    public String getImages_id() {
//        return images_id;
//    }
//
//    public void setImages_id(String images_id) {
//        this.images_id = images_id;
//    }

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
//        if (this.similarity > o.getSimilarity())
//            return -1;
//        if (this.similarity < o.getSimilarity())
//            return 1;
        return 0;
    }
}
