package com.nerdvana.positiveoffline.apirequests;

import java.util.HashMap;
import java.util.Map;

public class FetchDiscountRequest {

    private Map<String, String> mapValue;
    public FetchDiscountRequest() {
        mapValue = new HashMap<>();
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }


}
