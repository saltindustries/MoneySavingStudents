package com.example.moneysavingstudents;

public class AddItemsHelperClass {

    String productName, productPrice, productLink, productCategory;
    Boolean productPurchased;
    public AddItemsHelperClass() {

    }

    public AddItemsHelperClass(String productName, String productPrice, String productLink, String productCategory, Boolean productPurchased) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productLink = productLink;
        this.productCategory = productCategory;
        this.productPurchased = productPurchased;
    }


    public Boolean getProductPurchased() {
        return productPurchased;
    }

    public void setProductPurchased(Boolean productPurchased) {
        this.productPurchased = productPurchased;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
