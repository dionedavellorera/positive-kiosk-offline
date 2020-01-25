package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.User;

import java.util.List;

@Dao
public interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Products> productsList);

    @Query("SELECT * FROM Products WHERE product_barcode=:barcode")
    Products productViaBarCode(String barcode);

    @Query("SELECT * FROM Products")
    List<Products> productsList();

}
