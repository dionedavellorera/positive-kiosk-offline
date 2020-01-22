package com.nerdvana.positiveoffline.apirequests;

import java.util.Map;

public class AddOrderDiscountsOfflineRequest {
    private Map<String, Object> mapValue;
    public AddOrderDiscountsOfflineRequest(Map<String, Object> val) {
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
