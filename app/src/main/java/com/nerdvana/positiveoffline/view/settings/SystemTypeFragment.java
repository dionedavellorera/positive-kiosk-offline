package com.nerdvana.positiveoffline.view.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;

public class SystemTypeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private View view;

    private RadioGroup rgSystemType;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_system_type, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rgSystemType = view.findViewById(R.id.rgSystemType);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRadioGroupSelection();
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
        if (radioGroup.getCheckedRadioButtonId() == R.id.rbQuickServe) {
            SharedPreferenceManager.saveString(getContext(), "QS",AppConstants.SELECTED_SYSTEM_TYPE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbMotel) {
            SharedPreferenceManager.saveString(getContext(), "hotel",AppConstants.SELECTED_SYSTEM_TYPE);
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbRestaurant) {
            SharedPreferenceManager.saveString(getContext(), "restaurant",AppConstants.SELECTED_SYSTEM_TYPE);
        }
    }
}
