package com.nerdvana.positiveoffline.model;

public class SimpleListModel {
    private int id;
    private String name;
    private Boolean isActive;

    public SimpleListModel(String name, Boolean isActive,
                           int id) {
        this.name = name;
        this.isActive = isActive;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
