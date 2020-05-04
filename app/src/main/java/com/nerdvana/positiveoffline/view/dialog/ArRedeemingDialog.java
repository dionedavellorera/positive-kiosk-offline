package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.RedeemPaymentsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArRedeemingDialog extends BaseDialog implements PaymentsContract {
    private RecyclerView rvArRedeemList;
    private TransactionsViewModel transactionsViewModel;
    private UserViewModel userViewModel;
    private TextView noData;
    private Context context;
    public ArRedeemingDialog(Context context, TransactionsViewModel transactionsViewModel,
                             UserViewModel userViewModel) {
        super(context);
        this.context = context;
        this.transactionsViewModel = transactionsViewModel;
        this.userViewModel = userViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_ar_redeeming, "AR Redeeming");
        setCancelable(false);
        initViews();
        try {
            initPaymentRedeemListener();
        } catch (Exception e) {

        }
    }

    private void initPaymentRedeemListener() throws ExecutionException, InterruptedException {
        transactionsViewModel.ldUnredeemedPaymentList().observe((LifecycleOwner) context, new Observer<List<Payments>>() {
            @Override
            public void onChanged(List<Payments> paymentsList) {
                loadPayments(paymentsList);
            }
        });
    }

    private void loadPayments(List<Payments> paymentsList) {
        if (paymentsList.size() > 0) {
            noData.setVisibility(View.GONE);
            rvArRedeemList.setVisibility(View.VISIBLE);
            RedeemPaymentsAdapter redeemPaymentsAdapter = new RedeemPaymentsAdapter(paymentsList, getContext(), ArRedeemingDialog.this);
            rvArRedeemList.setAdapter(redeemPaymentsAdapter);
            rvArRedeemList.setLayoutManager(new LinearLayoutManager(getContext()));
            redeemPaymentsAdapter.notifyDataSetChanged();
        } else {
            noData.setVisibility(View.VISIBLE);
            rvArRedeemList.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        rvArRedeemList = findViewById(R.id.rvArRedeemList);
        noData = findViewById(R.id.noData);
    }

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }

    @Override
    public void clicked(Payments payments) {

    }

    @Override
    public void clicked(final Payments payments, View view) {

        PopupMenu popup = new PopupMenu(getContext(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_redeem_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cash:
                        try {
                            payments.setIs_redeemed(1);
                            payments.setIs_redeemed_by(getUser().getUsername());
                            payments.setIs_redeemed_at(Utils.getDateTimeToday());
                            payments.setIs_redeemed_for("CASH");
                            transactionsViewModel.updatePayment(payments);
                        } catch (Exception e) {

                        }
                        break;
                    case R.id.card:
                        try {
                            payments.setIs_redeemed(1);
                            payments.setIs_redeemed_by(getUser().getUsername());
                            payments.setIs_redeemed_at(Utils.getDateTimeToday());
                            payments.setIs_redeemed_for("CARD");
                            transactionsViewModel.updatePayment(payments);
                        } catch (Exception e) {

                        }
                        break;
                }
                return true;
            }
        });

        popup.show();



    }
}
