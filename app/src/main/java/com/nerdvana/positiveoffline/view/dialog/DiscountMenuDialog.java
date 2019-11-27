package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.DiscountMenuAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.intf.DiscountsContract;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscountMenuDialog extends BaseDialog implements DiscountsContract {

    private DiscountViewModel discountViewModel;
    private RecyclerView rvDiscountMenu;
    private SpecialDiscDialog specialDiscDialog;
    private CustomDiscDialog customDiscDialog;
    private TransactionsViewModel transactionsViewModel;
    private String transactionId;
    public DiscountMenuDialog(Context context, DiscountViewModel discountViewModel,
                              TransactionsViewModel transactionsViewModel, String transactionId) {
        super(context);
        this.discountViewModel = discountViewModel;
        this.transactionsViewModel = transactionsViewModel;
        this.transactionId = transactionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_discount_selection, getContext().getString(R.string.header_discount_selection));
        setCancelable(false);
        initViews();

        try {

            DiscountMenuAdapter discountMenuAdapter = new DiscountMenuAdapter(discountViewModel.getDiscountMenuList(), getContext(), DiscountMenuDialog.this);
            rvDiscountMenu.setAdapter(discountMenuAdapter);
            rvDiscountMenu.setLayoutManager(new LinearLayoutManager(getContext()));
            discountMenuAdapter.notifyDataSetChanged();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        rvDiscountMenu = findViewById(R.id.rvDiscountMenu);
    }

    @Override
    public void clicked(DiscountWithSettings discountWithSettings) {
        if (discountWithSettings.discounts.getCore_id() == 1000) { //SHOW MANUAL DIALOG

        } else if (discountWithSettings.discounts.getCore_id() == 1001) { //SHOW CUSTOM DIALOG
            if (customDiscDialog == null) {
                customDiscDialog = new CustomDiscDialog(getContext(), discountViewModel);
                customDiscDialog.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        customDiscDialog = null;
                    }
                });

                customDiscDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        customDiscDialog = null;
                    }
                });
                customDiscDialog.show();
            }
        } else { //SHOW SENIOR / PWD DIALOG
            if (specialDiscDialog == null) {
                try {
                    specialDiscDialog = new SpecialDiscDialog(
                            getContext(),
                            discountWithSettings.discounts.getDiscount_card(),
                            discountWithSettings,
                            transactionsViewModel.orderList(transactionId),
                            transactionsViewModel,
                            discountViewModel,
                            transactionId);

                    specialDiscDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            specialDiscDialog = null;
                        }
                    });
                    specialDiscDialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            specialDiscDialog = null;
                        }
                    });
                    specialDiscDialog.show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    }
}
