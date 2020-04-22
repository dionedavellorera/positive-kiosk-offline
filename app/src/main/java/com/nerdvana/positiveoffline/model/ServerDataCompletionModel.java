package com.nerdvana.positiveoffline.model;

public class ServerDataCompletionModel {
    private boolean isOkay;
    private String table;

    public ServerDataCompletionModel(boolean isOkay, String table) {
        this.isOkay = isOkay;
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public boolean isOkay() {
        return isOkay;
    }
}
