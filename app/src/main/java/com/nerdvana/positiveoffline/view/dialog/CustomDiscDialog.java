package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.CustomSpinnerAdapter;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.SpecialDiscountInfo;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class CustomDiscDialog extends BaseDialog implements View.OnClickListener{

    private DiscountViewModel discountViewModel;
    private SearchableSpinner spinnerDiscounts;

    private HidingEditText etCardNumber;
    private HidingEditText etName;
    private HidingEditText etAddress;

    List<DiscountWithSettings> dwsList = new ArrayList<>();
    List<String> customDiscountList = new ArrayList<>();

    private Button btnConfirm;

    private List<Orders> ordersList;
    private TransactionsViewModel transactionsViewModel;
    private String transactionId;
    public CustomDiscDialog(Context context, DiscountViewModel discountViewModel,
                            List<Orders> orderList,
                            TransactionsViewModel transactionsViewModel,
                            String transactionId) {
        super(context);
        this.discountViewModel = discountViewModel;
        this.ordersList = orderList;
        this.transactionsViewModel = transactionsViewModel;
        this.transactionId = transactionId;
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
        etCardNumber = findViewById(R.id.etCardNumber);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        spinnerDiscounts = findViewById(R.id.spinnerDiscounts);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
    }

    private void setCustomDiscountAdapter() {
        customDiscountList = new ArrayList<>();
        try {
            for (DiscountWithSettings dws : discountViewModel.getCustomDiscountList()) {
                customDiscountList.add(dws.discounts.getDiscount_card());
                dwsList.add(dws);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:
                if (!TextUtils.isEmpty(etCardNumber.getText().toString())) {

                    SpecialDiscountInfo specialDiscountInfo  =
                            new SpecialDiscountInfo(etCardNumber.getText().toString(),
                                    etName.getText().toString(),
                                    etAddress.getText().toString());

                    discountViewModel.insertDiscount(ordersList, dwsList.get(spinnerDiscounts.getSelectedItemPosition()), transactionId, specialDiscountInfo);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
                        }
                    }, 500);


                    discountSuccess();
                    dismiss();
                } else {
                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_card_number_required), getContext().getString(R.string.header_message));
                }
                break;
        }
    }

    public abstract void discountSuccess();
}
