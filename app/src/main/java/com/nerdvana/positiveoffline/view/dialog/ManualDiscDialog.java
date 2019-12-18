package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;

public class ManualDiscDialog extends BaseDialog {

    private DiscountViewModel discountViewModel;
    private List<Orders> orderList;
    private String transactionId;
    private TransactionsViewModel transactionsViewModel;
    public ManualDiscDialog(Context context, DiscountViewModel discountViewModel,
                            List<Orders> orderList, String transactionId,
                            TransactionsViewModel transactionsViewModel) {
        super(context);
        this.discountViewModel = discountViewModel;
        this.orderList = orderList;
        this.transactionId = transactionId;
        this.transactionsViewModel = transactionsViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_manual_discount, "");
//        discountViewModel.insertManualDiscount(orderList, transactionId,
//                1000, "MANUAL",
//                true, 1.00);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);
//            }
//        }, 500);
    }
}
