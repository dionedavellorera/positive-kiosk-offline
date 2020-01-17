package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.SpecialDiscountInfo;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.repository.DiscountsRepository;
import com.nerdvana.positiveoffline.repository.ProductsRepository;

import java.util.ArrayList;
import java.util.Arrays;
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

    public PostedDiscounts getPostedDiscount(int posted_discount_id) throws ExecutionException, InterruptedException {
        return discountsRepository.getPostedDiscounts(posted_discount_id);
    }

    public List<PostedDiscounts> getLastPostedDiscount(int transaction_id) throws ExecutionException, InterruptedException {
        return discountsRepository.getLastPostedDiscount(transaction_id);
    }

    public List<TransactionWithDiscounts> getTransactionWithDiscounts(String transaction_id) throws ExecutionException, InterruptedException {
        return discountsRepository.getTransactionWithDiscounts(transaction_id);
    }

    public List<OrderDiscounts> getDiscountList(int posted_discount_id) throws ExecutionException, InterruptedException {
        return discountsRepository.getDiscountList(posted_discount_id);
    }

    public long insertPostedDiscount(PostedDiscounts postedDiscounts) throws ExecutionException, InterruptedException {
        return discountsRepository.insertPostedDiscount(postedDiscounts);
    }

    public void insertOrderDiscount(List<OrderDiscounts> orderDiscountsList) {
        discountsRepository.insertOrderDiscount(orderDiscountsList);
    }

    public void updatePostedDiscount(PostedDiscounts postedDiscounts) {
        discountsRepository.updatedPostedDiscount(postedDiscounts);
    }

    public void updateOrderDiscount(OrderDiscounts orderDiscounts) {
        discountsRepository.updateOrderDiscount(orderDiscounts);
    }

    public void insertManualDiscount(List<Orders> ordersList, String transactionId,
                                     int discountId, String discountName,
                                     boolean isPercentage, Double amount) {
        long last_inserted_id = 0;
        for (Orders orders : ordersList) {
            try {

                PostedDiscounts postedDiscounts = new PostedDiscounts(
                        Integer.valueOf(transactionId),
                        discountId,
                        discountName,
                        false,
                        "",
                        "",
                        "",
                        isPercentage,
                        amount,
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                        Utils.getDateTimeToday()
                );
                if (last_inserted_id == 0) {
                    last_inserted_id = insertPostedDiscount(postedDiscounts);
                }


                List<OrderDiscounts> orderDiscountsList = new ArrayList<>();

                orderDiscountsList.add(new OrderDiscounts(
                        orders.getCore_id(),
                        isPercentage,
                        amount,
                        Integer.valueOf(transactionId),
                        orders.getId(),
                        discountName,
                        last_inserted_id,
                        false,
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                        Utils.getDateTimeToday()));

                insertOrderDiscount(orderDiscountsList);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    public void insertDiscount(List<Orders> ordersList, DiscountWithSettings discountWithSettings,
                               String transactionId, SpecialDiscountInfo specialDiscountInfo) {
        long last_inserted_id = 0;
        for (Orders orders : ordersList) {
            int count = 0; //2 for discounted counted
            double percentage = 0.00;
            String discName = "";
            int discId = 0;
            for (DiscountSettings disc : discountWithSettings.discountsList) {
                if (!TextUtils.isEmpty(disc.getProduct_id())) {
                    if (disc.getProduct_id().equalsIgnoreCase("all")) {
                        count+=1;
                    } else if (Arrays.asList(disc.getProduct_id().split(",")).contains(String.valueOf(orders.getCore_id()))) {
                        count+=1;
                    }
                }

                if (!TextUtils.isEmpty(disc.getDepartment_id())) {
                    if (disc.getDepartment_id().equalsIgnoreCase("all")) {
                        count+=1;
                    } else if (Arrays.asList(disc.getDepartment_id().split(",")).contains(String.valueOf(orders.getDepartmentId()))) {
                        count+=1;
                    }
                }

                percentage = disc.getPercentage();
                discName = disc.getDiscount_name();
                discId = discountWithSettings.discounts.getCore_id();
            }
            if (count == 2) {
                try {
                    if (last_inserted_id == 0) {



                        PostedDiscounts postedDiscounts = new PostedDiscounts(
                                Integer.valueOf(transactionId),
                                discId,
                                discName,
                                false,
                                specialDiscountInfo.getCard_number(),
                                specialDiscountInfo.getName(),
                                specialDiscountInfo.getAddress(),
                                true,
                                percentage,
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday()

                        );

                        last_inserted_id = insertPostedDiscount(postedDiscounts);
                    }
                    List<OrderDiscounts> orderDiscountsList = new ArrayList<>();
                    orderDiscountsList.add(new OrderDiscounts(
                            orders.getCore_id(),
                            true,
                            percentage,
                            Integer.valueOf(transactionId),
                            orders.getId(),
                            discName,
                            last_inserted_id,
                            false,
                            0,
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                            Utils.getDateTimeToday()));
                    insertOrderDiscount(orderDiscountsList);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
