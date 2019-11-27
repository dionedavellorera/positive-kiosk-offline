package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.view.HidingEditText;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SpecialDiscDialog extends BaseDialog implements View.OnClickListener{

    private String header;

    private Button btnConfirm;
    private HidingEditText etCardNumber;
    private HidingEditText etName;
    private HidingEditText etAddress;
    private DiscountWithSettings discountWithSettings;
    private List<Orders> ordersList;
    private TransactionsViewModel transactionsViewModel;
    private DiscountViewModel discountViewModel;
    private String transactionId;
    public SpecialDiscDialog(Context context,
                             String header,
                             DiscountWithSettings discountWithSettings,
                             List<Orders> orderList,
                             TransactionsViewModel transactionsViewModel,
                             DiscountViewModel discountViewModel,
                             String transactionId) {
        super(context);
        this.header = header;
        this.discountWithSettings = discountWithSettings;
        this.ordersList = orderList;
        this.transactionsViewModel = transactionsViewModel;
        this.discountViewModel = discountViewModel;
        this.transactionId = transactionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_specialdiscount, header);
        setCancelable(false);
        initViews();
    }

    private void initViews() {
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        etCardNumber = findViewById(R.id.etCardNumber);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnConfirm:

                Log.d("QWEQWE",String.valueOf(ordersList.size()));

                for (Orders orders : ordersList) {
                    int count = 0; //2 for discounted counted
                    double percentage = 0.00;
                    for (DiscountSettings disc : discountWithSettings.discountsList) {

                        if (!TextUtils.isEmpty(disc.getProduct_id())) {
                            if (disc.getProduct_id().equalsIgnoreCase("all")) {
                                count+=1;
                            } else if (Arrays.asList(disc.getProduct_id().split(",")).contains(String.valueOf(orders.getCore_id()))) {
                                count+=1;
                            }
                        }

                        if (!TextUtils.isEmpty(disc.getDepartment_id())) {
                            if (disc.getDepartment_id().equalsIgnoreCase("all")) {
                                count+=1;
                            } else if (Arrays.asList(disc.getDepartment_id().split(",")).contains(String.valueOf(orders.getDepartmentId()))) {
                                count+=1;
                            }
                        }

                        percentage = disc.getPercentage();
                    }
                    if (count == 2) {
                        List<OrderDiscounts> orderDiscountsList = new ArrayList<>();
                        orderDiscountsList.add(new OrderDiscounts(
                                orders.getCore_id(),
                                true,
                                percentage,
                                Integer.valueOf(transactionId)));
                        discountViewModel.insertOrderDiscount(orderDiscountsList);
                    }
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Log.d("MYDISCOUNTTRANSID", transactionId);

                            for (OrderWithDiscounts owd : discountViewModel.getOrderWithDiscount(transactionId)) {
                                Double remainingAmount = Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12);
                                Double totalDiscountAmount = 0.00;
                                for (OrderDiscounts od : owd.orderWithDiscountList) {
                                    totalDiscountAmount += Utils.roundedOffTwoDecimal((Utils.roundedOffTwoDecimal(remainingAmount) - Utils.roundedOffTwoDecimal(totalDiscountAmount)) * (od.getValue() / 100));
                                }

                                remainingAmount = Utils.roundedOffTwoDecimal(remainingAmount - totalDiscountAmount);
                                Orders ord = new Orders(
                                        owd.orders.getTransaction_id(),
                                        owd.orders.getCore_id(),
                                        owd.orders.getQty(),
                                        remainingAmount,
                                        owd.orders.getOriginal_amount(),
                                        owd.orders.getName(),
                                        owd.orders.getDepartmentId(),
                                        Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() - (owd.orders.getOriginal_amount() / 1.12)),
                                        0.00,
                                        Utils.roundedOffTwoDecimal(owd.orders.getOriginal_amount() / 1.12),
                                        totalDiscountAmount

                                );

                                Log.d("QWEQWQEQWEWQ", String.valueOf(owd.orders.getId()));

                                ord.setId(owd.orders.getId());
                                transactionsViewModel.updateOrder(ord);
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);






//                dismiss();

//                if (!TextUtils.isEmpty(etCardNumber.getText().toString()) &&
//                    !TextUtils.isEmpty(etName.getText().toString()) &&
//                    !TextUtils.isEmpty(etAddress.getText().toString())) {
//
//                } else {
//                    Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_message_fill_up_all_fields), getContext().getString(R.string.header_message));
//                }

                break;
        }
    }
}
