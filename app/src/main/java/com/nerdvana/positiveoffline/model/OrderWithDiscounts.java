package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;

import java.util.List;

import javax.annotation.Nullable;

public class OrderWithDiscounts {

    @Embedded
    public Orders orders;

    @Relation(parentColumn = "transaction_id", entityColumn = "transaction_id", entity = OrderDiscounts.class)
    public List<OrderDiscounts> orderWithDiscountList;

}
