package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.DiscountMenuAdapter;
import com.nerdvana.positiveoffline.adapter.DiscountsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.intf.DiscountsContract;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscountMenuDialog extends BaseDialog implements DiscountsContract {

    private DiscountViewModel discountViewModel;
    private RecyclerView rvDiscountMenu;
    private RecyclerView rvPostedDiscounts;
    private SpecialDiscDialog specialDiscDialog;
    private CustomDiscDialog customDiscDialog;
    private ManualDiscDialog manualDiscDialog;
    private TransactionsViewModel transactionsViewModel;
    private String transactionId;
    private TextView noData;
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
        loadDiscounts();
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

    private void loadDiscounts() {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {
                        List<TransactionWithDiscounts> twd = discountViewModel.getTransactionWithDiscounts(transactionId);

                        if (twd.size()< 1) {
                            showNoData();
                        } else {
                            setDiscountAdapter(twd);
                            hideNoData();
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }





                }
            }, 500);




    }

    private void initViews() {
        rvDiscountMenu = findViewById(R.id.rvDiscountMenu);
        rvPostedDiscounts = findViewById(R.id.rvPostedDiscounts);
        noData = findViewById(R.id.noData);
    }

    private void hideNoData() {
        noData.setVisibility(View.GONE);
        rvPostedDiscounts.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        noData.setVisibility(View.VISIBLE);
        rvPostedDiscounts.setVisibility(View.GONE);
    }

    @Override
    public void clicked(DiscountWithSettings discountWithSettings) throws ExecutionException, InterruptedException {
        if (discountWithSettings.discounts.getCore_id() == 1000) { //SHOW MANUAL DIALOG
            if (manualDiscDialog == null) {
                try {
                    manualDiscDialog = new ManualDiscDialog(getContext(), discountViewModel,
                            transactionsViewModel.orderList(transactionId),
                            transactionId,
                            transactionsViewModel) {
                        @Override
                        public void discountSuccess() {
                            loadDiscounts();
                        }
                    };
                    manualDiscDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            manualDiscDialog = null;
                        }
                    });

                    manualDiscDialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            manualDiscDialog = null;
                        }
                    });
                    if (discountViewModel.getTransactionWithDiscounts(transactionId).size() > 0) {
                        Helper.showDialogMessage(getContext(), "Only one discount can be applied per transaction", getContext().getString(R.string.header_message));
                    } else {
                        manualDiscDialog.show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        } else if (discountWithSettings.discounts.getCore_id() == 1001) { //SHOW CUSTOM DIALOG
            if (customDiscDialog == null) {
                customDiscDialog = new CustomDiscDialog(
                        getContext(),
                        discountViewModel,
                        transactionsViewModel.orderList(transactionId),
                        transactionsViewModel,
                        transactionId) {
                    @Override
                    public void discountSuccess() {
                        loadDiscounts();
                    }
                };
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
                if (discountViewModel.getTransactionWithDiscounts(transactionId).size() > 0) {
                    Helper.showDialogMessage(getContext(), "Only one discount can be applied per transaction", getContext().getString(R.string.header_message));
                } else {
                    customDiscDialog.show();
                }
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
                            transactionId) {
                        @Override
                        public void seniorSucceeded() {
                            loadDiscounts();
                        }
                    };

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
                    if (discountViewModel.getTransactionWithDiscounts(transactionId).size() > 0) {
                        Helper.showDialogMessage(getContext(), "Only one discount can be applied per transaction", getContext().getString(R.string.header_message));
                    } else {
                        specialDiscDialog.show();
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    @Override
    public void clicked(TransactionWithDiscounts transactionWithDiscounts) {
        //this is void discount
        try {
            PostedDiscounts tmpPd = discountViewModel.getPostedDiscount(transactionWithDiscounts.getPosted_discount_id());
            PostedDiscounts postedDiscounts = new PostedDiscounts(
                    Integer.valueOf(transactionWithDiscounts.getTransaction_id()),
                    tmpPd.getDiscount_id(),
                    tmpPd.getDiscount_name(),
                    true,
                    tmpPd.getCard_number(),
                    tmpPd.getName(),
                    tmpPd.getAddress(),
                    tmpPd.getIs_percentage(),
                    tmpPd.getDiscount_value(),
                    0,
                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                    Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                    Utils.getDateTimeToday()
            );
            postedDiscounts.setId(transactionWithDiscounts.getPosted_discount_id());

            discountViewModel.updatePostedDiscount(postedDiscounts);

            for (OrderDiscounts od : discountViewModel.getDiscountList(transactionWithDiscounts.getPosted_discount_id())) {
                od.setIs_void(true);
                discountViewModel.updateOrderDiscount(od);
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    transactionsViewModel.recomputeTransactionWithDiscount(transactionId, discountViewModel);

                }
            }, 700);

            loadDiscounts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setDiscountAdapter(List<TransactionWithDiscounts> orderDiscountsList) {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        DiscountsAdapter discountsAdapter = new DiscountsAdapter(orderDiscountsList, getContext(), this);
        rvPostedDiscounts.setAdapter(discountsAdapter);
        rvPostedDiscounts.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                llm.getOrientation());
        rvPostedDiscounts.addItemDecoration(dividerItemDecoration);

        discountsAdapter.notifyDataSetChanged();
    }
}
