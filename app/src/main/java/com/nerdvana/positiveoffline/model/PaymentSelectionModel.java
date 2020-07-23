package com.nerdvana.positiveoffline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSelectionModel {
        private int id;
        private int paymentId;
        private String onlinePayment;
        private String imageFile;


    public PaymentSelectionModel(int id, int paymentId, String onlinePayment, String imageFile) {
        this.id = id;
        this.paymentId = paymentId;
        this.onlinePayment = onlinePayment;
        this.imageFile = imageFile;
    }

    public int getId() {
        return id;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public String getOnlinePayment() {
        return onlinePayment;
    }

    public String getImageFile() {
        return imageFile;
    }
}
