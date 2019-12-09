package com.nerdvana.positiveoffline.view.printersettings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;

public class ReceiptSetupFragment extends Fragment {
    private View view;

    private SeekBar seekbarProgress;
    private TextView seekbarValue;

    public ReceiptSetupFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_receipt_setup, container, false);

        seekbarProgress = view.findViewById(R.id.seekbarProgress);
        seekbarValue = view.findViewById(R.id.seekbarValue);


        seekbarValue.setText(SharedPreferenceManager.getString(getContext(), AppConstants.MAX_COLUMN_COUNT));

        seekbarProgress.setProgress(Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MAX_COLUMN_COUNT)));
        seekbarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferenceManager.saveString(getContext(), String.valueOf(seekBar.getProgress()), AppConstants.MAX_COLUMN_COUNT);
            }
        });

        return view;
    }



}
