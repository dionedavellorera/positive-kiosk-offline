package com.nerdvana.positiveoffline.apirequests;

import java.util.HashMap;
import java.util.Map;

public class SendDataRequest {

    private Map<String, Object> mapValue;
    public SendDataRequest(Map<String, Object> val) {
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
