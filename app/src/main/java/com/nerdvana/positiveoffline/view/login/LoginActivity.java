package com.nerdvana.positiveoffline.view.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.ImageFilePath;
import com.nerdvana.positiveoffline.MainActivity;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.background.CheckPasswordAsync;
import com.nerdvana.positiveoffline.intf.PasswordCheckContract;
import com.nerdvana.positiveoffline.model.ErrorDataModel;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.view.ProgressButton;
import com.nerdvana.positiveoffline.view.dialog.SetupDialog;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PasswordCheckContract {

    private ProgressButton btnProceed;
    private ImageView setup;
    private HidingEditText username;
    private HidingEditText password;
    private ImageView logo;

    private SetupDialog setupDialog;
    private UserViewModel userViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private TransactionsViewModel transactionsViewModel;

    private ImageView ivBannerImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initViewModel();
        initDataSyncViewModel();
        initTransactionViewModel();
        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(LoginActivity.this, AppConstants.API_BASE_URL))) {
            PosClient.changeApiBaseUrl(SharedPreferenceManager.getString(LoginActivity.this, AppConstants.API_BASE_URL));
        }
        if (TextUtils.isEmpty(SharedPreferenceManager.getString(LoginActivity.this, AppConstants.MAX_COLUMN_COUNT))) {
            SharedPreferenceManager.saveString(LoginActivity.this, "32", AppConstants.MAX_COLUMN_COUNT);
        }

        initBackgroundChanger();
    }

    private void initBackgroundChanger() {
        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(LoginActivity.this, "saved_image"))) {

            Uri uri = Uri.parse(SharedPreferenceManager.getString(LoginActivity.this, "saved_image"));
            try {

                File direct = new File(SharedPreferenceManager.getString(LoginActivity.this, "saved_image"));
                Picasso.get().load(direct)
                        .fit()
                        .error(R.drawable.no_image)
                        .into(ivBannerImage);


            } catch (Exception e) {

            }


        }


        ivBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        LoginActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat
                            .requestPermissions(
                                    LoginActivity.this,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    1000);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2000);
                }
            }
        });
    }

    private void initTransactionViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initViews() {
        ivBannerImage = findViewById(R.id.ivBannerImage);
        logo = findViewById(R.id.logo);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnProceed = findViewById(R.id.proceed);
        btnProceed.setOnClickListener(this);
        setup = findViewById(R.id.setup);
        setup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setup:
                showSetupDialog();
                break;
            case R.id.proceed:
                if (!TextUtils.isEmpty(password.getText().toString()) &&
                        !TextUtils.isEmpty(username.getText().toString())) {
                    btnProceed.startLoading(view);
                    new CheckPasswordAsync(this,
                            username.getText().toString(),
                            password.getText().toString(),
                            userViewModel,
                            transactionsViewModel,
                            true,
                            false).execute();
                } else {
                    Helper.showDialogMessage(LoginActivity.this, getString(R.string.error_no_usernamepassword), getString(R.string.header_message));
                }

                break;
        }
    }

    private void showSetupDialog() {
        if (setupDialog == null) {
            setupDialog = new SetupDialog(LoginActivity.this) {
                @Override
                public void verifySuccess() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage("Machine successfully verified")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    if (!alert.isShowing()) {
                        alert.show();
                    }

                    alert.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            startSyncActivity(AppConstants.VERIFY_MACHINE);
                        }
                    });


                }
            };
            setupDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    setupDialog = null;
                }
            });
            setupDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    setupDialog = null;
                }
            });
            setupDialog.show();
        }
    }


    @Override
    public void isValid(String valid) {
        btnProceed.stopLoading(btnProceed);
        if (valid.equalsIgnoreCase("success")) {
            startSyncActivity(AppConstants.ACTIVITY_LOGIN);
            finish();
        } else {
            Helper.showDialogMessage(LoginActivity.this, valid, getString(R.string.header_message));
        }
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void startSyncActivity(String origin) {
        Intent syncIntent = new Intent(LoginActivity.this, SyncActivity.class);
        syncIntent.putExtra(AppConstants.ORIGIN, origin);
        startActivity(syncIntent);
    }

    private void startMainActivity() {
        Intent syncIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(syncIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkValidSession();
        isStoragePermissionGranted();
    }

    private void checkValidSession() {
        try {
            if (userViewModel.searchLoggedInUser().size() > 0) {
                if (errorDataModel().isValid()) {
                    startMainActivity();
                } else {
                    startSyncActivity(AppConstants.VERIFY_MACHINE);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private ErrorDataModel errorDataModel() {
        boolean isValid = true;
        String errorMessage = "Success";
        try {
            if (dataSyncViewModel.getUnsyncedData().size() > 0) {
                isValid = false;
                errorMessage = getApplicationContext().getString(R.string.error_message_data_not_sync);
            }

        } catch (ExecutionException e) {
            isValid = false;
            errorMessage = e.getLocalizedMessage();
        } catch (InterruptedException e) {
            isValid = false;
            errorMessage = e.getLocalizedMessage();
        }

        return new ErrorDataModel(errorMessage, isValid);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();


                String realPath = ImageFilePath.getPath(LoginActivity.this, data.getData());
//                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                SharedPreferenceManager.saveString(LoginActivity.this, realPath, "saved_image");


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    // Log.d(TAG, String.valueOf(bitmap));


                    Picasso.get().load(uri)
                            .fit()
                            .error(R.drawable.no_image)
                            .into(ivBannerImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            //TODO: action
        }
    }

}
