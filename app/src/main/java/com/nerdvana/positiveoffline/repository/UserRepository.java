package com.nerdvana.positiveoffline.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.apirequests.FetchUserRequest;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.dao.DataSyncDao;
import com.nerdvana.positiveoffline.dao.UserDao;
import com.nerdvana.positiveoffline.database.DatabaseHelper;
import com.nerdvana.positiveoffline.database.PosDatabase;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private MutableLiveData<FetchUserResponse> userResponseLiveData;

    public UserRepository(Application application) {
        PosDatabase posDatabase = DatabaseHelper.getDatabase(application);
        userDao = posDatabase.userDao();
        allUsers = userDao.userList();
        userResponseLiveData = new MutableLiveData<>();

    }

    public List<User> getUser(String username) {
        return userDao.specificUser(username);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(List<User> user) {
        new insertAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new updateAsyncTask(userDao, user).execute();
    }

    public MutableLiveData<FetchUserResponse> getUserResponseLiveData() {
        return userResponseLiveData;
    }



    public void fetchUserRequest() {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        FetchUserRequest req = new FetchUserRequest();

        Call<FetchUserResponse> call = iUsers.fetchUserRequest(req.getMapValue());
        call.enqueue(new Callback<FetchUserResponse>() {
            @Override
            public void onResponse(Call<FetchUserResponse> call, Response<FetchUserResponse> response) {
                userResponseLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<FetchUserResponse> call, Throwable t) {

            }
        });
    }

    private static class insertAsyncTask extends AsyncTask<List<User>, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<User>... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        private User user;
        updateAsyncTask(UserDao dao, User user) {
            mAsyncTaskDao = dao;
            this.user = user;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.update(user);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    public List<User> searchLoggedInUser() throws ExecutionException, InterruptedException {
        Callable<List<User>> callable = new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return userDao.searchLoggedInUser(true);
            }
        };

        Future<List<User>> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }

    public User searchUsername(String username) throws ExecutionException, InterruptedException {
        Callable<User> callable = new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userDao.searchUsername(username);
            }
        };

        Future<User> future = Executors.newSingleThreadExecutor().submit(callable);
        return future.get();

    }


}
