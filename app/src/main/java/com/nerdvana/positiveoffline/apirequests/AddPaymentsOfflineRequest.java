package com.nerdvana.positiveoffline.apirequests;

import java.util.Map;

public class AddPaymentsOfflineRequest {
    private Map<String, Object> mapValue;
    public AddPaymentsOfflineRequest(Map<String, Object> val) {
        mapValue = val;
    }

    public Map<String, Object> getMapValue() {
        return mapValue;
    }

    @Override
    public String toString() {
        return "SendDataRequest{" +
                "mapValue=" + mapValue +
                '}';
    }



}
