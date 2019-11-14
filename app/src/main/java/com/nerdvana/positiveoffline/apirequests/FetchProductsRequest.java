package com.nerdvana.positiveoffline.apirequests;

import com.nerdvana.positiveoffline.base.BaseRequest;

import java.util.HashMap;
import java.util.Map;

public class FetchProductsRequest extends BaseRequest {
    private Map<String, String> mapValue;
    public FetchProductsRequest() {
        mapValue = new HashMap<>();
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }
}
