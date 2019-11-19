package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class TransactionWithOrders {
    @Embedded
    public Transactions transactions;

    @Relation(parentColumn = "id", entityColumn = "transaction_id", entity = Orders.class)
    public List<Orders> ordersList;

}
