package com.nerdvana.positiveoffline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
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
//    @Query("SELECT * FROM Products WHERE departmentId = 3 OR departmentId = 4 OR departmentId = 5")
    List<Products> productsList();

    @Query("DELETE FROM Products")
    void truncateProducts();

    @Query("DELETE FROM ProductAlacart")
    void truncateAlacarts();

    @Query("DELETE FROM BranchGroup")
    void truncateBranchGroup();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlacart(List<ProductAlacart> productsList);

    @Query("SELECT * FROM BranchGroup WHERE product_group_id = :product_id")
    List<BranchGroup> getBranchGroup(String product_id);


    @Query("SELECT * FROM BranchGroup WHERE product_id = :product_id AND product_group_id = :product_group_id")
    List<BranchGroup> getBranchGroupViaProductIdAndProductGroupId(String product_id, String product_group_id);


    @Query("SELECT * FROM BranchGroup WHERE branch_group_id = :branch_group_id")
    List<BranchGroup> getFilteredProductsPerCategory(String branch_group_id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBranchGroup(List<BranchGroup> productsList);

    @Query("SELECT * FROM ProductAlacart WHERE product_id = :product_id")
    List<ProductAlacart> getBranchAlacart(String product_id);


}
