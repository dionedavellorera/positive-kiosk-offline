package com.nerdvana.positiveoffline.model;

public class StringModel {
    private int id;
    private String string;

    public StringModel(int id, String string) {
        this.id = id;
        this.string = string;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
