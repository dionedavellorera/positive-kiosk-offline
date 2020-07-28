package com.nerdvana.positiveoffline.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    private LiveData<List<User>> mAllUser;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mAllUser = mRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUser() { return mAllUser; }

    public void insert(List<User> user) { mRepository.insert(user); }

    public List<User> searchLoggedInUser() throws ExecutionException, InterruptedException {
        return mRepository.searchLoggedInUser();
    }

    public void fetchUserRequest() {
        mRepository.fetchUserRequest();
    }
    public MutableLiveData<FetchUserResponse> getUserResponseMutableData() {
        return mRepository.getUserResponseLiveData();
    }

    public List<User> isExisting(String username) {
        return mRepository.getUser(username);
    }

    public User searchUsername(String username) throws ExecutionException, InterruptedException {
        return mRepository.searchUsername(username);
    }

    public void update(User user) {
        mRepository.update(user);
    }

}
