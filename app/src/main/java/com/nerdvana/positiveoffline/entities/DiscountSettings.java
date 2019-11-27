package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DiscountSettings")
public class DiscountSettings {
    @NonNull
    @PrimaryKey
    private int core_id;
    private int discount_id;
    private String product_id;
    private String department_id;
    private String room_type_id;
    private String room_rate_id;
    private double percentage;

    public DiscountSettings(int core_id, int discount_id,
                            String product_id, String department_id,
                            String room_type_id, String room_rate_id,
                            double percentage) {
        this.core_id = core_id;
        this.discount_id = discount_id;
        this.product_id = product_id;
        this.department_id = department_id;
        this.room_type_id = room_type_id;
        this.room_rate_id = room_rate_id;
        this.percentage = percentage;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(String room_type_id) {
        this.room_type_id = room_type_id;
    }

    public String getRoom_rate_id() {
        return room_rate_id;
    }

    public void setRoom_rate_id(String room_rate_id) {
        this.room_rate_id = room_rate_id;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
