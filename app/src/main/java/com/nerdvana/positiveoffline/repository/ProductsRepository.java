package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchProductsRequest;
import com.nerdvana.positiveoffline.apirequests.FetchUserRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.dao.ProductsDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRepository {

    private ProductsDao productsDao;
//    private LiveData<List<User>> allUsers;
    private MutableLiveData<FetchProductsResponse> fetchProductLiveData;

    public ProductsRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        productsDao = posDatabase.productsDao();
        fetchProductLiveData = new MutableLiveData<>();

    }

//    public LiveData<List<User>> getAllUsers() {
//        return allUsers;
//    }

    public void insert(List<Products> user) {
        new ProductsRepository.insertAsyncTask(productsDao).execute(user);
    }


    public MutableLiveData<FetchProductsResponse> getFetchProductLiveData() {
        return fetchProductLiveData;
    }
    //findProductViaBarcode
    public Products findProductViaBarcode(final String barcode) throws ExecutionException, InterruptedException {
        Callable<Products> callable = new Callable<Products>() {
            @Override
            public Products call() throws Exception {
                return productsDao.productViaBarCode(barcode);
            }
        };

        Future<Products> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public List<Products> getProductsList() throws ExecutionException, InterruptedException {
        Callable<List<Products>> callable = new Callable<List<Products>>() {
            @Override
            public List<Products> call() throws Exception {
                return productsDao.productsList();
            }
        };

        Future<List<Products>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();
    }

    public void fetchProductRequest() {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        FetchProductsRequest req = new FetchProductsRequest();

        Call<FetchProductsResponse> call = iUsers.fetchProductsRequest(req.getMapValue());
        call.enqueue(new Callback<FetchProductsResponse>() {
            @Override
            public void onResponse(Call<FetchProductsResponse> call, Response<FetchProductsResponse> response) {
                fetchProductLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<FetchProductsResponse> call, Throwable t) {

            }
        });
    }

    private static class insertAsyncTask extends AsyncTask<List<Products>, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        insertAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<Products>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



}
