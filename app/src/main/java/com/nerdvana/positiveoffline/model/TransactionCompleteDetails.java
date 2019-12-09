package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class TransactionCompleteDetails {
    @Embedded
    public Transactions transactions;

    @Relation(parentColumn = "id", entityColumn = "transaction_id", entity = Orders.class)
    public List<Orders> ordersList;

    @Relation(parentColumn = "id", entityColumn = "transaction_id", entity = PostedDiscounts.class)
    public List<PostedDiscounts> postedDiscountsList;

    @Relation(parentColumn = "id", entityColumn = "transaction_id", entity = Payments.class)
    public List<Payments> paymentsList;
}
