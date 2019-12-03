package com.nerdvana.positiveoffline.model;

public class SimpleListModel {
    private String name;
    private Boolean isActive;

    public SimpleListModel(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
