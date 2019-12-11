package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.ZReadAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.intf.ZReadContract;

import java.util.List;

public class ReprintZReadDialog extends BaseDialog implements ZReadContract {

    private RecyclerView rvList;
    private List<EndOfDay> endOfDayList;
    public ReprintZReadDialog(Context context, List<EndOfDay> endOfDayList) {
        super(context);
        this.endOfDayList = endOfDayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_reprint_zread, "Z READ DATA");
        setCancelable(false);
        initViews();
        setAdapter();
    }

    private void setAdapter() {
        ZReadAdapter zReadAdapter = new ZReadAdapter(endOfDayList, this, getContext());
        rvList.setAdapter(zReadAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        zReadAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        rvList = findViewById(R.id.rvList);
    }

    @Override
    public void clicked(EndOfDay endOfDay) {

    }
}
