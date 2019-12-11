package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.XReadAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.intf.XReadContract;

import java.util.List;

public class ReprintXReadDialog extends BaseDialog implements XReadContract {

    private RecyclerView rvList;
    private List<CutOff> cutOffList;
    public ReprintXReadDialog(Context context, List<CutOff> cutOffList) {
        super(context);
        this.cutOffList = cutOffList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_reprint_xread, "X READ DATA");
        setCancelable(false);
        initViews();
        setAdapter();
    }

    private void setAdapter() {
        XReadAdapter xReadAdapter = new XReadAdapter(cutOffList, this, getContext());
        rvList.setAdapter(xReadAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        xReadAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        rvList = findViewById(R.id.rvList);
    }

    @Override
    public void clicked(CutOff cutOff) {

    }
}
