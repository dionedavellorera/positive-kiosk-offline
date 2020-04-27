package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PaymentTypes")
public class PaymentTypes {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int core_id;

    private String image_url;
    private String payment_type;
    private String other_field;

    public PaymentTypes(int core_id,
                        String image_url, String payment_type,
                        String other_field) {
        this.core_id = core_id;
        this.image_url = image_url;
        this.payment_type = payment_type;
        this.other_field = other_field;
    }

    public String getOther_field() {
        return other_field;
    }

    public void setOther_field(String other_field) {
        this.other_field = other_field;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
}
