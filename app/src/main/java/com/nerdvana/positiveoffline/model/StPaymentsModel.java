package com.nerdvana.positiveoffline.model;

import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;

import java.util.List;

public class StPaymentsModel {
    private int id;
    private boolean isSelected;
    private String name;
    private List<Payments> paymentsList;
    private List<Orders> ordersList;

    public StPaymentsModel(int id,
                           String name, List<Payments> paymentsList,
                           List<Orders> ordersList, boolean isSelected) {
        this.isSelected = false;
        this.id = id;
        this.name = name;
        this.paymentsList = paymentsList;
        this.ordersList = ordersList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public String getName() {
        return name;
    }

    public List<Payments> getPaymentsList() {
        return paymentsList;
    }
}
