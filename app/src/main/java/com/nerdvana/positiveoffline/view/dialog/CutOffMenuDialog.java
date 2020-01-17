package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CutOffMenuDialog extends BaseDialog implements View.OnClickListener{
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
                                    Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
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
                            Double total_change = 0.00;

                            int seniorCount = 0;
                            Double seniorAmount = 0.00;
                            int pwdCount = 0;
                            Double pwdAmount = 0.00;
                            int othersCount = 0;
                            Double othersAmount = 0.00;

                            String begOrNo = "";
                            String endOrNo = "";


                            if (transactionsList.size() > 0) {
                                begOrNo = transactionsList.get(0).getReceipt_number();
                                endOrNo = transactionsList.get(transactionsList.size() - 1).getReceipt_number();
                                for (Transactions tr : transactionsList) {
                                    if (tr.getIs_void()) {
                                        void_amount += tr.getNet_sales();
                                    } else {

                                        gross_sales += tr.getGross_sales();
                                        net_sales += tr.getNet_sales();
                                        vatable_sales += tr.getVatable_sales();
                                        vat_exempt_sales += tr.getVat_exempt_sales();
                                        vat_amount += tr.getVat_amount();
                                        total_change += tr.getChange();
                                    }
                                    number_of_transaction += 1;

                                    tr.setIs_cut_off(true);
                                    tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                                    tr.setCut_off_id(cut_off_id);
                                    tr.setIs_cut_off_at(Utils.getDateTimeToday());
                                    transactionsViewModel.update(tr);
                                }
                                dismiss();
                            } else {
                                Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_transaction_cutoff), getContext().getString(R.string.header_message));
                            }


                            for (Payments payments : cutOffViewModel.getAllPayments()) {
                                payments.setCut_off_id((int) cut_off_id);
                                cutOffViewModel.update(payments);
                                switch (payments.getCore_id()) {
                                    case 1://CASH
                                        total_cash_payments += payments.getAmount();
                                        break;
                                    case 2://CARD
                                        total_card_payments += payments.getAmount();
                                }
                            }

                            for (PostedDiscounts postedDiscounts : cutOffViewModel.getUnCutOffPostedDiscount()) {
                                postedDiscounts.setCut_off_id((int)cut_off_id);
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

                            CutOff cutOff = cutOffViewModel.getCutOff(cut_off_id);
                            cutOff.setTotal_change(Utils.roundedOffTwoDecimal(total_change));
                            cutOff.setGross_sales(Utils.roundedOffTwoDecimal(gross_sales));
                            cutOff.setNet_sales(Utils.roundedOffTwoDecimal(net_sales));
                            cutOff.setVatable_sales(Utils.roundedOffTwoDecimal(vatable_sales));
                            cutOff.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vat_exempt_sales));
                            cutOff.setVat_amount(Utils.roundedOffTwoDecimal(vat_amount));
                            cutOff.setNumber_of_transactions(number_of_transaction);
                            cutOff.setVoid_amount(Utils.roundedOffTwoDecimal(void_amount));
                            cutOff.setTotal_cash_amount(Utils.roundedOffTwoDecimal(totalCash));
                            cutOff.setTotal_cash_payments(Utils.roundedOffTwoDecimal(total_cash_payments));
                            cutOff.setTotal_card_payments(Utils.roundedOffTwoDecimal(total_card_payments));

                            cutOff.setSeniorCount(seniorCount);
                            cutOff.setSeniorAmount(seniorAmount);
                            cutOff.setPwdCount(pwdCount);
                            cutOff.setPwdAmount(pwdAmount);
                            cutOff.setOthersCount(othersCount);
                            cutOff.setOthersAmount(othersAmount);

                            cutOff.setBegOrNo(begOrNo);
                            cutOff.setEndOrNo(endOrNo);

                            BusProvider.getInstance().post(new PrintModel("PRINT_XREAD", GsonHelper.getGson().toJson(cutOff)));

                            cutOffViewModel.update(cutOff);
                            CutOffMenuDialog.this.dismiss();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                };
                collectionDialog.show();
                break;
            case R.id.btnZReading:

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
                                Integer.valueOf(SharedPreferenceManager.getString(getContext(), AppConstants.BRANCH_ID))
                        ));

                        for (PostedDiscounts postedDiscounts : cutOffViewModel.getZeroEndOfDay()) {
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
                        Double total_change = 0.00;

                        int seniorCount = 0;
                        Double seniorAmount = 0.00;
                        int pwdCount = 0;
                        Double pwdAmount = 0.00;
                        int othersCount = 0;
                        Double othersAmount = 0.00;

                        List<String> orNumberArray = new ArrayList<>();

                        for (CutOff cutOff : cutOffViewModel.getUnCutOffData()) {
                            orNumberArray.add(cutOff.getBegOrNo());
                            orNumberArray.add(cutOff.getEndOrNo());
                            cutOff.setTreg(Utils.getDateTimeToday());
                            cutOff.setZ_read_id((int) end_of_day_id);
                            cutOffViewModel.update(cutOff);
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
                            total_change += cutOff.getTotal_change() != null ? cutOff.getTotal_change() : 0.00;

                            seniorCount += cutOff.getSeniorCount();
                            seniorAmount += cutOff.getSeniorAmount();
                            pwdCount += cutOff.getPwdCount();
                            pwdAmount += cutOff.getPwdAmount();
                            othersCount += cutOff.getOthersCount();
                            othersAmount += cutOff.getOthersAmount();
                        }

                        EndOfDay endOfDay = cutOffViewModel.getEndOfDay(end_of_day_id);
                        endOfDay.setGross_sales(Utils.roundedOffTwoDecimal(gross_sales));
                        endOfDay.setNet_sales(Utils.roundedOffTwoDecimal(net_sales));
                        endOfDay.setVatable_sales(Utils.roundedOffTwoDecimal(vatable_sales));
                        endOfDay.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vat_exempt_sales));
                        endOfDay.setVat_amount(Utils.roundedOffTwoDecimal(vat_amount));
                        endOfDay.setNumber_of_transactions(number_of_transaction);
                        endOfDay.setVoid_amount(Utils.roundedOffTwoDecimal(void_amount));
                        endOfDay.setTotal_cash_amount(Utils.roundedOffTwoDecimal(total_cash));
                        endOfDay.setTotal_cash_payments(Utils.roundedOffTwoDecimal(total_cash_payments));
                        endOfDay.setTotal_card_payments(Utils.roundedOffTwoDecimal(total_card_payments));
                        endOfDay.setTotal_change(total_change);

                        endOfDay.setSeniorCount(seniorCount);
                        endOfDay.setSeniorAmount(seniorAmount);
                        endOfDay.setPwdCount(pwdCount);
                        endOfDay.setPwdAmount(pwdAmount);
                        endOfDay.setOthersCount(othersCount);
                        endOfDay.setOthersAmount(othersAmount);

                        if (orNumberArray.size() > 0) {
                            endOfDay.setBegOrNo(orNumberArray.get(0));
                            endOfDay.setEndOrNo(orNumberArray.get(orNumberArray.size() -1));
                        }

                        List<EndOfDay> eod = cutOffViewModel.getEndOfDayList();
                        if (eod.size() > 1) {
                            endOfDay.setBegSales(eod.get(1).getEndSales());
                            endOfDay.setEndSales(gross_sales + eod.get(1).getEndSales());
                        } else {
                            endOfDay.setBegSales(gross_sales);
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


                break;
            case R.id.btnReprintXRead:
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

                break;
            case R.id.btnReprintZRead:

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




                break;
        }
    }
}
