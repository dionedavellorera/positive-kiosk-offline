package com.nerdvana.positiveoffline.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class TransactionWithDiscounts {

    @ColumnInfo(name = "id")
    private String transaction_id;
    @ColumnInfo(name = "pd_id")
    private int posted_discount_id;
    @ColumnInfo(name = "discount_name")
    private String posted_discount_name;
    @ColumnInfo(name = "percentage")
    private Double percentage;


    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getPosted_discount_id() {
        return posted_discount_id;
    }

    public void setPosted_discount_id(int posted_discount_id) {
        this.posted_discount_id = posted_discount_id;
    }

    public String getPosted_discount_name() {
        return posted_discount_name;
    }

    public void setPosted_discount_name(String posted_discount_name) {
        this.posted_discount_name = posted_discount_name;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    //    @Embedded
//    public Transactions transactions;
//
//    @Relation(parentColumn = "id", entityColumn = "transaction_id", entity = PostedDiscounts.class)
//    public List<PostedDiscounts> discountList;

}
