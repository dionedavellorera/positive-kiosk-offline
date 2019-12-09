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
import com.nerdvana.positiveoffline.dao.PostedDiscountsDao;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiscountsRepository {

    private DiscountsDao discountsDao;
    private OrderDiscountsDao orderDiscountsDao;
    private PostedDiscountsDao postedDiscountsDao;

    public DiscountsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        discountsDao = posDatabase.discountsDao();
        orderDiscountsDao = posDatabase.orderDiscountsDao();
        postedDiscountsDao = posDatabase.postedDiscountsDao();

    }


    public PostedDiscounts getPostedDiscounts(final int posted_discount_id) throws ExecutionException, InterruptedException {
        Callable<PostedDiscounts> callable = new Callable<PostedDiscounts>() {
            @Override
            public PostedDiscounts call() throws Exception {
                return postedDiscountsDao.getPostedDiscount(posted_discount_id);
            }
        };

        Future<PostedDiscounts> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<PostedDiscounts> getUnCutOffPostedDiscounts() throws ExecutionException, InterruptedException {
        Callable<List<PostedDiscounts>> callable = new Callable<List<PostedDiscounts>>() {
            @Override
            public List<PostedDiscounts> call() throws Exception {
                return postedDiscountsDao.getUnCutOffPostedDiscounts();
            }
        };

        Future<List<PostedDiscounts>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }
    public List<PostedDiscounts> getZeroEndOfDay() throws ExecutionException, InterruptedException {
        Callable<List<PostedDiscounts>> callable = new Callable<List<PostedDiscounts>>() {
            @Override
            public List<PostedDiscounts> call() throws Exception {
                return postedDiscountsDao.getZeroEndOfDay();
            }
        };

        Future<List<PostedDiscounts>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
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

    public List<OrderDiscounts> getDiscountList(final int posted_discount_id) throws ExecutionException, InterruptedException {
        Callable<List<OrderDiscounts>> callable = new Callable<List<OrderDiscounts>>() {
            @Override
            public List<OrderDiscounts> call() throws Exception {
                return orderDiscountsDao.discountList(posted_discount_id);
            }
        };

        Future<List<OrderDiscounts>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }


    public List<TransactionWithDiscounts> getTransactionWithDiscounts(final String transaction_id) throws ExecutionException, InterruptedException {
        Callable<List<TransactionWithDiscounts>> callable = new Callable<List<TransactionWithDiscounts>>() {
            @Override
            public List<TransactionWithDiscounts> call() throws Exception {
                return postedDiscountsDao.postedDiscountList(transaction_id);
            }
        };

        Future<List<TransactionWithDiscounts>> future = Executors.newSingleThreadExecutor().submit(callable);
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

    public long insertPostedDiscount(PostedDiscounts postedDiscounts) throws ExecutionException, InterruptedException {
        return new DiscountsRepository.insertPostedDiscount(postedDiscountsDao).execute(postedDiscounts).get();
    }

    public void updatedPostedDiscount(PostedDiscounts postedDiscounts) {
        new DiscountsRepository.updateAsyncTask(postedDiscountsDao, postedDiscounts).execute();
    }

    public void updateOrderDiscount(OrderDiscounts orderDiscounts) {
        new DiscountsRepository.updateOrderDiscount(orderDiscountsDao, orderDiscounts).execute();
    }

    private static class updateOrderDiscount extends AsyncTask<PostedDiscounts, Void, Void> {

        private OrderDiscountsDao mAsyncTaskDao;

        private OrderDiscounts orderDiscounts;
        updateOrderDiscount(OrderDiscountsDao dao, OrderDiscounts orderDiscounts) {
            mAsyncTaskDao = dao;
            this.orderDiscounts = orderDiscounts;
        }

        @Override
        protected Void doInBackground(final PostedDiscounts... params) {
            mAsyncTaskDao.update(orderDiscounts);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class updateAsyncTask extends AsyncTask<PostedDiscounts, Void, Void> {

        private PostedDiscountsDao mAsyncTaskDao;

        private PostedDiscounts postedDiscount;
        updateAsyncTask(PostedDiscountsDao dao, PostedDiscounts postedDiscounts) {
            mAsyncTaskDao = dao;
            this.postedDiscount = postedDiscounts;
        }

        @Override
        protected Void doInBackground(final PostedDiscounts... params) {
            mAsyncTaskDao.update(postedDiscount);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class insertPostedDiscount extends AsyncTask<PostedDiscounts, Void, Long> {

        private PostedDiscountsDao mAsyncTaskDao;

        insertPostedDiscount(PostedDiscountsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final PostedDiscounts... params) {
            return mAsyncTaskDao.insert(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }
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
