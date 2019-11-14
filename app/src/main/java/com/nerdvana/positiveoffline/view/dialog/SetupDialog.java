package com.nerdvana.positiveoffline.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.IUsers;
import com.nerdvana.positiveoffline.PosClient;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.apirequests.TestRequest;
import com.nerdvana.positiveoffline.apirequests.VerifyMachineRequest;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.apiresponses.VerifyMachineResponse;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.view.HidingEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SetupDialog extends BaseDialog implements View.OnClickListener{

    //region views
    private Button btnConfirm;
    private ProgressDialog progressDialog;
    private HidingEditText etHostName;
    private HidingEditText etCompany;
    private HidingEditText etCode;
    private HidingEditText etProductKey;
    //endregion

    public SetupDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_setup, getContext().getString(R.string.app_setup));
        setCancelable(false);

        initViews();

        fillupFields();
    }

    private void fillupFields() {
        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(getContext(), AppConstants.HOST))) {
            etHostName.setText(SharedPreferenceManager.getString(getContext(), AppConstants.HOST));
            etCompany.setText(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH));
            etCode.setText(SharedPreferenceManager.getString(getContext(), AppConstants.CODE));
            etProductKey.setText(SharedPreferenceManager.getString(getContext(), AppConstants.SERIAL_NUMBER));
        }
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getContext().getString(R.string.general_progress_text));

        etHostName = findViewById(R.id.etHostName);
        etCompany = findViewById(R.id.etCompany);
        etCode = findViewById(R.id.etCode);
        etProductKey = findViewById(R.id.etProductKey);
    }

    private boolean isAllFieldsOk() {
        boolean isValid = false;
        if (!TextUtils.isEmpty(etHostName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etCompany.getText().toString().trim()) &&
                !TextUtils.isEmpty(etCode.getText().toString().trim()) &&
                !TextUtils.isEmpty(etProductKey.getText().toString().trim())) {
            isValid = true;
        }

        return isValid;
    }
    
    private void savePreference() {
        SharedPreferenceManager.saveString(getContext(),
                etHostName.getText().toString(),AppConstants.HOST);
        SharedPreferenceManager.saveString(getContext(),
                etCompany.getText().toString(),AppConstants.BRANCH);
        SharedPreferenceManager.saveString(getContext(),
                etCode.getText().toString(),AppConstants.CODE);
        SharedPreferenceManager.saveString(getContext(),
                etProductKey.getText().toString(), AppConstants.SERIAL_NUMBER);
    }

    private void testConnection() {

        if (isAllFieldsOk()) {

            if (URLUtil.isValidUrl(String.format("%s/%s/%s/%s/",
                    etHostName.getText().toString(),
                    "api",
                    etCompany.getText().toString(),
                    etCode.getText().toString()))) {
                savePreference();

                String apiBaseUrl = String.format("%s/%s/%s/%s/",
                        etHostName.getText().toString(),
                        "api",
                        etCompany.getText().toString(),
                        etCode.getText().toString());
                SharedPreferenceManager.saveString(getContext(), apiBaseUrl, AppConstants.API_BASE_URL);
                PosClient.changeApiBaseUrl(
                        apiBaseUrl
                );
                progressDialog.show();
                IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
                TestRequest collectionRequest = new TestRequest();
                Call<TestResponse> request = iUsers.sendTestRequest(collectionRequest.getMapValue());
                request.enqueue(new Callback<TestResponse>() {
                    @Override
                    public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                        if (response.body().getStatus().equalsIgnoreCase("1")) {
                            sendVerifyMachineRequest(etProductKey.getText().toString().toUpperCase());

                        } else {
                            progressDialog.dismiss();
                            Helper.showDialogMessage(getContext(), response.body().getMessage(), getContext().getString(R.string.text_header_error));
                        }

                    }

                    @Override
                    public void onFailure(Call<TestResponse> call, Throwable t) {
                        Helper.showDialogMessage(getContext(), t.getMessage(), getContext().getString(R.string.text_header_error));
                        progressDialog.dismiss();
                    }
                });
            } else {
                Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_invalid_url), getContext().getString(R.string.text_header_error));
            }
        } else {
            Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.text_header_error));
        }


    }

    public abstract void verifySuccess();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                testConnection();
                break;
        }
    }

    private void sendVerifyMachineRequest(String productKey) {
        VerifyMachineRequest request = new VerifyMachineRequest(
                productKey,
                Build.ID,
                Build.SERIAL,
                Build.MODEL,
                Build.MANUFACTURER,
                Build.BOARD
        );
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        Call<VerifyMachineResponse> verifyMachineRequest = iUsers.sendVerifyMachineRequest(
                request.getMapValue());

        verifyMachineRequest.enqueue(new Callback<VerifyMachineResponse>() {
            @Override
            public void onResponse(Call<VerifyMachineResponse> call, Response<VerifyMachineResponse> response) {
                progressDialog.dismiss();
                if (response.body().getStatus() == 1) { //success
                    SharedPreferenceManager.saveString(getContext(), "40", AppConstants.MAX_COLUMN_COUNT);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getResult().get(0).getPrinter_path()), AppConstants.SELECTED_PORT);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getResult().get(0).getId()), AppConstants.MACHINE_ID);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getCompany().get(0).getCompany()), AppConstants.BUSINESS_NAME);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getCompany().get(0).getOwner()), AppConstants.TAXPAYERS_NAME);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getInfo().getTinNo()), AppConstants.TIN_NUMBER);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getAddress()), AppConstants.BRANCH_ADDRESS);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getInfo().getRemarks()), AppConstants.OR_INFO_DISPLAY);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getInfo().getTax()), AppConstants.TAX_RATE);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getId()), AppConstants.BRANCH_ID);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getBranchCode()), AppConstants.BRANCH_CODE);
                    SharedPreferenceManager.saveString(getContext(), String.valueOf(response.body().getBranch().getInfo().getSafe_keeping_amount()), AppConstants.SAFEKEEPING_AMOUNT);
                    SharedPreferenceManager.saveString(getContext(), GsonHelper.getGson().toJson(String.valueOf(response.body().getBranch().getShift())), AppConstants.SHIFT_DETAILS);
                    verifySuccess();
                    dismiss();
                } else {
                    Helper.showDialogMessage(getContext(), response.body().getMesage(), getContext().getString(R.string.text_header_error));

                }
            }

            @Override
            public void onFailure(Call<VerifyMachineResponse> call, Throwable t) {
                progressDialog.dismiss();
                Helper.showDialogMessage(getContext(), t.getLocalizedMessage(), getContext().getString(R.string.text_header_error));
            }
        });


//        BusProvider.getInstance().post(request);
    }
}
