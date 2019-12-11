package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "OrderDiscounts")
public class OrderDiscounts {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private int transaction_id;
    @NonNull
    private int product_id;
    @NonNull
    private Boolean is_percentage;
    @NonNull
    private Double value;
    @NonNull
    private int order_id;
    @NonNull
    private String discount_name;
    @NonNull
    private long posted_discount_id;
    private Boolean is_void;
    public OrderDiscounts(int product_id, @NonNull Boolean is_percentage,
                          @NonNull Double value, @NonNull int transaction_id,
                          int order_id, String discount_name,
                          long posted_discount_id, Boolean is_void) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.is_percentage = is_percentage;
        this.value = value;
        this.transaction_id = transaction_id;
        this.discount_name = discount_name;
        this.posted_discount_id = posted_discount_id;
        this.is_void = is_void;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public long getPosted_discount_id() {
        return posted_discount_id;
    }

    public void setPosted_discount_id(long posted_discount_id) {
        this.posted_discount_id = posted_discount_id;
    }

    @NonNull
    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(@NonNull String discount_name) {
        this.discount_name = discount_name;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    @NonNull
    public Boolean getIs_percentage() {
        return is_percentage;
    }

    public void setIs_percentage(@NonNull Boolean is_percentage) {
        this.is_percentage = is_percentage;
    }

    @NonNull
    public Double getValue() {
        return value;
    }

    public void setValue(@NonNull Double value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}