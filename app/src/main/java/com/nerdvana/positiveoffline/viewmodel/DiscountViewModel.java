package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.repository.DiscountsRepository;
import com.nerdvana.positiveoffline.repository.ProductsRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscountViewModel extends AndroidViewModel {
    private DiscountsRepository discountsRepository;

    public DiscountViewModel(@NonNull Application application) {
        super(application);
         discountsRepository = new DiscountsRepository(application);
    }

    public List<DiscountWithSettings> getDiscountWithSetting() throws ExecutionException, InterruptedException {
        return discountsRepository.getDiscountWithSettingsList();
    }

    public List<DiscountWithSettings> getDiscountMenuList() throws ExecutionException, InterruptedException {
        return discountsRepository.getDiscountWithMenuList();
    }

    public List<DiscountWithSettings> getCustomDiscountList() throws ExecutionException, InterruptedException {
        return discountsRepository.getCustomDiscountList();
    }

    public List<OrderWithDiscounts> getOrderWithDiscount(String transaction_id) throws ExecutionException, InterruptedException {
        return discountsRepository.getOrderWithDiscountList(transaction_id);
    }

    public void insertOrderDiscount(List<OrderDiscounts> orderDiscountsList) {
        discountsRepository.insertOrderDiscount(orderDiscountsList);
    }

}
