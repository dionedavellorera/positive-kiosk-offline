package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    private String saved_at = "";
    private Boolean is_cut_off = false;
    private String is_cut_off_by = "";
    private String is_cut_off_at = "";

    private Boolean is_backed_out = false;
    private String is_backed_out_by = "";
    private String is_backed_out_at = "";

    private String trans_name = "";
    private String treg = "";
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

    private Boolean is_cancelled = false;
    private String is_cancelled_by = "";
    private String is_cancelled_at = "";

    private String tin_number = "";

    private int is_sent_to_server = 0;
    private int machine_id;
    private int branch_id;

    private int room_id = 0;
    private String room_number = "";

    private String check_in_time = "";
    private String check_out_time = "";

    private Double service_charge_value = 0.00;
    private boolean service_charge_is_percentage = false;

    private int is_shared = 0;

    private String transaction_type = "";

    private String delivery_to = "";
    private String delivery_address = "";

    private int to_id = 0;
    private int to_transaction_id = 0;
    private int is_temp = 0;
    private String to_control_number = "";
    private String shift_number = "";

    public Transactions(@NonNull String control_number,
                        String user_id, String treg,
                        int is_sent_to_server,
                        int machine_id, int branch_id,
                        int to_id, int is_temp,
                        String shift_number) {
        this.shift_number = shift_number;
        this.is_temp = is_temp;
        this.to_id = to_id;
        this.control_number = control_number;
        this.user_id = user_id;
        this.treg = treg;

        this.is_sent_to_server = is_sent_to_server;
        this.machine_id = machine_id;
        this.branch_id = branch_id;
    }

    @Ignore
    public Transactions() {}

    @Ignore
    public Transactions(int id, @NonNull String control_number,
                        @NonNull String user_id, Boolean is_void,
                        String is_void_by, Boolean is_completed,
                        String is_completed_by, Boolean is_saved,
                        String is_saved_by, Boolean is_cut_off,
                        String is_cut_off_by, String trans_name,
                        String treg, String receipt_number,
                        Double gross_sales, Double net_sales,
                        Double vatable_sales, Double vat_exempt_sales,
                        Double vat_amount, Double discountAmount,
                        Double change, String void_at,
                        String completed_at, String saved_at, String is_cut_off_at,
                        Boolean is_cancelled, String is_cancelled_by, String is_cancelled_at,
                        String tin_number,
                        Boolean is_backed_out, String is_backed_out_by, String is_backed_out_at,
                        String delivery_to, String delivery_address,
                        int to_id, int is_temp,
                        String to_control_number, String shift_number) {
        this.shift_number = shift_number;
        this.to_control_number = to_control_number;
        this.is_temp = is_temp;
        this.to_id = to_id;
        this.delivery_to = delivery_to;
        this.delivery_address = delivery_address;
        this.is_backed_out_by = is_backed_out_by;
        this.is_backed_out_at = is_backed_out_at;
        this.is_backed_out = is_backed_out;
        this.tin_number = tin_number;
        this.is_cancelled = is_cancelled;
        this.is_cancelled_by = is_cancelled_by;
        this.is_cancelled_at = is_cancelled_at;
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
        this.treg = treg;
        this.receipt_number = receipt_number;
        this.gross_sales = gross_sales;
        this.net_sales = net_sales;
        this.vatable_sales = vatable_sales;
        this.vat_exempt_sales = vat_exempt_sales;
        this.vat_amount = vat_amount;
        this.discount_amount = discountAmount;
        this.change = change;
    }

    public String getShift_number() {
        return shift_number;
    }

    public void setShift_number(String shift_number) {
        this.shift_number = shift_number;
    }

    public String getTo_control_number() {
        return to_control_number;
    }

    public void setTo_control_number(String to_control_number) {
        this.to_control_number = to_control_number;
    }

    public int getIs_temp() {
        return is_temp;
    }

    public void setIs_temp(int is_temp) {
        this.is_temp = is_temp;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public String getDelivery_to() {
        return delivery_to;
    }

    public void setDelivery_to(String delivery_to) {
        this.delivery_to = delivery_to;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public Boolean getIs_backed_out() {
        return is_backed_out;
    }

    public void setIs_backed_out(Boolean is_backed_out) {
        this.is_backed_out = is_backed_out;
    }

    public String getIs_backed_out_by() {
        return is_backed_out_by;
    }

    public void setIs_backed_out_by(String is_backed_out_by) {
        this.is_backed_out_by = is_backed_out_by;
    }

    public String getIs_backed_out_at() {
        return is_backed_out_at;
    }

    public void setIs_backed_out_at(String is_backed_out_at) {
        this.is_backed_out_at = is_backed_out_at;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
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

    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(String tin_number) {
        this.tin_number = tin_number;
    }

    public void setIs_cancelled(Boolean is_cancelled) {
        this.is_cancelled = is_cancelled;
    }

    public void setIs_cancelled_by(String is_cancelled_by) {
        this.is_cancelled_by = is_cancelled_by;
    }

    public void setIs_cancelled_at(String is_cancelled_at) {
        this.is_cancelled_at = is_cancelled_at;
    }

    public Boolean getIs_cancelled() {
        return is_cancelled;
    }

    public String getIs_cancelled_by() {
        return is_cancelled_by;
    }

    public String getIs_cancelled_at() {
        return is_cancelled_at;
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

    public String getTreg() {
        return treg;
    }

    public void setTreg(String treg) {
        this.treg = treg;
    }

    public long getCut_off_id() {
        return cut_off_id;
    }

    public void setCut_off_id(long cut_off_id) {
        this.cut_off_id = cut_off_id;
    }

    public Double getService_charge_value() {
        return service_charge_value;
    }

    public void setService_charge_value(Double service_charge_value) {
        this.service_charge_value = service_charge_value;
    }

    public boolean isService_charge_is_percentage() {
        return service_charge_is_percentage;
    }

    public void setService_charge_is_percentage(boolean service_charge_is_percentage) {
        this.service_charge_is_percentage = service_charge_is_percentage;
    }

    public int getIs_shared() {
        return is_shared;
    }

    public void setIs_shared(int is_shared) {
        this.is_shared = is_shared;
    }


    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getTo_transaction_id() {
        return to_transaction_id;
    }

    public void setTo_transaction_id(int to_transaction_id) {
        this.to_transaction_id = to_transaction_id;
    }
}
