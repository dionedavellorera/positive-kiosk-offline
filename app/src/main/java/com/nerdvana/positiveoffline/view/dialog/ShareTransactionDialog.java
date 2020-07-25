package com.nerdvana.positiveoffline.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.ShareTransactionProductAdapter;
import com.nerdvana.positiveoffline.adapter.SharedTransactionPaymentsAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.SharedTransactionsContract;
import com.nerdvana.positiveoffline.intf.StOrderContract;
import com.nerdvana.positiveoffline.model.StPaymentsModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class ShareTransactionDialog extends BaseDialog implements View.OnClickListener,
        SharedTransactionsContract, StOrderContract {
    private int customerCounter = 0;
    private FloatingActionButton fabCheckout;

    private List<Orders> ordersList;
    private RecyclerView rvOrders;
    private RecyclerView rvPayments;
    private Button btnAddCustomer;
    private SharedTransactionPaymentsAdapter stPaymentsAdapter;
    private ShareTransactionProductAdapter stProdAdapter;
    private List<StPaymentsModel> stPaymentsList;
    private int orderCount = 0;

    private int selectedCustomerPosition = 999;

    private DataSyncViewModel dataSyncViewModel;
    private UserViewModel userViewModel;
    public ShareTransactionDialog(Context context, List<Orders> ordersList,
                                  int orderCount, DataSyncViewModel dataSyncViewModel,
                                  UserViewModel userViewModel) {
        super(context);
        this.userViewModel = userViewModel;
        this.ordersList = ordersList;
        this.orderCount = orderCount;
        this.dataSyncViewModel = dataSyncViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_share_transaction, "Share Transaction");
        initViews();

        setProductAdapter();
        setPaymentsAdapter();
    }

    private void setPaymentsAdapter() {
        stPaymentsAdapter = new SharedTransactionPaymentsAdapter(stPaymentsList, getContext(), this);
        rvPayments.setAdapter(stPaymentsAdapter);
        rvPayments.setLayoutManager(new LinearLayoutManager(getContext()));
        stPaymentsAdapter.notifyDataSetChanged();
    }

    private void setProductAdapter() {
        stProdAdapter = new ShareTransactionProductAdapter(ordersList, getContext(),this);
        rvOrders.setAdapter(stProdAdapter);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        stProdAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        rvPayments = findViewById(R.id.rvPayments);
        stPaymentsList = new ArrayList<>();
        rvOrders = findViewById(R.id.rvOrders);
        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnAddCustomer.setOnClickListener(this);
        fabCheckout = findViewById(R.id.fabCheckout);
        fabCheckout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dialog dialog = this;
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabCheckout:
                if (stPaymentsList.size() < 2) {
                    Helper.showDialogMessage(getContext(), "Payments should be shared by 2 or more persons", "Information");
                } else {
                    //validate if all persons paid right amount
                    int mOrderCount = 0;
                    double mTotal = 0.00;
                    double mPayment = 0.00;
                    int loopCount = 0;
                    //orderCount
                    for (StPaymentsModel stmodel : stPaymentsList) {
                        Double total = 0.00;
                        Double totalPayment = 0.00;
                        boolean hasOrder = false;
                        for (Orders ord : stmodel.getOrdersList()) {
                            mOrderCount += ord.getQty();
                            hasOrder = true;
                            total += ord.getAmount() * ord.getQty();
                        }

                        if (hasOrder) {
                            loopCount += 1;
                        }

                        for (Payments pym : stmodel.getPaymentsList()) {
                            totalPayment += pym.getAmount();
                        }

                        mTotal += total;
                        mPayment += totalPayment;


                    }

                    if (loopCount != stPaymentsList.size()) {
//                        Helper.showDialogMessage(getContext(), "DI LAHAT MAY ORDER", "Information");
                        Helper.showDialogMessage(getContext(), "Please attach an item to share to all customers", "Information");
                    } else if (mOrderCount != orderCount) {
                        Helper.showDialogMessage(getContext(), "Please allocate all items to sharing customers", "Information");
                    } else if (mPayment < mTotal) {
                        Helper.showDialogMessage(getContext(), "Insufficient payment", "Information");
                    }else {

                        confirmPayments(stPaymentsList);
                        dismiss();
//                        Helper.showDialogMessage(getContext(), "GO OKAY NA YAN", "Information");
                    }
                }



                break;
            case R.id.btnAddCustomer:
                if (orderCount > stPaymentsList.size()) {
                    customerCounter += 1;
                    stPaymentsList.add(new StPaymentsModel(
                            customerCounter,
                            "Customer " + (customerCounter),
                            new ArrayList<Payments>(),
                            new ArrayList<Orders>(),
                            false));
                    if (stPaymentsAdapter != null) {
                        stPaymentsAdapter.notifyDataSetChanged();
                    }
                } else {
                    Helper.showDialogMessage(getContext(), "Cannot add more person to share than the order quantity", "Information");
                }

                break;
        }
    }

    public abstract void confirmPayments(List<StPaymentsModel> list);

    @Override
    public void customerClicked(int position) {
        selectedCustomerPosition = position;
        for (StPaymentsModel st : stPaymentsList) {
            if (st.getId() == stPaymentsList.get(position).getId()) {
                st.setSelected(true);
            } else {
                st.setSelected(false);
            }
        }

        if (stPaymentsAdapter != null) {
            stPaymentsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void cashClicked(final int position) {
        CashFormDialog cashFormDialog = new CashFormDialog(getContext()) {
            @Override
            public void confirmPayment(String cashAmount) {
                try {
                    Payments p = new Payments(
                            1,//to fix later
                            1,
                            Double.valueOf(cashAmount), "CASH",
                            0,
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                            Utils.getDateTimeToday(),
                            1,
                            getUser().getUsername(),
                            Utils.getDateTimeToday(),
                            0); //bug to fix later on
                    p.setOther_data("");

                    stPaymentsList.get(position).getPaymentsList().add(p);

                    if (stPaymentsAdapter != null) {
                        stPaymentsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }
        };
        cashFormDialog.show();

    }

    @Override
    public void cardClicked(final int position) {
        CardFormDialog cardFormDialog = new CardFormDialog(getContext(), dataSyncViewModel) {
            @Override
            public void cardPaymentAdded(String creditCardAmount, String cardJsonData) {
                try {
                    Payments p = new Payments(
                            1, 2,
                            Double.valueOf(creditCardAmount), "CARD",
                            0,
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                            Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                            Utils.getDateTimeToday(),
                            1,
                            getUser().getUsername(),
                            Utils.getDateTimeToday(),
                            0); //bug to fix later on
                    p.setOther_data(cardJsonData);


                    stPaymentsList.get(position).getPaymentsList().add(p);

                    if (stPaymentsAdapter != null) {
                        stPaymentsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }


            }
        };
        cardFormDialog.show();
    }

    @Override
    public void orderRemovedClicked(int position, int viewPosition) {
        ordersList.add(stPaymentsList.get(position).getOrdersList().get(viewPosition));
        stPaymentsList.get(position).getOrdersList().remove(viewPosition);
        if (stPaymentsAdapter != null) {
            stPaymentsAdapter.notifyDataSetChanged();
        }

        if (stProdAdapter != null) {
            stProdAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void paymentRemovedClicked(int position, int orderPosition) {

        stPaymentsList.get(position).getPaymentsList().remove(orderPosition);
        if (stPaymentsAdapter != null) {
            stPaymentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clicked(final int position) {
        if (selectedCustomerPosition != 999) {
            if (ordersList.get(position).getQty() > 1) {
                ChooseQtyDialog chooseQtyDialog = new ChooseQtyDialog(getContext(), String.valueOf(ordersList.get(position).getQty())) {
                    @Override
                    public void confirmQty(int qty) {
                        Orders tmpOrder = ordersList.get(position);
                        Log.d("QEWQEQWEQ", String.valueOf(tmpOrder.getVatAmount()));
                        Double discountAmount =  (tmpOrder.getDiscountAmount());
                        Double finalAmount = 0.00;
                        if (tmpOrder.getVatExempt() > 0) {
                            finalAmount = Utils.roundedOffFourDecimal(tmpOrder.getOriginal_amount() / 1.12) - discountAmount;
                        } else {
                            finalAmount = (tmpOrder.getOriginal_amount()) - discountAmount;
                        }
                        Orders ord = new Orders(
                                tmpOrder.getTransaction_id(), //to change later on
                                tmpOrder.getCore_id(),
                                qty,
                                Utils.roundedOffFourDecimal(finalAmount),
                                tmpOrder.getOriginal_amount(),
                                tmpOrder.getName(),
                                tmpOrder.getDepartmentId(),
                                tmpOrder.getVatAmount(),
                                tmpOrder.getVatable(),
                                tmpOrder.getVatExempt(),
                                Utils.roundedOffFourDecimal(discountAmount),
                                tmpOrder.getDepartmentName(),
                                tmpOrder.getCategoryName(),
                                tmpOrder.getCategoryId(),
                                0,
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                                Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                                Utils.getDateTimeToday(),
                                tmpOrder.getIs_room_rate(),
                                tmpOrder.getNotes(),
                                tmpOrder.getIs_take_out(),
                                tmpOrder.getIs_fixed_asset(),
                                tmpOrder.getTo_id(),
                                tmpOrder.getName_initials(),
                                tmpOrder.getUnit_id(),
                                tmpOrder.getUnit_id_description()
                        );


                        if (qty == ordersList.get(position).getQty()) {
                            ordersList.remove(position);
                        } else {
                            ordersList.get(position).setQty(ordersList.get(position).getQty() - qty);
                        }

                        stPaymentsList.get(selectedCustomerPosition).getOrdersList().add(ord);

                        if (stProdAdapter != null) {
                            stProdAdapter.notifyDataSetChanged();
                        }

                        if (stPaymentsAdapter != null) {
                            stPaymentsAdapter.notifyDataSetChanged();
                        }
                    }
                };
                chooseQtyDialog.show();
            } else {

                Orders tmpOrder = ordersList.get(position);

                Double discountAmount =  (tmpOrder.getDiscountAmount());
                Double finalAmount = 0.00;
                if (tmpOrder.getVatExempt() > 0) {
                    finalAmount = Utils.roundedOffFourDecimal(tmpOrder.getOriginal_amount() / 1.12) - discountAmount;
                } else {
                    finalAmount = (tmpOrder.getOriginal_amount()) - discountAmount;
                }
                Orders ord = new Orders(
                        tmpOrder.getTransaction_id(), //to change later on
                        tmpOrder.getCore_id(),
                        tmpOrder.getQty(),
                        Utils.roundedOffFourDecimal(finalAmount),
                        tmpOrder.getOriginal_amount(),
                        tmpOrder.getName(),
                        tmpOrder.getDepartmentId(),
                        tmpOrder.getVatAmount(),
                        tmpOrder.getVatable(),
                        tmpOrder.getVatExempt(),
                        Utils.roundedOffFourDecimal(discountAmount),
                        tmpOrder.getDepartmentName(),
                        tmpOrder.getCategoryName(),
                        tmpOrder.getCategoryId(),
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(null, AppConstants.BRANCH_ID)),
                        Utils.getDateTimeToday(),
                        tmpOrder.getIs_room_rate(),
                        tmpOrder.getNotes(),
                        tmpOrder.getIs_take_out(),
                        tmpOrder.getIs_fixed_asset(),
                        tmpOrder.getTo_id(),
                        tmpOrder.getName_initials(),
                        tmpOrder.getUnit_id(),
                        tmpOrder.getUnit_id_description()

                );

                stPaymentsList.get(selectedCustomerPosition).getOrdersList().add(ord);

                if (stPaymentsAdapter != null) {
                    stPaymentsAdapter.notifyDataSetChanged();
                }

                ordersList.remove(position);
                if (stProdAdapter != null) {
                    stProdAdapter.notifyDataSetChanged();
                }
            }


        } else {
            Helper.showDialogMessage(getContext(), "Please select customer to share order", "Information");
        }
    }

    @Override
    public void customerRemoved(int position) {

        for (Orders ord : stPaymentsList.get(position).getOrdersList()) {
            ordersList.add(ord);
        }
        stPaymentsList.remove(position);

        if (stPaymentsAdapter != null) {
            stPaymentsAdapter.notifyDataSetChanged();
        }

        if (stProdAdapter != null) {
            stProdAdapter.notifyDataSetChanged();
        }

    }

    private User getUser() throws ExecutionException, InterruptedException {
        return userViewModel.searchLoggedInUser().get(0);
    }
}
