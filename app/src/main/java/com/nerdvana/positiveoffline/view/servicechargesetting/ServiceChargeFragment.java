package com.nerdvana.positiveoffline.view.servicechargesetting;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.ServiceChargeAdapter;
import com.nerdvana.positiveoffline.entities.ServiceCharge;
import com.nerdvana.positiveoffline.intf.ServiceChargeContract;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.ServiceChargeViewModel;

import java.util.concurrent.ExecutionException;

public class ServiceChargeFragment extends Fragment implements View.OnClickListener, ServiceChargeContract {

    private View view;
    private ServiceChargeViewModel serviceChargeViewModel;
    private RadioGroup rgType;
    private HidingEditText etValue;
    private Button btnConfirm;
    private RecyclerView rvServiceCharge;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_charge, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rvServiceCharge = view.findViewById(R.id.rvServiceCharge);
        rgType = view.findViewById(R.id.rgType);
        etValue = view.findViewById(R.id.etValue);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initServiceChargeViewModel();

        setAdapter();
    }

    private void initServiceChargeViewModel() {
        serviceChargeViewModel = new ViewModelProvider(this).get(ServiceChargeViewModel.class);
    }

    private void setAdapter() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ServiceChargeAdapter serviceChargeAdapter =
                            new ServiceChargeAdapter(serviceChargeViewModel.getServiceChargeList(), getContext(), ServiceChargeFragment.this);
                    rvServiceCharge.setAdapter(serviceChargeAdapter);
                    rvServiceCharge.setLayoutManager(new LinearLayoutManager(getContext()));
                    serviceChargeAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }

            }
        }, 500);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!TextUtils.isEmpty(etValue.getText().toString())) {
                    if (Double.valueOf(etValue.getText().toString()) >= 0) {


                        if (rgType.getCheckedRadioButtonId() != -1) {
                            if (rgType.getCheckedRadioButtonId() == R.id.rdPercentage) {
                                ServiceCharge serviceCharge = new ServiceCharge(Double.valueOf(etValue.getText().toString()), true,
                                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)));
                                serviceChargeViewModel.insertServiceChargeSetting(serviceCharge);
                                Helper.showDialogMessage(getContext(), "Service Charge Added", getContext().getString(R.string.text_header_error));
                                etValue.setText("");
                            } else {
                                ServiceCharge serviceCharge = new ServiceCharge(Double.valueOf(etValue.getText().toString()), false,
                                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)));
                                serviceChargeViewModel.insertServiceChargeSetting(serviceCharge);
                                Helper.showDialogMessage(getContext(), "Service Charge Added", getContext().getString(R.string.text_header_error));
                                etValue.setText("");
                            }
                        } else {
                            Helper.showDialogMessage(getContext(), "Please select value type", getContext().getString(R.string.text_header_error));
                        }

                        setAdapter();




                    } else {
                        Helper.showDialogMessage(getContext(), "Value cannot be less than 0", getContext().getString(R.string.text_header_error));
                    }
                } else {
                    Helper.showDialogMessage(getContext(), "Please enter value for service charge", getContext().getString(R.string.text_header_error));
                }
                break;
        }
    }


    @Override
    public void clicked(ServiceCharge serviceCharge, int position) {
        serviceChargeViewModel.updateServiceChargeSetting(serviceCharge);
        try {
            for (ServiceCharge sc : serviceChargeViewModel.getServiceChargeList()) {
                if (sc.getId() == serviceCharge.getId()) {
                    sc.setIs_selected(true);
                } else {
                    sc.setIs_selected(false);
                }
                serviceChargeViewModel.updateServiceChargeSetting(sc);
            }

            setAdapter();
        } catch (Exception e) {

        }
    }
}
