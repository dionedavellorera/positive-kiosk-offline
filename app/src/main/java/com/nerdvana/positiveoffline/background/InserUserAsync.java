package com.nerdvana.positiveoffline.background;

import android.os.AsyncTask;
import android.util.Log;

import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class InserUserAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchUserResponse.Result> list;
    private UserViewModel userViewModel;
    public InserUserAsync(List<FetchUserResponse.Result> list, SyncCallback syncCallback,
                          UserViewModel userViewModel) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.userViewModel = userViewModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<User> users = new ArrayList<>();
        for (FetchUserResponse.Result r : list) {
            User user = new User();
            user.setId(r.getId());
            user.setUser_id(r.getUserId());
            user.setName(r.getName());
            user.setEmail(r.getEmail());
            user.setUsername(r.getUsername());
            user.setPassword(r.getPassword());
            user.setIs_logged_in(false);
            users.add(user);
        }

        userViewModel.insert(users);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("user");
    }
}
