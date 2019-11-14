package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Orders")
public class Orders {
    //@ForeignKey(entity = Transactions.class, parentColumns = "id", childColumns = "transaction_id")

    @NonNull
    private int transaction_id;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int core_id;
    @NonNull
    private int qty;
    @NonNull
    private Double amount;
    @NonNull
    private Double original_amount;
    @NonNull
    private String name;

    public Orders(int transaction_id, int core_id,
                  int qty, @NonNull Double amount,
                  @NonNull Double original_amount,
                  @NonNull String name) {
        this.transaction_id = transaction_id;
        this.core_id = core_id;
        this.qty = qty;
        this.amount = amount;
        this.original_amount = original_amount;
        this.name = name;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @NonNull
    public Double getAmount() {
        return amount;
    }

    public void setAmount(@NonNull Double amount) {
        this.amount = amount;
    }

    @NonNull
    public Double getOriginal_amount() {
        return original_amount;
    }

    public void setOriginal_amount(@NonNull Double original_amount) {
        this.original_amount = original_amount;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
