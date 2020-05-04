package com.nerdvana.positiveoffline.intf;

import android.view.View;

import com.nerdvana.positiveoffline.entities.Payments;

public interface PaymentsContract {
    void clicked(Payments payments);
    void clicked(Payments payments, View view);
}
