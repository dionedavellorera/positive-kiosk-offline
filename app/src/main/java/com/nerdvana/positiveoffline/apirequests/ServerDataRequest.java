package com.nerdvana.positiveoffline.apirequests;

import java.util.Map;

public class ServerDataRequest {
    private Map<String, String> mapValue;
    public ServerDataRequest(Map<String, String> val) {
        mapValue = val;
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }

    @Override
    public String toString() {
        return "SendDataRequest{" +
                "mapValue=" + mapValue +
                '}';
    }
}
