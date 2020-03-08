package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "BranchGroup")
public class BranchGroup {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int product_id;
    private int product_group_id;
    private double branch_qty;
    private double price;
    private String product;
    private String product_initial;
    private String img_file;
    private String branch_group_name;
    private String branch_group_id;
    @Ignore
    private int selectedQty;
    public BranchGroup(int product_id, int product_group_id,
                       double branch_qty, double price,
                       String product, String product_initial,
                       String img_file, String branch_group_name,
                       String branch_group_id) {
        this.branch_group_id = branch_group_id;
        this.branch_group_name = branch_group_name;
        this.product_id = product_id;
        this.product_group_id = product_group_id;
        this.branch_qty = branch_qty;
        this.price = price;
        this.product = product;
        this.product_initial = product_initial;
        this.img_file = img_file;
    }

    public int getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(int selectedQty) {
        this.selectedQty = selectedQty;
    }

    public String getBranch_group_id() {
        return branch_group_id;
    }

    public void setBranch_group_id(String branch_group_id) {
        this.branch_group_id = branch_group_id;
    }

    public String getBranch_group_name() {
        return branch_group_name;
    }

    public void setBranch_group_name(String branch_group_name) {
        this.branch_group_name = branch_group_name;
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

    public int getProduct_group_id() {
        return product_group_id;
    }

    public void setProduct_group_id(int product_group_id) {
        this.product_group_id = product_group_id;
    }

    public double getBranch_qty() {
        return branch_qty;
    }

    public void setBranch_qty(double branch_qty) {
        this.branch_qty = branch_qty;
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
