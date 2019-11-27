package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public OrderDiscounts(int product_id, @NonNull Boolean is_percentage,
                          @NonNull Double value, @NonNull int transaction_id) {
        this.product_id = product_id;
        this.is_percentage = is_percentage;
        this.value = value;
        this.transaction_id = transaction_id;
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
