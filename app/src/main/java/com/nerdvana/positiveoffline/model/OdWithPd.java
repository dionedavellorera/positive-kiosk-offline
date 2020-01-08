package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class OdWithPd {
    @Embedded
    public OrderDiscounts orderDiscounts;

    @Relation(parentColumn = "transaction_id", entityColumn = "transaction_id", entity = Orders.class)
    public List<Orders> orders;

    
}
