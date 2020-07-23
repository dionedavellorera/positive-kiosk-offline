package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostedDiscounts")
public class PostedDiscounts {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int transaction_id;
    @NonNull
    private int discount_id;
    @NonNull
    private String discount_name;
    private Boolean is_void = false;

    private String card_number;
    private String name;
    private String address;

    private int cut_off_id = 0;
    private int end_of_day_id = 0;

    private Double amount=  0.00;

    private Boolean is_percentage;
    private Double discount_value;

    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private String treg;

    private int to_id = 0;

    private double qty = 0.00;

    @Ignore
    public PostedDiscounts() {}

    public PostedDiscounts(int transaction_id, int discount_id,
                           String discount_name, Boolean is_void,
                           String card_number, String name,
                           String address, Boolean is_percentage,
                           Double discount_value, int is_sent_to_server,
                           int machine_id, int branch_id,
                           String treg, int to_id,
                           double qty) {
        this.qty = qty;
        this.to_id = to_id;
        this.treg = treg;
        this.is_sent_to_server = is_sent_to_server;
        this.machine_id = machine_id;
        this.branch_id = branch_id;
        this.transaction_id = transaction_id;
        this.discount_id = discount_id;
        this.discount_name = discount_name;
        this.is_void = is_void;
        this.card_number = card_number;
        this.name = name;
        this.address = address;
        this.is_percentage = is_percentage;
        this.discount_value = discount_value;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public String getTreg() {
        return treg;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public int getIs_sent_to_server() {
        return is_sent_to_server;
    }

    public void setIs_sent_to_server(int is_sent_to_server) {
        this.is_sent_to_server = is_sent_to_server;
    }

    public int getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(int machine_id) {
        this.machine_id = machine_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public Boolean getIs_percentage() {
        return is_percentage;
    }

    public void setIs_percentage(Boolean is_percentage) {
        this.is_percentage = is_percentage;
    }

    public Double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(Double discount_value) {
        this.discount_value = discount_value;
    }

    public int getEnd_of_day_id() {
        return end_of_day_id;
    }

    public void setEnd_of_day_id(int end_of_day_id) {
        this.end_of_day_id = end_of_day_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(int cut_off_id) {
        this.cut_off_id = cut_off_id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }
}
