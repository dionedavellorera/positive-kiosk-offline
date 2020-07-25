package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CutOffMenuDialog extends BaseDialog implements View.OnClickListener{
    private PasswordDialog passwordDialog;

    private Button btnXReading;
    private Button btnZReading;
    private Button btnReprintXReading;
    private Button btnReprintZReading;
    private UserViewModel userViewModel;

    private ReprintXReadDialog reprintXReadDialog;
    private ReprintZReadDialog reprintZReadDialog;

    private TransactionsViewModel transactionsViewModel;
    private CutOffViewModel cutOffViewModel;
    private DataSyncViewModel dataSyncViewModel;
    private DateRangeDialog dateRangeDialog;
    public CutOffMenuDialog(Context context, TransactionsViewModel transactionsViewModel,
                            UserViewModel userViewModel, DataSyncViewModel dataSyncViewModel,
                            CutOffViewModel cutOffViewModel) {
        super(context);
        this.transactionsViewModel = transactionsViewModel;
        this.userViewModel = userViewModel;
        this.dataSyncViewModel = dataSyncViewModel;
        this.cutOffViewModel = cutOffViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_cutoff_menu, "CUT OFF MENU");
        setCancelable(false);
        initViews();
//        hideAccess();
    }

    private void hideAccess() {
        List<String> accessList = new ArrayList<>();
        String[]images = {"", ""};
        try {
            if (userViewModel.searchLoggedInUser().size() > 0) {
                User currentUser = userViewModel.searchLoggedInUser().get(0);
                if (currentUser != null) {
                    if (!TextUtils.isEmpty(currentUser.getAccess())) {
                        accessList = Arrays.asList(currentUser.getAccess().split(","));
                    }
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (accessList.size() > 0) {
            if (!accessList.contains("72")) { //XREAD
                btnXReading.setVisibility(View.GONE);
            }

            if (!accessList.contains("73")) { //ZREAD
                btnZReading.setVisibility(View.GONE);
            }

            if (!accessList.contains("75")) { //REPRINT X READ
                btnReprintXReading.setVisibility(View.GONE);
            }

            if (!accessList.contains("76")) { //REPRINT Z READ
                btnReprintZReading.setVisibility(View.GONE);
            }
        } else {
            btnXReading.setVisibility(View.VISIBLE);
            btnZReading.setVisibility(View.VISIBLE);
            btnReprintXReading.setVisibility(View.VISIBLE);
            btnReprintZReading.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        btnXReading = findViewById(R.id.btnXReading);
        btnXReading.setOnClickListener(this);
        btnZReading = findViewById(R.id.btnZReading);
        btnZReading.setOnClickListener(this);
        btnReprintXReading = findViewById(R.id.btnReprintXRead);
        btnReprintXReading.setOnClickListener(this);
        btnReprintZReading = findViewById(R.id.btnReprintZRead);
        btnReprintZReading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnXReading:
                if (Utils.isPasswordProtected(userViewModel, "72")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getContext(), "XREADING", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doXReadFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doXReadFunction();
                }

                break;
            case R.id.btnZReading:

                if (Utils.isPasswordProtected(userViewModel, "73")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getContext(), "ZREADING", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doZReadFunction();
                            }
                        };
                        passwordDialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doZReadFunction();
                }





                break;
            case R.id.btnReprintXRead:

                if (Utils.isPasswordProtected(userViewModel, "75")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getContext(), "XREADING", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doReprintXReading();
                            }
                        };
                        passwordDialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doReprintXReading();
                }




                break;
            case R.id.btnReprintZRead:

                if (Utils.isPasswordProtected(userViewModel, "76")) {
                    if (passwordDialog == null) {
                        passwordDialog = new PasswordDialog(getContext(), "ZREADING", userViewModel, transactionsViewModel) {
                            @Override
                            public void success(String username) {
                                doReprintZReading();
                            }
                        };
                        passwordDialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                passwordDialog = null;
                            }
                        });
                        passwordDialog.show();
                    }
                } else {
                    doReprintZReading();
                }







                break;
        }
    }

    private void doReprintZReading() {
        if (dateRangeDialog == null) {
            dateRangeDialog = new DateRangeDialog(getContext()) {
                @Override
                void dateConfirmed(String dateFrom, String dateTo) {

                    if (reprintZReadDialog == null) {

                        try {

                            if (cutOffViewModel.getEndOfDayViaDate(dateFrom, dateTo).size() > 0) {
                                reprintZReadDialog = new ReprintZReadDialog(CutOffMenuDialog.this.getContext(), cutOffViewModel.getEndOfDayViaDate(dateFrom, dateTo));
                                reprintZReadDialog.setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        reprintZReadDialog = null;
                                    }
                                });

                                reprintZReadDialog.setOnDismissListener(new OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        reprintZReadDialog = null;
                                    }
                                });

                                reprintZReadDialog.show();


                            } else {
                                Helper.showDialogMessage(CutOffMenuDialog.this.getContext(), String.format("No data for z reading %s %s", dateFrom, dateTo), getContext().getString(R.string.header_message));
                            }



                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };


            dateRangeDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dateRangeDialog = null;
                }
            });

            dateRangeDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dateRangeDialog = null;
                }
            });
            dateRangeDialog.show();
        }
    }

    private void doReprintXReading() {
        if (dateRangeDialog == null) {
            dateRangeDialog = new DateRangeDialog(getContext()) {
                @Override
                void dateConfirmed(String dateFrom, String dateTo) {
                    if (reprintXReadDialog == null) {
                        try {

                            if (cutOffViewModel.getCutOffViaDate(dateFrom, dateTo).size() > 0) {
                                reprintXReadDialog = new ReprintXReadDialog(CutOffMenuDialog.this.getContext(), cutOffViewModel.getCutOffViaDate(dateFrom, dateTo));
                                reprintXReadDialog.setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        reprintXReadDialog = null;
                                    }
                                });

                                reprintXReadDialog.setOnDismissListener(new OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        reprintXReadDialog = null;
                                    }
                                });

                                reprintXReadDialog.show();


                            } else {
                                Helper.showDialogMessage(CutOffMenuDialog.this.getContext(), String.format("No data for x reading %s %s", dateFrom, dateTo), getContext().getString(R.string.header_message));
                            }

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }


                }
            };

            dateRangeDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dateRangeDialog = null;
                }
            });

            dateRangeDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dateRangeDialog = null;
                }
            });
            dateRangeDialog.show();
        }
    }

    private void doZReadFunction() {
        try {
            if (cutOffViewModel.getUnCutOffData().size() > 0) {
                //PERFORM Z READING
                long end_of_day_id = cutOffViewModel.insertData(new EndOfDay(
                        0,
                        0.00,
                        0.00,
                        0.00,
                        0.00,
                        0.00,
                        0.00,
                        0.00,
                        Utils.getDateTimeToday(),
                        "",
                        "",
                        0.00,
                        0.00,
                        0,
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                        Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                        0.00,
                        0.00
                ));

                for (PostedDiscounts postedDiscounts : cutOffViewModel.getZeroEndOfDay()) {
                    postedDiscounts.setIs_sent_to_server(0);
                    postedDiscounts.setEnd_of_day_id((int)end_of_day_id);
                }

                int number_of_transaction = 0;
                Double gross_sales = 0.00;
                Double net_sales = 0.00;
                Double vatable_sales = 0.00;
                Double vat_exempt_sales = 0.00;
                Double vat_amount = 0.00;
                Double void_amount = 0.00;
                Double total_cash = 0.00;
                Double total_cash_payments = 0.00;
                Double total_card_payments = 0.00;

                Double total_online_payments = 0.00;
                Double total_ar_payments = 0.00;
                Double total_mobile_payments = 0.00;

                Double total_change = 0.00;
                Double total_payout = 0.00;
                Double total_service_charge = 0.00;

                Double cash_redeemed_from_prev_ar = 0.00;
                Double card_redeemed_from_prev_ar = 0.00;

                Double discount_amount = 0.00;

                int seniorCount = 0;
                Double seniorAmount = 0.00;
                int pwdCount = 0;
                Double pwdAmount = 0.00;
                int othersCount = 0;
                Double othersAmount = 0.00;
                List<String> orNumberArray = new ArrayList<>();

                for (CutOff cutOff : cutOffViewModel.getUnCutOffData()) {
                    cash_redeemed_from_prev_ar += cutOff.getCash_redeemed_from_prev_ar();
                    card_redeemed_from_prev_ar += cutOff.getCard_redeemed_from_prev_ar();
                    total_service_charge += cutOff.getTotal_service_charge();
                    cutOff.setIs_sent_to_server(0);
                    orNumberArray.add(cutOff.getBegOrNo());
                    orNumberArray.add(cutOff.getEndOrNo());
                    cutOff.setTreg(Utils.getDateTimeToday());
                    cutOff.setZ_read_id((int) end_of_day_id);
                    cutOffViewModel.update(cutOff);
                    total_payout += cutOff.getTotal_payout();
                    void_amount += cutOff.getVoid_amount();
                    gross_sales += cutOff.getGross_sales();
                    net_sales += cutOff.getNet_sales();
                    vatable_sales += cutOff.getVatable_sales();
                    vat_exempt_sales += cutOff.getVat_exempt_sales();
                    vat_amount += cutOff.getVat_amount();
                    number_of_transaction += cutOff.getNumber_of_transactions();
                    total_cash += cutOff.getTotal_cash_amount();
                    total_cash_payments += cutOff.getTotal_cash_payments();
                    total_card_payments += cutOff.getTotal_card_payments();

                    total_ar_payments += cutOff.getTotal_ar_payments();
                    total_online_payments += cutOff.getTotal_online_payments();
                    total_mobile_payments += cutOff.getTotal_mobile_payments();

                    total_change += cutOff.getTotal_change() != null ? cutOff.getTotal_change() : 0.00;
                    discount_amount += cutOff.getDiscount_amount();
                    seniorCount += cutOff.getSeniorCount();
                    seniorAmount += cutOff.getSeniorAmount();
                    pwdCount += cutOff.getPwdCount();
                    pwdAmount += cutOff.getPwdAmount();
                    othersCount += cutOff.getOthersCount();
                    othersAmount += cutOff.getOthersAmount();
                }

                EndOfDay endOfDay = cutOffViewModel.getEndOfDay(end_of_day_id);
                endOfDay.setCard_redeemed_from_prev_ar(card_redeemed_from_prev_ar);
                endOfDay.setCash_redeemed_from_prev_ar(cash_redeemed_from_prev_ar);
                endOfDay.setTotal_service_charge(total_service_charge);
                endOfDay.setTotal_payout(total_payout);
                endOfDay.setIs_sent_to_server(0);
                endOfDay.setGross_sales(Utils.roundedOffFourDecimal(gross_sales));
                endOfDay.setNet_sales(Utils.roundedOffFourDecimal(net_sales));
                endOfDay.setVatable_sales(Utils.roundedOffFourDecimal(vatable_sales));
                endOfDay.setVat_exempt_sales(Utils.roundedOffFourDecimal(vat_exempt_sales));
                endOfDay.setVat_amount(Utils.roundedOffFourDecimal(vat_amount));
                endOfDay.setNumber_of_transactions(number_of_transaction);
                endOfDay.setVoid_amount(Utils.roundedOffFourDecimal(void_amount));
                endOfDay.setTotal_cash_amount(Utils.roundedOffFourDecimal(total_cash));
                endOfDay.setTotal_cash_payments(Utils.roundedOffFourDecimal(total_cash_payments));
                endOfDay.setTotal_card_payments(Utils.roundedOffFourDecimal(total_card_payments));

                endOfDay.setTotal_online_payments(Utils.roundedOffFourDecimal(total_online_payments));
                endOfDay.setTotal_ar_payments(Utils.roundedOffFourDecimal(total_ar_payments));
                endOfDay.setTotal_mobile_payments(Utils.roundedOffFourDecimal(total_mobile_payments));


                endOfDay.setTotal_change(total_change);

                endOfDay.setSeniorCount(seniorCount);
                endOfDay.setSeniorAmount(seniorAmount);
                endOfDay.setPwdCount(pwdCount);
                endOfDay.setPwdAmount(pwdAmount);
                endOfDay.setOthersCount(othersCount);
                endOfDay.setOthersAmount(othersAmount);
                endOfDay.setDiscount_amount(discount_amount);

                if (orNumberArray.size() > 0) {
                    endOfDay.setBegOrNo(orNumberArray.get(0));
                    endOfDay.setEndOrNo(orNumberArray.get(orNumberArray.size() -1));
                }

                List<EndOfDay> eod = cutOffViewModel.getEndOfDayList();
                if (eod.size() > 1) {
                    endOfDay.setBegSales(eod.get(1).getEndSales());
                    endOfDay.setEndSales(gross_sales + eod.get(1).getEndSales());
                } else {
                    endOfDay.setBegSales(0.00);
                    endOfDay.setEndSales(gross_sales);
                }

                cutOffViewModel.update(endOfDay);

                BusProvider.getInstance().post(new PrintModel("PRINT_ZREAD", GsonHelper.getGson().toJson(endOfDay)));


                CutOffMenuDialog.this.dismiss();
            } else {
                Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_cut_off_to_end_of_day), getContext().getString(R.string.header_message));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doXReadFunction() {
        CollectionDialog collectionDialog = new CollectionDialog(getContext(), dataSyncViewModel) {
            @Override
            public void cutOffSuccess(Double totalCash) {
                try {

                    long cut_off_id = cutOffViewModel.insertData(new CutOff(
                            0,
                            0.00,
                            0.00,
                            0.00,
                            0.00,
                            0.00,
                            0.00,
                            0.00,
                            0,
                            Utils.getDateTimeToday(),
                            "",
                            "",
                            0,
                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.MACHINE_ID)),
                            Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID)),
                            0.00,
                            0.00,
                            String.valueOf(cutOffViewModel.getUnCutOffData().size() + 1 )
                    ));

                    List<Transactions> transactionsList = transactionsViewModel.unCutOffTransactions(userViewModel.searchLoggedInUser().get(0).getUsername());
                    int number_of_transaction = 0;
                    Double gross_sales = 0.00;
                    Double net_sales = 0.00;
                    Double vatable_sales = 0.00;
                    Double vat_exempt_sales = 0.00;
                    Double vat_amount = 0.00;
                    Double void_amount = 0.00;

                    Double total_cash_payments = 0.00;
                    Double total_card_payments = 0.00;

                    Double total_online_payments = 0.00;
                    Double total_ar_payments = 0.00;
                    Double total_mobile_payments = 0.00;

                    Double cash_redeemed_from_prev_ar = 0.00;
                    Double card_redeemed_from_prev_ar = 0.00;


                    Double total_change = 0.00;
                    Double total_service_charge = 0.00;

                    Double discount_amount = 0.00;

                    int seniorCount = 0;
                    Double seniorAmount = 0.00;
                    int pwdCount = 0;
                    Double pwdAmount = 0.00;
                    int othersCount = 0;
                    Double othersAmount = 0.00;
                    Double total_payout = 0.00;

                    String begOrNo = "";
                    String endOrNo = "";


                    if (transactionsList.size() > 0) {
                        begOrNo = transactionsList.get(0).getReceipt_number();
                        endOrNo = transactionsList.get(transactionsList.size() - 1).getReceipt_number();
                        for (Transactions tr : transactionsList) {
                            if (tr.getIs_void()) {
                                void_amount += tr.getGross_sales() + tr.getDiscount_amount();
                            } else {
                                gross_sales += tr.getGross_sales();
                                net_sales += tr.getNet_sales();
                                vatable_sales += tr.getVatable_sales();
                                vat_exempt_sales += tr.getVat_exempt_sales();
                                vat_amount += tr.getVat_amount();
                                total_change += tr.getChange();
                                total_service_charge += tr.getService_charge_value();
                                discount_amount += tr.getDiscount_amount();
                            }
                            number_of_transaction += 1;

//                            tr.setService_charge_value(total_service_charge);
                            tr.setIs_sent_to_server(0);
                            tr.setIs_cut_off(true);
                            tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                            tr.setCut_off_id(cut_off_id);
                            tr.setIs_cut_off_at(Utils.getDateTimeToday());
                            transactionsViewModel.update(tr);
                        }
//                        dismiss();


                        for (Payments payments : cutOffViewModel.getAllPayments()) {
                            switch (payments.getCore_id()) {
                                case 1://CASH
                                    if (!payments.getIs_void()) {
                                        if (payments.getIs_from_other_shift() != 0) {
                                            cash_redeemed_from_prev_ar += payments.getAmount();
                                        }
                                        total_cash_payments += payments.getAmount();
                                    }
                                    break;
                                case 2://CARD
                                    if (!payments.getIs_void()) {
                                        if (payments.getIs_from_other_shift() != 0) {
                                            card_redeemed_from_prev_ar += payments.getAmount();
                                        }
                                        total_card_payments += payments.getAmount();
                                    }

                                    break;
                                case 3://ONLINE
                                    if (!payments.getIs_void()) {
                                        total_online_payments += payments.getAmount();
                                    }
                                    break;
                                case 8://AR
                                    if (!payments.getIs_void()) {
                                        if (payments.getIs_redeemed() == 0) {
                                            total_ar_payments += payments.getAmount();
                                        }
                                    }

                                    break;
                                case 9://MOBILE PAYMENT
                                    if (!payments.getIs_void()) {
                                        total_mobile_payments += payments.getAmount();
                                    }

                                    break;
                            }

                            payments.setIs_sent_to_server(0);
                            payments.setCut_off_id((int) cut_off_id);
                            cutOffViewModel.update(payments);
                        }

                        for (Payout payout : cutOffViewModel.getUnCutOffPayouts()) {

                            total_payout += payout.getAmount();
                            payout.setIs_sent_to_server(0);
                            payout.setIs_cut_off(true);
                            payout.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                            payout.setCut_off_id((int)cut_off_id);
                            payout.setIs_cut_off_at(Utils.getDateTimeToday());
                            cutOffViewModel.update(payout);

                        }

                        for (PostedDiscounts postedDiscounts : cutOffViewModel.getUnCutOffPostedDiscount()) {
                            postedDiscounts.setCut_off_id((int)cut_off_id);
                            postedDiscounts.setIs_sent_to_server(0);
                            if (!postedDiscounts.getIs_void()) {
                                if (postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR")) {
                                    seniorCount += 1;
                                    seniorAmount += postedDiscounts.getAmount();
                                } else if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD")) {
                                    pwdCount += 1;
                                    pwdAmount += postedDiscounts.getAmount();
                                } else {
                                    othersCount += 1;
                                    othersAmount += postedDiscounts.getAmount();
                                }
                            }

                            cutOffViewModel.update(postedDiscounts);

                        }

                        Log.d("DATA-PREVCASH", String.valueOf(cash_redeemed_from_prev_ar));
                        Log.d("DATA-PREVCARD", String.valueOf(card_redeemed_from_prev_ar));

                        Log.d("DATA-ONLINEPAY", String.valueOf(total_online_payments));
                        Log.d("DATA-ARPAY", String.valueOf(total_ar_payments));
                        Log.d("DATA-MOBILEPAY", String.valueOf(total_mobile_payments));

                        Log.d("DATA-CASHPAY", String.valueOf(total_cash_payments));
                        Log.d("DATA-CARDPAY", String.valueOf(total_card_payments));
                        Log.d("DATA-GROSSSALE", String.valueOf(gross_sales));
                        Log.d("DATA-NETSALES", String.valueOf(net_sales));
                        Log.d("DATA-VATSALES", String.valueOf(vatable_sales));
                        Log.d("DATA-VATEXESALES", String.valueOf(vat_exempt_sales));
                        Log.d("DATA-CHANGE", String.valueOf(total_change));
                        Log.d("DATA-TOTALCASH", String.valueOf(totalCash));
                        Log.d("DATA-SHORTOVER", String.valueOf(totalCash - (total_cash_payments - total_change)));

                        CutOff cutOff = cutOffViewModel.getCutOff(cut_off_id);
                        //SHORT OVER COMPUTATION
                        //cutOff.getTotal_cash_amount() - (cutOff.getTotal_cash_payments() - cutOff.getTotal_change())
                        cutOff.setTotal_service_charge(total_service_charge);
                        cutOff.setIs_sent_to_server(0);
                        cutOff.setTotal_payout(total_payout);
                        cutOff.setTotal_change(Utils.roundedOffFourDecimal(total_change));
                        cutOff.setGross_sales(Utils.roundedOffFourDecimal(gross_sales));
                        cutOff.setNet_sales(Utils.roundedOffFourDecimal(net_sales));
                        cutOff.setVatable_sales(Utils.roundedOffFourDecimal(vatable_sales));
                        cutOff.setVat_exempt_sales(Utils.roundedOffFourDecimal(vat_exempt_sales));
                        cutOff.setVat_amount(Utils.roundedOffFourDecimal(vat_amount));
                        cutOff.setNumber_of_transactions(number_of_transaction);
                        cutOff.setVoid_amount(Utils.roundedOffFourDecimal(void_amount));
                        cutOff.setTotal_cash_amount(Utils.roundedOffFourDecimal(totalCash));
                        cutOff.setTotal_cash_payments(Utils.roundedOffFourDecimal(total_cash_payments));
                        cutOff.setTotal_card_payments(Utils.roundedOffFourDecimal(total_card_payments));

                        cutOff.setTotal_online_payments(Utils.roundedOffFourDecimal(total_online_payments));
                        cutOff.setTotal_ar_payments(Utils.roundedOffFourDecimal(total_ar_payments));
                        cutOff.setTotal_mobile_payments(Utils.roundedOffFourDecimal(total_mobile_payments));

                        cutOff.setSeniorCount(seniorCount);
                        cutOff.setSeniorAmount(seniorAmount);
                        cutOff.setPwdCount(pwdCount);
                        cutOff.setPwdAmount(pwdAmount);
                        cutOff.setOthersCount(othersCount);
                        cutOff.setOthersAmount(othersAmount);

                        cutOff.setBegOrNo(begOrNo);
                        cutOff.setEndOrNo(endOrNo);
                        cutOff.setDiscount_amount(discount_amount);

                        BusProvider.getInstance().post(new PrintModel("PRINT_XREAD", GsonHelper.getGson().toJson(cutOff)));

                        cutOffViewModel.update(cutOff);
                        dismiss();
                        CutOffMenuDialog.this.dismiss();


                    } else {
                        Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_transaction_cutoff), getContext().getString(R.string.header_message));
                    }



                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        collectionDialog.show();

    }
}
