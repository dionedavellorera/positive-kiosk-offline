package com.nerdvana.positiveoffline.model;

public class SettingsMenuModel {
    private int id;
    private String menuName;
    boolean isActive;

    public SettingsMenuModel(int id, String menuName,
                             boolean isActive) {
        this.id = id;
        this.menuName = menuName;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
