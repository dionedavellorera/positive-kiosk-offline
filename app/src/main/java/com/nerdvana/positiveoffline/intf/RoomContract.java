package com.nerdvana.positiveoffline.intf;

import com.nerdvana.positiveoffline.entities.Rooms;

public interface RoomContract {
    void clicked(Rooms rooms);
    void showRates(int room_id);
}
