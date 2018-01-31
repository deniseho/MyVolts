package com.example.cassie.myvolts.dto;


public class HistoryData extends DataBase {
    public HistoryData(String name) {
        this.name = name;
    }

    public HistoryData(String name, String isWholeProduct) {
        this.name = name;
        this.isWholeProduct = isWholeProduct;
    }

    public HistoryData(String name, String isWholeProduct, String productId) {
        this.name = name;
        this.isWholeProduct = isWholeProduct;
        this.productId = productId;
    }

    public String name;
    public String isWholeProduct;
    public String productId;

    @Override
    public String getItemName() {
        return name;
    }

    public String isWholeProduct() {
        return isWholeProduct;
    }

    public void setWholeProduct(String wholeProduct) {
        isWholeProduct = wholeProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsWholeProduct() {
        return isWholeProduct;
    }

    public void setIsWholeProduct(String isWholeProduct) {
        this.isWholeProduct = isWholeProduct;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
