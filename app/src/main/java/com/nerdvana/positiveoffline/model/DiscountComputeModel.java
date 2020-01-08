package com.nerdvana.positiveoffline.model;

public class DiscountComputeModel {
    private int postedDiscountId;
    private Double discountAmount;

    public DiscountComputeModel(int postedDiscountId, Double discountAmount) {
        this.postedDiscountId = postedDiscountId;
        this.discountAmount = discountAmount;
    }

    public int getPostedDiscountId() {
        return postedDiscountId;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setPostedDiscountId(int postedDiscountId) {
        this.postedDiscountId = postedDiscountId;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
