package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProductAlacart")
public class ProductAlacart {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int product_id;
    private int product_alacart_id;
    private double qty;
    private double price;
    private String product;
    private String product_initial;
    private String img_file;

    public ProductAlacart(int product_id, int product_alacart_id,
                          double qty, double price,
                          String product, String product_initial,
                          String img_file) {
        this.product_id = product_id;
        this.product_alacart_id = product_alacart_id;
        this.qty = qty;
        this.price = price;
        this.product = product;
        this.product_initial = product_initial;
        this.img_file = img_file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_alacart_id() {
        return product_alacart_id;
    }

    public void setProduct_alacart_id(int product_alacart_id) {
        this.product_alacart_id = product_alacart_id;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct_initial() {
        return product_initial;
    }

    public void setProduct_initial(String product_initial) {
        this.product_initial = product_initial;
    }

    public String getImg_file() {
        return img_file;
    }

    public void setImg_file(String img_file) {
        this.img_file = img_file;
    }
}
