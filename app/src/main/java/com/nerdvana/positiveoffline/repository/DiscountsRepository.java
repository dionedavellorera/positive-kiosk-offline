package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchProductsRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.dao.DiscountsDao;
import com.nerdvana.positiveoffline.dao.OrderDiscountsDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiscountsRepository {

    private DiscountsDao discountsDao;
    private OrderDiscountsDao orderDiscountsDao;

    public DiscountsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        discountsDao = posDatabase.discountsDao();
        orderDiscountsDao = posDatabase.orderDiscountsDao();

    }

    public List<OrderWithDiscounts> getOrderWithDiscountList(final String transaction_id) throws ExecutionException, InterruptedException {
        Callable<List<OrderWithDiscounts>> callable = new Callable<List<OrderWithDiscounts>>() {
            @Override
            public List<OrderWithDiscounts> call() throws Exception {
                return orderDiscountsDao.orderDiscountList(transaction_id);
            }
        };

        Future<List<OrderWithDiscounts>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<DiscountWithSettings> getCustomDiscountList() throws ExecutionException, InterruptedException {
        Callable<List<DiscountWithSettings>> callable = new Callable<List<DiscountWithSettings>>() {
            @Override
            public List<DiscountWithSettings> call() throws Exception {
                return discountsDao.customDiscounts();
            }
        };

        Future<List<DiscountWithSettings>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<DiscountWithSettings> getDiscountWithSettingsList() throws ExecutionException, InterruptedException {
        Callable<List<DiscountWithSettings>> callable = new Callable<List<DiscountWithSettings>>() {
            @Override
            public List<DiscountWithSettings> call() throws Exception {
                return discountsDao.discountWithSettings();
            }
        };

        Future<List<DiscountWithSettings>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<DiscountWithSettings> getDiscountWithMenuList() throws ExecutionException, InterruptedException {
        Callable<List<DiscountWithSettings>> callable = new Callable<List<DiscountWithSettings>>() {
            @Override
            public List<DiscountWithSettings> call() throws Exception {
                return discountsDao.menuDiscountList();
            }
        };

        Future<List<DiscountWithSettings>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public void insertOrderDiscount(List<OrderDiscounts> orderDiscountsList) {
        new DiscountsRepository.insertOrderDiscount(orderDiscountsDao).execute(orderDiscountsList);
    }


    private static class insertOrderDiscount extends AsyncTask<List<OrderDiscounts>, Void, Void> {

        private OrderDiscountsDao mAsyncTaskDao;

        insertOrderDiscount(OrderDiscountsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<OrderDiscounts>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



}
