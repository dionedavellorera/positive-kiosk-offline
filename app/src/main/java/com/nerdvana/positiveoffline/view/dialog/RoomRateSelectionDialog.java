package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.CustomSpinnerAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public abstract class RoomRateSelectionDialog extends BaseDialog implements View.OnClickListener{

    private List<RoomRates> roomRatesList;
    private SearchableSpinner spinnerRoomRate;

    private RoomRates selectedRoomRate = null;
    private Button btnConfirm;
    public RoomRateSelectionDialog(Context context, List<RoomRates> roomRatesList) {
        super(context);
        this.roomRatesList = roomRatesList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_room_rate_selection, "RATES");
        initViews();

        setRoomRateSpinner();

    }

    private void setRoomRateSpinner() {
        final List<String> roomRateList = new ArrayList<>();
        for (RoomRates rrm : roomRatesList) {
            roomRateList.add(String.format("%s - %s", rrm.getRoom_rate_description(), String.valueOf(rrm.getAmount())));
        }
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.id.spinnerItem,
                roomRateList);
        spinnerRoomRate.setAdapter(customSpinnerAdapter);

        spinnerRoomRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoomRate = roomRatesList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initViews() {
        spinnerRoomRate = findViewById(R.id.spinnerRoomRate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (selectedRoomRate != null) {
                    rateSelected(selectedRoomRate);
                } else {
                    Helper.showDialogMessage(getContext(),"No room rate selected", getContext().getString(R.string.header_message));
                }
                dismiss();
                break;
        }
    }

    public abstract void rateSelected(RoomRates roomRates);
}
