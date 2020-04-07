package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CutOff")
public class CutOff {
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
    private Double total_cash_payments = 0.00;
    private Double total_card_payments = 0.00;
    private Double total_change;
    private int z_read_id = 0;
    private String treg;
    private int seniorCount = 0;
    private Double seniorAmount = 0.00;
    private int pwdCount = 0;
    private Double pwdAmount = 0.00;
    private int othersCount = 0;
    private Double othersAmount = 0.00;
    private Double total_payout = 0.00;

    private String begOrNo = "";
    private String endOrNo = "";

    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private Double total_service_charge = 0.00;

    public CutOff(int number_of_transactions, Double gross_sales,
                  Double net_sales, Double vatable_sales,
                  Double vat_exempt_sales, Double vat_amount,
                  Double void_amount, Double total_cash_amount,
                  int z_read_id, String treg,
                  String begOrNo, String endOrNo,
                  int is_sent_to_server, int machine_id,
                  int branch_id, Double total_service_charge) {
        this.branch_id = branch_id;
        this.is_sent_to_server = is_sent_to_server;
        this.machine_id = machine_id;
        this.number_of_transactions = number_of_transactions;
        this.gross_sales = gross_sales;
        this.net_sales = net_sales;
        this.vatable_sales = vatable_sales;
        this.vat_exempt_sales = vat_exempt_sales;
        this.vat_amount = vat_amount;
        this.void_amount = void_amount;
        this.total_cash_amount = total_cash_amount;
        this.z_read_id = z_read_id;
        this.treg = treg;
        this.begOrNo = begOrNo;
        this.endOrNo = endOrNo;
        this.total_service_charge = total_service_charge;
    }

    public Double getTotal_service_charge() {
        return total_service_charge;
    }

    public void setTotal_service_charge(Double total_service_charge) {
        this.total_service_charge = total_service_charge;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
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

    public String getBegOrNo() {
        return begOrNo;
    }

    public void setBegOrNo(String begOrNo) {
        this.begOrNo = begOrNo;
    }

    public String getEndOrNo() {
        return endOrNo;
    }

    public void setEndOrNo(String endOrNo) {
        this.endOrNo = endOrNo;
    }

    public int getSeniorCount() {
        return seniorCount;
    }

    public void setSeniorCount(int seniorCount) {
        this.seniorCount = seniorCount;
    }

    public Double getSeniorAmount() {
        return seniorAmount;
    }

    public void setSeniorAmount(Double seniorAmount) {
        this.seniorAmount = seniorAmount;
    }

    public int getPwdCount() {
        return pwdCount;
    }

    public void setPwdCount(int pwdCount) {
        this.pwdCount = pwdCount;
    }

    public Double getPwdAmount() {
        return pwdAmount;
    }

    public void setPwdAmount(Double pwdAmount) {
        this.pwdAmount = pwdAmount;
    }

    public int getOthersCount() {
        return othersCount;
    }

    public void setOthersCount(int othersCount) {
        this.othersCount = othersCount;
    }

    public Double getOthersAmount() {
        return othersAmount;
    }

    public void setOthersAmount(Double othersAmount) {
        this.othersAmount = othersAmount;
    }

    public Double getTotal_cash_payments() {
        return total_cash_payments;
    }

    public void setTotal_cash_payments(Double total_cash_payments) {
        this.total_cash_payments = total_cash_payments;
    }

    public Double getTotal_card_payments() {
        return total_card_payments;
    }

    public void setTotal_card_payments(Double total_card_payments) {
        this.total_card_payments = total_card_payments;
    }

    public Double getTotal_change() {
        return total_change;
    }

    public void setTotal_change(Double total_change) {
        this.total_change = total_change;
    }

    public String getTreg() {
        return treg;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public int getZ_read_id() {
        return z_read_id;
    }

    public void setZ_read_id(int z_read_id) {
        this.z_read_id = z_read_id;
    }

    public Double getTotal_cash_amount() {
        return total_cash_amount;
    }

    public void setTotal_cash_amount(Double total_cash_amount) {
        this.total_cash_amount = total_cash_amount;
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

    public Double getTotal_payout() {
        return total_payout;
    }

    public void setTotal_payout(Double total_payout) {
        this.total_payout = total_payout;
    }
}
