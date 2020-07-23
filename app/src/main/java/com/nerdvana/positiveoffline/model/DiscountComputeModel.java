package com.nerdvana.positiveoffline.model;

public class DiscountComputeModel {
    private int postedDiscountId;
    private Double discountAmount;
    private double qty;

    public DiscountComputeModel(int postedDiscountId, Double discountAmount, double qty) {
        this.postedDiscountId = postedDiscountId;
        this.discountAmount = discountAmount;
        this.qty = qty;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
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
