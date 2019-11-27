package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Discounts")
public class Discounts {
    @NonNull
    @PrimaryKey
    private int core_id;
    @NonNull
    private String discount_card;
    private int is_custom_discount;
    private int is_card;
    private int is_employee;
    private int is_special;

    public Discounts(int core_id, @NonNull String discount_card,
                     int is_custom_discount, int is_card,
                     int is_employee, int is_special) {
        this.core_id = core_id;
        this.discount_card = discount_card;
        this.is_custom_discount = is_custom_discount;
        this.is_card = is_card;
        this.is_employee = is_employee;
        this.is_special = is_special;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    @NonNull
    public String getDiscount_card() {
        return discount_card;
    }

    public void setDiscount_card(@NonNull String discount_card) {
        this.discount_card = discount_card;
    }

    public int getIs_custom_discount() {
        return is_custom_discount;
    }

    public void setIs_custom_discount(int is_custom_discount) {
        this.is_custom_discount = is_custom_discount;
    }

    public int getIs_card() {
        return is_card;
    }

    public void setIs_card(int is_card) {
        this.is_card = is_card;
    }

    public int getIs_employee() {
        return is_employee;
    }

    public void setIs_employee(int is_employee) {
        this.is_employee = is_employee;
    }

    public int getIs_special() {
        return is_special;
    }

    public void setIs_special(int is_special) {
        this.is_special = is_special;
    }
}
