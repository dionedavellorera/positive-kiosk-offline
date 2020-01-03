package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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

    public Orders(int transaction_id, int core_id,
                  int qty, @NonNull Double amount,
                  @NonNull Double original_amount,
                  @NonNull String name, int departmentId,
                  Double vatAmount, Double vatable,
                  Double vatExempt, Double discountAmount,
                  String departmentName, String categoryName,
                  int categoryId) {
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
}
