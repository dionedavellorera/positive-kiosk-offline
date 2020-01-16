package com.nerdvana.positiveoffline.model;

import androidx.room.Embedded;
import androidx.room.Relation;
import androidx.room.Room;

import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Transactions;

import java.util.List;

public class RoomWithRates {
    @Embedded
    public Rooms rooms;

    @Relation(parentColumn = "room_id", entityColumn = "room_id", entity = RoomRates.class)
    public List<RoomRates> orderWithDiscountList;

}
