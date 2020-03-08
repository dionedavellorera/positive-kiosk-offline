package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.repository.ProductsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductsViewModel extends AndroidViewModel {

    private ProductsRepository productsRepository;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        productsRepository = new ProductsRepository(application);
    }

    public MutableLiveData<FetchProductsResponse> getProductsLiveData() {
        return productsRepository.getFetchProductLiveData();
    }

    public List<Products> getProductsList() throws ExecutionException, InterruptedException {
        return productsRepository.getProductsList();
    }


    public Products findProductViaBarcode(String barcode) throws ExecutionException, InterruptedException {
        return productsRepository.findProductViaBarcode(barcode);
    }

    public void fetchProductsRequest() {
        productsRepository.fetchProductRequest();
    }

    public void insert(List<Products> productsList) {
        productsRepository.insert(productsList);
    }

    public void insertAlaCart(List<ProductAlacart> branchAlaCartList) {
        productsRepository.insertAlacart(branchAlaCartList);
    }

    public void insertBranchGroup(List<BranchGroup> branchGroupList) {
        productsRepository.insertBranchGroup(branchGroupList);
    }
}
