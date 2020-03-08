package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.AlacartAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.ProductAlacart;

import java.util.List;

public abstract class AlacartCompositionDialog extends BaseDialog implements View.OnClickListener {

    private List<ProductAlacart> productAlacartList;
    private RecyclerView rvAlacart;
    private Button btnConfirm;
    public AlacartCompositionDialog(Context context, List<ProductAlacart> productAlacartList) {
        super(context);
        this.productAlacartList = productAlacartList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_alacart_composition, "ALACART COMPOSITION");
        setCancelable(false);
        initViews();
        setAlacartAdapter();
    }

    private void initViews() {
        rvAlacart = findViewById(R.id.rvAlacart);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    private void setAlacartAdapter() {
        AlacartAdapter alacartAdapter = new AlacartAdapter(productAlacartList, getContext());
        rvAlacart.setAdapter(alacartAdapter);
        rvAlacart.setLayoutManager(new GridLayoutManager(getContext(), 4));
        alacartAdapter.notifyDataSetChanged();
    }

    public abstract void confirmed();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                confirmed();
                dismiss();
                break;
        }
    }
}
