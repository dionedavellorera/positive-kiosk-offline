package com.nerdvana.positiveoffline.apirequests;

import java.util.Map;

public class AddPostedDiscountsOfflineRequest {
    private Map<String, Object> mapValue;
    public AddPostedDiscountsOfflineRequest(Map<String, Object> val) {
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
