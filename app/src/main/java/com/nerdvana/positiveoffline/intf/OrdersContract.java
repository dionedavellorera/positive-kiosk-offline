package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.entities.Orders;

public interface OrdersContract {
    void longClicked(Orders orders);
    void clicked(Orders orders);
}
