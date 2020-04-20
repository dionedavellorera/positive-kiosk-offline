package com.nerdvana.positiveoffline.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Products")
public class Products {

    @NonNull
    @PrimaryKey
    private int core_id;
    @NonNull
    private String product;
    @NonNull
    private String product_initial;
    private String product_barcode;
    @NonNull
    private Double tax_rate;
    private String image_file;
    private Double amount;
    private Double mark_up;
    private String category;
    private String department;

    @NonNull
    private int departmentId;
    @NonNull
    private int categoryId;

    private int is_fixed_asset = 0;
    private String json_promo = "";
    public Products(int core_id,
                    @NonNull String product, @NonNull String product_initial,
                    String product_barcode, @NonNull Double tax_rate,
                    String image_file, Double amount,
                    Double mark_up, String category,
                    String department, int departmentId,
                    int categoryId, int is_fixed_asset,
                    String json_promo) {
        this.json_promo = json_promo;
        this.is_fixed_asset = is_fixed_asset;
        this.core_id = core_id;
        this.product = product;
        this.product_initial = product_initial;
        this.product_barcode = product_barcode;
        this.tax_rate = tax_rate;
        this.image_file = image_file;
        this.amount = amount;
        this.mark_up = mark_up;
        this.category = category;
        this.department = department;
        this.departmentId = departmentId;
        this.categoryId = categoryId;
    }

    public String getJson_promo() {
        return json_promo;
    }

    public void setJson_promo(String json_promo) {
        this.json_promo = json_promo;
    }

    public int getIs_fixed_asset() {
        return is_fixed_asset;
    }

    public void setIs_fixed_asset(int is_fixed_asset) {
        this.is_fixed_asset = is_fixed_asset;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public void setProduct(@NonNull String product) {
        this.product = product;
    }

    public void setProduct_initial(@NonNull String product_initial) {
        this.product_initial = product_initial;
    }

    public void setProduct_barcode(String product_barcode) {
        this.product_barcode = product_barcode;
    }

    public void setTax_rate(@NonNull Double tax_rate) {
        this.tax_rate = tax_rate;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setMark_up(Double mark_up) {
        this.mark_up = mark_up;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getCore_id() {
        return core_id;
    }

    @NonNull
    public String getProduct() {
        return product;
    }

    @NonNull
    public String getProduct_initial() {
        return product_initial;
    }

    public String getProduct_barcode() {
        return product_barcode;
    }

    @NonNull
    public Double getTax_rate() {
        return tax_rate;
    }

    public String getImage_file() {
        return image_file;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getMark_up() {
        return mark_up;
    }

    public String getCategory() {
        return category;
    }

    public String getDepartment() {
        return department;
    }
}
