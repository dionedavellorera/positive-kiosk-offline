package com.nerdvana.positiveoffline;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);
    public static Bus getInstance() {
        return BUS;
    }
    private BusProvider() {
    }
}
