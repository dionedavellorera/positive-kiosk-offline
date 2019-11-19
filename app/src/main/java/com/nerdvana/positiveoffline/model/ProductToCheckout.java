package com.nerdvana.positiveoffline.model;

import com.nerdvana.positiveoffline.entities.Products;

public class ProductToCheckout {
    private Products products;

    public ProductToCheckout(Products products) {
        this.products = products;
    }

    public Products getProducts() {
        return products;
    }
}
