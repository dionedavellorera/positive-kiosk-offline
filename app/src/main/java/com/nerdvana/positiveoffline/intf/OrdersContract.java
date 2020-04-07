package com.nerdvana.positiveoffline.intf;

import android.view.View;

import com.nerdvana.positiveoffline.entities.Orders;

public interface OrdersContract {
    void longClicked(Orders orders, View view);
    void clicked(Orders orders);
}
