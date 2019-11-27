package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class DiscountWithSettings {

    @Embedded
    public Discounts discounts;

    @Relation(parentColumn = "core_id", entityColumn = "discount_id", entity = DiscountSettings.class)
    public List<DiscountSettings> discountsList;


}
