package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CreditCards")
public class CreditCards {

    @NonNull
    @PrimaryKey
    private int core_id;

    @NonNull
    private String credit_card;

    public CreditCards(int core_id, @NonNull String credit_card) {
        this.core_id = core_id;
        this.credit_card = credit_card;
    }

    public int getCore_id() {
        return core_id;
    }

    @NonNull
    public String getCredit_card() {
        return credit_card;
    }
}
