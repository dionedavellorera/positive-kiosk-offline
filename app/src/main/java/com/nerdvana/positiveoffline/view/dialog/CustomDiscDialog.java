package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.CustomSpinnerAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomDiscDialog extends BaseDialog {

    private DiscountViewModel discountViewModel;
    private SearchableSpinner spinnerDiscounts;
    public CustomDiscDialog(Context context, DiscountViewModel discountViewModel) {
        super(context);
        this.discountViewModel = discountViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_customdiscount, getContext().getString(R.string.header_custom));
        setCancelable(false);
        initViews();
        setCustomDiscountAdapter();
    }

    private void initViews() {
        spinnerDiscounts = findViewById(R.id.spinnerDiscounts);
    }

    private void setCustomDiscountAdapter() {

        List<String> customDiscountList = new ArrayList<>();

        try {
            for (DiscountWithSettings dws : discountViewModel.getCustomDiscountList()) {
                customDiscountList.add(dws.discounts.getDiscount_card());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.id.spinnerItem, customDiscountList);
        spinnerDiscounts.setAdapter(customSpinnerAdapter);
        customSpinnerAdapter.notifyDataSetChanged();
    }
}
