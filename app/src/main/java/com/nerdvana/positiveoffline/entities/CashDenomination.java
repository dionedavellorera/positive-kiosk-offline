package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CashDenomination")
public class CashDenomination {
    @PrimaryKey
    @NonNull
    private int core_id;
    private String denomination;
    private Double amount;

    public CashDenomination(int core_id, String denomination, Double amount) {
        this.core_id = core_id;
        this.denomination = denomination;
        this.amount = amount;
    }


    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
