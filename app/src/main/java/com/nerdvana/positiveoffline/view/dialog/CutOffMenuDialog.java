package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Transactions;
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
    private TransactionsViewModel transactionsViewModel;
    private CutOffViewModel cutOffViewModel;
    private DataSyncViewModel dataSyncViewModel;
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
                                    Utils.getDateTimeToday()
                            ));



                            List<Transactions> transactionsList = transactionsViewModel.unCutOffTransactions(userViewModel.searchLoggedInUser().get(0).getUsername());
                            int number_of_transaction = 0;
                            Double gross_sales = 0.00;
                            Double net_sales = 0.00;
                            Double vatable_sales = 0.00;
                            Double vat_exempt_sales = 0.00;
                            Double vat_amount = 0.00;
                            Double void_amount = 0.00;
                            if (transactionsList.size() > 0) {
                                for (Transactions tr : transactionsList) {
                                    if (tr.getIs_void()) {
                                        void_amount += tr.getNet_sales();
                                    } else {
                                        gross_sales += tr.getGross_sales();
                                        net_sales += tr.getNet_sales();
                                        vatable_sales += tr.getVatable_sales();
                                        vat_exempt_sales += tr.getVat_exempt_sales();
                                        vat_amount += tr.getVat_amount();
                                    }
                                    number_of_transaction += 1;

                                    tr.setIs_cut_off(true);
                                    tr.setIs_cut_off_by(userViewModel.searchLoggedInUser().get(0).getUsername());
                                    tr.setCut_off_id(cut_off_id);
                                    transactionsViewModel.update(tr);
                                }
                                dismiss();
                            } else {
                                Helper.showDialogMessage(getContext(), getContext().getString(R.string.error_no_transaction_cutoff), getContext().getString(R.string.header_message));
                            }

                            CutOff cutOff = cutOffViewModel.getCutOff(cut_off_id);
                            cutOff.setGross_sales(Utils.roundedOffTwoDecimal(gross_sales));
                            cutOff.setNet_sales(Utils.roundedOffTwoDecimal(net_sales));
                            cutOff.setVatable_sales(Utils.roundedOffTwoDecimal(vatable_sales));
                            cutOff.setVat_exempt_sales(Utils.roundedOffTwoDecimal(vat_exempt_sales));
                            cutOff.setVat_amount(Utils.roundedOffTwoDecimal(vat_amount));
                            cutOff.setNumber_of_transactions(number_of_transaction);
                            cutOff.setVoid_amount(Utils.roundedOffTwoDecimal(void_amount));
                            cutOff.setTotal_cash_amount(totalCash);
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
                                Utils.getDateTimeToday()
                        ));

                        int number_of_transaction = 0;
                        Double gross_sales = 0.00;
                        Double net_sales = 0.00;
                        Double vatable_sales = 0.00;
                        Double vat_exempt_sales = 0.00;
                        Double vat_amount = 0.00;
                        Double void_amount = 0.00;
                        Double total_cash = 0.00;

                        for (CutOff cutOff : cutOffViewModel.getUnCutOffData()) {
                            cutOff.setCreated_at(Utils.getDateTimeToday());
                            cutOff.setZ_read_id((int) end_of_day_id);
                            cutOffViewModel.update(cutOff);
                            void_amount += cutOff.getNet_sales();
                            gross_sales += cutOff.getGross_sales();
                            net_sales += cutOff.getNet_sales();
                            vatable_sales += cutOff.getVatable_sales();
                            vat_exempt_sales += cutOff.getVat_exempt_sales();
                            vat_amount += cutOff.getVat_amount();
                            number_of_transaction += cutOff.getNumber_of_transactions();
                            total_cash += cutOff.getTotal_cash_amount();


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
                        cutOffViewModel.update(endOfDay);

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
                break;
            case R.id.btnReprintZRead:
                break;
        }
    }
}
