package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SerialNumbers")
public class SerialNumbers {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int transaction_id;
    private String serial_number;
    private String treg;

    private boolean is_void = false;
    private String is_void_at = "";
    private int product_core_id;
    private String product_name;

    private int for_update = 0;
    private int is_sent_to_server = 0;
    private int order_id;

    private int machine_id;
    private int branch_id;
    public SerialNumbers(int transaction_id, String serial_number,
                         String treg, int product_core_id,
                         String product_name, int order_id,
                         int machine_id, int branch_id) {
        this.machine_id = machine_id;
        this.branch_id = branch_id;
        this.product_core_id = product_core_id;
        this.product_name = product_name;
        this.transaction_id = transaction_id;
        this.serial_number = serial_number;
        this.treg = treg;
        this.order_id = order_id;
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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getFor_update() {
        return for_update;
    }

    public void setFor_update(int for_update) {
        this.for_update = for_update;
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

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }


    public String getTreg() {
        return treg;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public boolean isIs_void() {
        return is_void;
    }

    public void setIs_void(boolean is_void) {
        this.is_void = is_void;
    }

    public String getIs_void_at() {
        return is_void_at;
    }

    public void setIs_void_at(String is_void_at) {
        this.is_void_at = is_void_at;
    }

    public int getProduct_core_id() {
        return product_core_id;
    }

    public void setProduct_core_id(int product_core_id) {
        this.product_core_id = product_core_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getIs_sent_to_server() {
        return is_sent_to_server;
    }

    public void setIs_sent_to_server(int is_sent_to_server) {
        this.is_sent_to_server = is_sent_to_server;
    }
}
