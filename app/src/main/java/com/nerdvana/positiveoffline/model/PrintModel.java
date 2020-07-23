package com.nerdvana.positiveoffline.model;

public class PrintModel {

    private String type;
    private String data;
    private String additionalData;

    public PrintModel(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public PrintModel(String type, String data, String additionalData) {
        this.additionalData = additionalData;
        this.type = type;
        this.data = data;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
