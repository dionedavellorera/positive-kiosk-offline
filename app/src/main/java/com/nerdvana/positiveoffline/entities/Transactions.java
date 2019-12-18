package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

@Entity(tableName = "Transactions")
public class Transactions {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String control_number;
    @NonNull
    private String user_id;
    private Boolean is_void = false;
    private String is_void_by = "";
    private String void_at = "";
    private Boolean is_completed = false;
    private String is_completed_by = "";
    private String completed_at = "";
    private Boolean is_saved = false;
    private String is_saved_by = "";
    private String saved_at;
    private Boolean is_cut_off = false;
    private String is_cut_off_by = "";
    private String is_cut_off_at = "";
    private String trans_name;
    private String created_at;
    private String receipt_number = "";


    private Double gross_sales = 0.00;
    private Double net_sales = 0.00;
    private Double vatable_sales = 0.00;
    private Double vat_exempt_sales = 0.00;
    private Double vat_amount = 0.00;
    private Double discount_amount;
    private Double change = 0.00;
    private long cut_off_id = 0;

    private int has_special = 0;

    public Transactions(@NonNull String control_number,
                        String user_id, String created_at) {
        this.control_number = control_number;
        this.user_id = user_id;
        this.created_at = created_at;
    }

    @Ignore
    public Transactions(int id, @NonNull String control_number,
                        @NonNull String user_id, Boolean is_void,
                        String is_void_by, Boolean is_completed,
                        String is_completed_by, Boolean is_saved,
                        String is_saved_by, Boolean is_cut_off,
                        String is_cut_off_by, String trans_name,
                        String created_at, String receipt_number,
                        Double gross_sales, Double net_sales,
                        Double vatable_sales, Double vat_exempt_sales,
                        Double vat_amount, Double discountAmount,
                        Double change, String void_at,
                        String completed_at, String saved_at, String is_cut_off_at) {
        this.void_at = void_at;
        this.completed_at = completed_at;
        this.saved_at = saved_at;
        this.is_cut_off_at = is_cut_off_at;
        this.id = id;
        this.control_number = control_number;
        this.user_id = user_id;
        this.is_void = is_void;
        this.is_void_by = is_void_by;
        this.is_completed = is_completed;
        this.is_completed_by = is_completed_by;
        this.is_saved = is_saved;
        this.is_saved_by = is_saved_by;
        this.is_cut_off = is_cut_off;
        this.is_cut_off_by = is_cut_off_by;
        this.trans_name = trans_name;
        this.created_at = created_at;
        this.receipt_number = receipt_number;
        this.gross_sales = gross_sales;
        this.net_sales = net_sales;
        this.vatable_sales = vatable_sales;
        this.vat_exempt_sales = vat_exempt_sales;
        this.vat_amount = vat_amount;
        this.discount_amount = discountAmount;
        this.change = change;
    }

    public int getHas_special() {
        return has_special;
    }

    public void setHas_special(int has_special) {
        this.has_special = has_special;
    }

    public String getVoid_at() {
        return void_at;
    }

    public void setVoid_at(String void_at) {
        this.void_at = void_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(String completed_at) {
        this.completed_at = completed_at;
    }

    public String getSaved_at() {
        return saved_at;
    }

    public void setSaved_at(String saved_at) {
        this.saved_at = saved_at;
    }

    public String getIs_cut_off_at() {
        return is_cut_off_at;
    }

    public void setIs_cut_off_at(String is_cut_off_at) {
        this.is_cut_off_at = is_cut_off_at;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
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

    public Double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getReceipt_number() {
        return receipt_number;
    }

    public void setReceipt_number(String receipt_number) {
        this.receipt_number = receipt_number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setControl_number(@NonNull String control_number) {
        this.control_number = control_number;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public void setIs_saved(Boolean is_saved) {
        this.is_saved = is_saved;
    }

    public Boolean getIs_saved() {
        return is_saved;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getControl_number() {
        return control_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public Boolean getIs_cut_off() {
        return is_cut_off;
    }

    public void setIs_cut_off(Boolean is_cut_off) {
        this.is_cut_off = is_cut_off;
    }

    public String getIs_void_by() {
        return is_void_by;
    }

    public void setIs_void_by(String is_void_by) {
        this.is_void_by = is_void_by;
    }

    public String getIs_completed_by() {
        return is_completed_by;
    }

    public void setIs_completed_by(String is_completed_by) {
        this.is_completed_by = is_completed_by;
    }

    public String getIs_saved_by() {
        return is_saved_by;
    }

    public void setIs_saved_by(String is_saved_by) {
        this.is_saved_by = is_saved_by;
    }

    public String getIs_cut_off_by() {
        return is_cut_off_by;
    }

    public void setIs_cut_off_by(String is_cut_off_by) {
        this.is_cut_off_by = is_cut_off_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(long cut_off_id) {
        this.cut_off_id = cut_off_id;
    }
}
