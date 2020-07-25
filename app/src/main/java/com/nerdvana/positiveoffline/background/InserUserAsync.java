package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchUserResponse;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InserUserAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchUserResponse.Result> list;
    private UserViewModel userViewModel;
    private Context context;
    public InserUserAsync(List<FetchUserResponse.Result> list, SyncCallback syncCallback,
                          UserViewModel userViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.userViewModel = userViewModel;
        this.context = context;
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
            if (r.getRole() != null) {
                user.setRole(r.getRole().getRole());
                if (r.getRole().getGroup() != null) {
                    user.setUser_group(r.getRole().getGroup().getUser_group());
                    user.setAccess(r.getRole().getGroup().getAccess());
                }
            }
            user.setIs_logged_in(false);
            users.add(user);


//            if (r.getImage_file() != null && !TextUtils.isEmpty(r.getImage_file())) {
//                File direct = new File(Environment.getExternalStorageDirectory()
//                        + "/POS/USERS");
//                if (!direct.exists()) {
//                    direct.mkdirs();
//                }
//                File directory = Environment.getExternalStorageDirectory();
//                File file = new File(directory, "/POS/USERS/" + r.getUsername() + ".jpg");
//
//                if (!file.exists()) {
//
//                    if (!TextUtils.isEmpty(r.getImage_file())) {
//                        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
////                        Uri downloadUri = Uri.parse(SharedPreferenceManager.getString(null, AppConstants.HOST) + "/uploads/icon/" + r.getImage());
//                        Uri downloadUri = Uri.parse(r.getImage_file());
//                        DownloadManager.Request request = new DownloadManager.Request(
//                                downloadUri);
//
//                        request.setAllowedNetworkTypes(
//                                DownloadManager.Request.NETWORK_WIFI
//                                        | DownloadManager.Request.NETWORK_MOBILE)
//                                .setAllowedOverRoaming(false).setTitle(r.getUsername())
////                        .setDescription(r.)
//                                .setDestinationInExternalPublicDir("/POS/USERS", String.valueOf(r.getUsername()) + ".jpg");
//
//                        mgr.enqueue(request);
//                    }
//                }
//            }


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
