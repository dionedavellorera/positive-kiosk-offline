package com.nerdvana.positiveoffline.background;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.PasswordCheckContract;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CheckPasswordAsync extends AsyncTask<Void, Void, String> {
    private PasswordCheckContract passwordCheckContract;
    private String username;
    private String password;
    private UserViewModel userViewModel;
    private TransactionsViewModel transactionsViewModel;
    private Boolean fromLogin;

    public CheckPasswordAsync(PasswordCheckContract passwordCheckContract, String username,
                              String password, UserViewModel userViewModel,
                              TransactionsViewModel transactionsViewModel, boolean fromLogin) {
        this.passwordCheckContract = passwordCheckContract;
        this.username = username;
        this.password = password;
        this.userViewModel = userViewModel;
        this.transactionsViewModel = transactionsViewModel;
        this.fromLogin = fromLogin;
    }

//    public CheckPasswordAsync(PasswordCheckContract passwordCheckContract, String username,
//                              String password, UserViewModel userViewModel,
//                              boolean tickLoggedIn, TransactionsViewModel transactionsViewModel) {
//        this.passwordCheckContract = passwordCheckContract;
//        this.username = username;
//        this.password = password;
//        this.userViewModel = userViewModel;
//        this.tickLoggedIn = tickLoggedIn;
//    }



    @Override
    protected String doInBackground(Void... voids) {
        String message = "";
        List<User> user = userViewModel.isExisting(username);
        if (user.size() < 1) {
            message = "No account found";
        } else {
            if (!BCrypt.verifyer().verify(password.toCharArray(), user.get(0).getPassword()).verified) {
                message = "Incorrect Credentials";
            } else {
                try {
                    if (transactionsViewModel.unCutOffTransactions().size() > 0) {
                        if (!transactionsViewModel.unCutOffTransactions().get(0).getIs_completed_by().equalsIgnoreCase(user.get(0).getUsername())) {
                            message = String.format("Please cutoff %s id transaction/s first", transactionsViewModel.unCutOffTransactions().get(0).getIs_completed_by());
                        }
                    }
                } catch (ExecutionException e) {
                    message = e.getMessage();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    message = e.getMessage();
                    e.printStackTrace();
                }
            }
        }

        if (TextUtils.isEmpty(message)) { //means login success
            if (fromLogin) {
                user.get(0).setIs_logged_in(true);
                userViewModel.update(user.get(0));
            }

        }
        //lookup sa database
//        String hashedPassword = "$2y$10$etvCPgv6BbKB0pAJXRCB5uZOauNzY91EOtHNZJlN5KCXgzsZnkjr6";
//        BCrypt.Result result2 = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return message;
//        return result2.verified;
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        if (TextUtils.isEmpty(str)) {
            passwordCheckContract.isValid("Success");
        } else {
            passwordCheckContract.isValid(str);
        }
//        passwordCheckContract.isValid(aBoolean);
    }
}
