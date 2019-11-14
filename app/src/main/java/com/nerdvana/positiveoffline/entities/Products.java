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

    public Products(int core_id,
                    @NonNull String product, @NonNull String product_initial,
                    String product_barcode, @NonNull Double tax_rate,
                    String image_file, Double amount,
                    Double mark_up, String category,
                    String department) {
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
