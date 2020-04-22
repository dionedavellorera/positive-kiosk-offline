package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Orders")
public class Orders {


    @NonNull
    private int transaction_id;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private int core_id;
    @NonNull
    private int qty;
    @NonNull
    private Double amount;
    @NonNull
    private Double original_amount;
    @NonNull
    private String name;
    private Boolean is_void = false;
    private Boolean is_editing = false;
    @NonNull
    private int departmentId;

    private Double vatAmount;
    private Double vatable;
    private Double vatExempt;
    private Double discountAmount;

    private String departmentName;
    private String categoryName;
    private int categoryId;

    private int is_sent_to_server;
    private int machine_id;
    private int branch_id;

    private String treg;

    private int is_room_rate;

    private int is_discount_exempt = 0;

    private int product_alacart_id = 0;
    private int product_group_id = 0;
    private int orders_incremental_id = 0;

    private String notes = "";

    private int is_take_out;

    private String serial_number = "";

    private int is_fixed_asset = 0;

    @Ignore
    public Orders() {}

    public Orders(int transaction_id, int core_id,
                  int qty, @NonNull Double amount,
                  @NonNull Double original_amount,
                  @NonNull String name, int departmentId,
                  Double vatAmount, Double vatable,
                  Double vatExempt, Double discountAmount,
                  String departmentName, String categoryName,
                  int categoryId, int is_sent_to_server,
                  int machine_id, int branch_id,
                  String treg, int is_room_rate,
                  String notes, int is_take_out,
                  int is_fixed_asset) {
        this.is_fixed_asset = is_fixed_asset;
        this.is_take_out = is_take_out;
        this.notes = notes;
        this.is_room_rate = is_room_rate;
        this.treg = treg;
        this.is_sent_to_server = is_sent_to_server;
        this.branch_id = branch_id;
        this.machine_id = machine_id;
        this.categoryId = categoryId;
        this.departmentName = departmentName;
        this.categoryName = categoryName;
        this.transaction_id = transaction_id;
        this.core_id = core_id;
        this.qty = qty;
        this.amount = amount;
        this.original_amount = original_amount;
        this.name = name;
        this.departmentId = departmentId;
        this.vatAmount = vatAmount;
        this.vatable = vatable;
        this.vatExempt = vatExempt;
        this.discountAmount = discountAmount;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public int getIs_fixed_asset() {
        return is_fixed_asset;
    }

    public void setIs_fixed_asset(int is_fixed_asset) {
        this.is_fixed_asset = is_fixed_asset;
    }

    public int getIs_take_out() {
        return is_take_out;
    }

    public void setIs_take_out(int is_take_out) {
        this.is_take_out = is_take_out;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOrders_incremental_id() {
        return orders_incremental_id;
    }

    public void setOrders_incremental_id(int orders_incremental_id) {
        this.orders_incremental_id = orders_incremental_id;
    }

    public int getIs_discount_exempt() {
        return is_discount_exempt;
    }

    public void setIs_discount_exempt(int is_discount_exempt) {
        this.is_discount_exempt = is_discount_exempt;
    }

    public int getIs_room_rate() {
        return is_room_rate;
    }

    public void setIs_room_rate(int is_room_rate) {
        this.is_room_rate = is_room_rate;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Double getVatable() {
        return vatable;
    }

    public void setVatable(Double vatable) {
        this.vatable = vatable;
    }

    public Double getVatExempt() {
        return vatExempt;
    }

    public void setVatExempt(Double vatExempt) {
        this.vatExempt = vatExempt;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getIs_editing() {
        return is_editing;
    }

    public void setIs_editing(Boolean is_editing) {
        this.is_editing = is_editing;
    }

    public Boolean getIs_void() {
        return is_void;
    }

    public void setIs_void(Boolean is_void) {
        this.is_void = is_void;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @NonNull
    public Double getAmount() {
        return amount;
    }

    public void setAmount(@NonNull Double amount) {
        this.amount = amount;
    }

    @NonNull
    public Double getOriginal_amount() {
        return original_amount;
    }

    public void setOriginal_amount(@NonNull Double original_amount) {
        this.original_amount = original_amount;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getProduct_alacart_id() {
        return product_alacart_id;
    }

    public void setProduct_alacart_id(int product_alacart_id) {
        this.product_alacart_id = product_alacart_id;
    }

    public int getProduct_group_id() {
        return product_group_id;
    }

    public void setProduct_group_id(int product_group_id) {
        this.product_group_id = product_group_id;
    }
}
