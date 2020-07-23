package com.nerdvana.positiveoffline.view.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.model.RefreshMenuModel;

public class SystemTypeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private View view;

    private RadioGroup rgSystemType;
    private RadioGroup rgSystemMode;

    private TextView secondLabel;
    private View secondTopSeparator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_system_type, container, false);
        initViews(view);

        return view;
    }

    private void loadViews() {
        if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
            rgSystemType.setVisibility(View.VISIBLE);
            secondLabel.setVisibility(View.VISIBLE);
            secondTopSeparator.setVisibility(View.VISIBLE);
            rgSystemMode.check(R.id.rbCashier);

            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE))) {
                rgSystemMode.check(R.id.rbCashier);
            } else {
                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("cashier")) {
                    rgSystemMode.check(R.id.rbCashier);
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("to")) {
                    rgSystemMode.check(R.id.rbTakeOrder);
                }
            }


        } else {
            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                rgSystemType.setVisibility(View.VISIBLE);
                secondLabel.setVisibility(View.VISIBLE);
                secondTopSeparator.setVisibility(View.VISIBLE);


                if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE))) {
                    rgSystemMode.check(R.id.rbCashier);
                } else {
                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("cashier")) {
                        rgSystemMode.check(R.id.rbCashier);
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("to")) {
                        rgSystemMode.check(R.id.rbTakeOrder);
                    }
                }



            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                rgSystemType.setVisibility(View.VISIBLE);
                secondLabel.setVisibility(View.VISIBLE);
                secondTopSeparator.setVisibility(View.VISIBLE);
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                rgSystemType.setVisibility(View.VISIBLE);
                secondLabel.setVisibility(View.VISIBLE);
                secondTopSeparator.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initViews(View view) {
        secondLabel = view.findViewById(R.id.secondLabel);
        secondTopSeparator = view.findViewById(R.id.secondTopSeparator);
        rgSystemMode = view.findViewById(R.id.rgSystemMode);
        rgSystemType = view.findViewById(R.id.rgSystemType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRadioGroupSelection();
        loadViews();
        rgSystemMode.setOnCheckedChangeListener(this);
        rgSystemType.setOnCheckedChangeListener(this);
    }

    private void setRadioGroupSelection() {
        if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
            rgSystemType.check(R.id.rbQuickServe);

        } else {
            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                rgSystemType.check(R.id.rbQuickServe);
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                rgSystemType.check(R.id.rbMotel);
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                rgSystemType.check(R.id.rbRestaurant);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        SharedPreferenceManager.saveString(null, "1", AppConstants.HAS_CHANGED);
        switch (radioGroup.getId()) {
            case R.id.rgSystemType:

                if (radioGroup.getCheckedRadioButtonId() == R.id.rbQuickServe) {
                    SharedPreferenceManager.saveString(getContext(), "retail", AppConstants.TYPE_VALUE);
                    SharedPreferenceManager.saveString(getContext(), "QS",AppConstants.SELECTED_SYSTEM_TYPE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbMotel) {
                    SharedPreferenceManager.saveString(getContext(), "motel", AppConstants.TYPE_VALUE);
                    SharedPreferenceManager.saveString(getContext(), "hotel",AppConstants.SELECTED_SYSTEM_TYPE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbRestaurant) {
                    SharedPreferenceManager.saveString(getContext(), "restaurant", AppConstants.TYPE_VALUE);
                    SharedPreferenceManager.saveString(getContext(), "restaurant",AppConstants.SELECTED_SYSTEM_TYPE);
                }


                break;
            case R.id.rgSystemMode:
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbCashier) {
                    SharedPreferenceManager.saveString(getContext(), "cashier", AppConstants.SELECTED_SYSTEM_MODE);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbTakeOrder) {
                    SharedPreferenceManager.saveString(getContext(), "to", AppConstants.SELECTED_SYSTEM_MODE);
                }
                break;
        }

        BusProvider.getInstance().post(new RefreshMenuModel("test"));
    }
}
