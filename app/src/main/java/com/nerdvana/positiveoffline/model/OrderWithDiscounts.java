package com.nerdvana.positiveoffline.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;

import java.util.List;

import javax.annotation.Nullable;

public class OrderWithDiscounts {


//    o.transaction_id,o.qty,o.amount,o.original_amount,o.name,o.vatAmount,o.vatable,o.vatExempt,o.discountAmount,od.is_percentage,od.value,od.discount_name
//    @ColumnInfo(defaultValue = "transaction_id")
//    private String transactionId;
//    @ColumnInfo(defaultValue = "qty")
//    private int qty;
//    @ColumnInfo(defaultValue = "amount")
//    private Double amount;
//    @ColumnInfo(defaultValue = "original_amount")
//    private Double originalAmount;
//    @ColumnInfo(defaultValue = "name")
//    private String name;
//    @ColumnInfo(defaultValue = "vatAmount")
//    private Double vatAmount;
//    @ColumnInfo(defaultValue = "vatable")
//    private Double vatable;
//    @ColumnInfo(defaultValue = "vatExempt")
//    private Double vatExempt;
//    @ColumnInfo(defaultValue = "discountAmount")
//    private Double discountAmount;
//    @ColumnInfo(defaultValue = "is_percentage")
//    private Boolean isPercentage;
//    @ColumnInfo(defaultValue = "value")
//    private Double value;
//    @ColumnInfo(defaultValue = "discount_name")
//    private String discountName;
//
//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public int getQty() {
//        return qty;
//    }
//
//    public void setQty(int qty) {
//        this.qty = qty;
//    }
//
//    public Double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Double amount) {
//        this.amount = amount;
//    }
//
//    public Double getOriginalAmount() {
//        return originalAmount;
//    }
//
//    public void setOriginalAmount(Double originalAmount) {
//        this.originalAmount = originalAmount;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Double getVatAmount() {
//        return vatAmount;
//    }
//
//    public void setVatAmount(Double vatAmount) {
//        this.vatAmount = vatAmount;
//    }
//
//    public Double getVatable() {
//        return vatable;
//    }
//
//    public void setVatable(Double vatable) {
//        this.vatable = vatable;
//    }
//
//    public Double getVatExempt() {
//        return vatExempt;
//    }
//
//    public void setVatExempt(Double vatExempt) {
//        this.vatExempt = vatExempt;
//    }
//
//    public Double getDiscountAmount() {
//        return discountAmount;
//    }
//
//    public void setDiscountAmount(Double discountAmount) {
//        this.discountAmount = discountAmount;
//    }
//
//    public Boolean getPercentage() {
//        return isPercentage;
//    }
//
//    public void setPercentage(Boolean percentage) {
//        isPercentage = percentage;
//    }
//
//    public Double getValue() {
//        return value;
//    }
//
//    public void setValue(Double value) {
//        this.value = value;
//    }
//
//    public String getDiscountName() {
//        return discountName;
//    }
//
//    public void setDiscountName(String discountName) {
//        this.discountName = discountName;
//    }

        @Embedded
    public Orders orders;

    @Relation(parentColumn = "id", entityColumn = "order_id", entity = OrderDiscounts.class)
    public List<OrderDiscounts> orderWithDiscountList;

    @Relation(parentColumn = "transaction_id", entityColumn = "transaction_id", entity = PostedDiscounts.class)
    public List<PostedDiscounts> postedDiscounts;

}
