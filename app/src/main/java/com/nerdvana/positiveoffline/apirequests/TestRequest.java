package com.nerdvana.positiveoffline.apirequests;

import com.nerdvana.positiveoffline.base.BaseRequest;

import java.util.HashMap;
import java.util.Map;

public class TestRequest extends BaseRequest {
    private Map<String, String> mapValue;

    public TestRequest(String test) {
        mapValue = new HashMap<>();
        mapValue.put("test_data", test);
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }

}
