package com.nerdvana.positiveoffline.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

import javax.annotation.Nullable;

public class OrderWithDiscounts {

    @Embedded
    public Orders orders;

    @Relation(parentColumn = "id", entityColumn = "order_id", entity = OrderDiscounts.class)
    public List<OrderDiscounts> orderWithDiscountList;

    @Relation(parentColumn = "transaction_id", entityColumn = "transaction_id", entity = PostedDiscounts.class)
    public List<PostedDiscounts> postedDiscounts;

    @Relation(parentColumn = "transaction_id", entityColumn = "id", entity = Transactions.class)
    public Transactions transactions;

}
