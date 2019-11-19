package com.nerdvana.positiveoffline.apirequests;

import com.nerdvana.positiveoffline.base.BaseRequest;

import java.util.HashMap;
import java.util.Map;

public class FetchPaymentTypeRequest extends BaseRequest {
    private Map<String, String> mapValue;
    public FetchPaymentTypeRequest() {
        mapValue = new HashMap<>();
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }

}
