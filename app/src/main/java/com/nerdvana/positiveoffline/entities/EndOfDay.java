package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "EndOfDay")
public class EndOfDay {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private int number_of_transactions;
    private Double gross_sales;
    private Double net_sales;
    private Double vatable_sales;
    private Double vat_exempt_sales;
    private Double vat_amount;
    private Double void_amount;
    private Double total_cash_amount;
    private String created_at;

    public EndOfDay(int number_of_transactions, Double gross_sales,
                    Double net_sales, Double vatable_sales,
                    Double vat_exempt_sales, Double vat_amount,
                    Double void_amount, Double total_cash_amount,
                    String created_at) {
        this.number_of_transactions = number_of_transactions;
        this.gross_sales = gross_sales;
        this.net_sales = net_sales;
        this.vatable_sales = vatable_sales;
        this.vat_exempt_sales = vat_exempt_sales;
        this.vat_amount = vat_amount;
        this.void_amount = void_amount;
        this.total_cash_amount = total_cash_amount;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber_of_transactions() {
        return number_of_transactions;
    }

    public void setNumber_of_transactions(int number_of_transactions) {
        this.number_of_transactions = number_of_transactions;
    }

    public Double getGross_sales() {
        return gross_sales;
    }

    public void setGross_sales(Double gross_sales) {
        this.gross_sales = gross_sales;
    }

    public Double getNet_sales() {
        return net_sales;
    }

    public void setNet_sales(Double net_sales) {
        this.net_sales = net_sales;
    }

    public Double getVatable_sales() {
        return vatable_sales;
    }

    public void setVatable_sales(Double vatable_sales) {
        this.vatable_sales = vatable_sales;
    }

    public Double getVat_exempt_sales() {
        return vat_exempt_sales;
    }

    public void setVat_exempt_sales(Double vat_exempt_sales) {
        this.vat_exempt_sales = vat_exempt_sales;
    }

    public Double getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(Double vat_amount) {
        this.vat_amount = vat_amount;
    }

    public Double getVoid_amount() {
        return void_amount;
    }

    public void setVoid_amount(Double void_amount) {
        this.void_amount = void_amount;
    }

    public Double getTotal_cash_amount() {
        return total_cash_amount;
    }

    public void setTotal_cash_amount(Double total_cash_amount) {
        this.total_cash_amount = total_cash_amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
