package com.nerdvana.positiveoffline.printjobasync;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.printer.SPrinter;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class PrintReceiptAsync extends AsyncTask<Void, Void, Void> {

    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;
    public PrintReceiptAsync(PrintModel printModel, Context context,
                             AsyncFinishCallBack asyncFinishCallBack,
                             DataSyncViewModel dataSyncViewModel) {
        this.context = context;
        this.printModel = printModel;
        this.asyncFinishCallBack = asyncFinishCallBack;
        this.dataSyncViewModel = dataSyncViewModel;
    }



    @Override
    protected Void doInBackground(Void... voids) {

        try {

            printer = new Printer(
                    dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                    dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                    context
            );
            printer.addPulse(Printer.DRAWER_HIGH, Printer.PULSE_100);
        } catch (Epos2Exception e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        printer.setReceiveEventListener(new ReceiveListener() {
            @Override
            public void onPtrReceive(final Printer printer, int i, PrinterStatusInfo printerStatusInfo, String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            printer.disconnect();
                            asyncFinishCallBack.doneProcessing();
                        } catch (Epos2Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        PrinterUtils.connect(context, printer);


        TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);


        PrinterUtils.addHeader(printModel, printer);
        addPrinterSpace(1, printer);
        addTextToPrinter(printer, "OFFICIAL RECEIPT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
        addPrinterSpace(1, printer);
        addTextToPrinter(printer, twoColumns(
                "OR NO",
                transactionCompleteDetails.transactions.getReceipt_number(),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addTextToPrinter(printer, twoColumns(
                "DATE",
                transactionCompleteDetails.transactions.getCreated_at(),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addPrinterSpace(1, printer);

        addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addTextToPrinter(printer, "QTY  DESCRIPTION          AMOUNT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        Double amountDue = 0.00;
        for (Orders orders : transactionCompleteDetails.ordersList) {

            String qty = "";

            qty += orders.getQty();

            if (String.valueOf(orders.getQty()).length() < 4) {
                for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                    qty += " ";
                }
            }
            addTextToPrinter(printer, twoColumns(
                    qty +  " " + orders.getName(),
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            if (orders.getVatExempt() <= 0) {
                amountDue += orders.getOriginal_amount();
            } else {
                amountDue += orders.getAmount();
            }
        }



        if (transactionCompleteDetails.postedDiscountsList.size() > 0) {
            addPrinterSpace(1, printer);
            addTextToPrinter(printer, twoColumns(
                    "DISCOUNT LIST",
                    "",
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            for (PostedDiscounts postedDiscounts : transactionCompleteDetails.postedDiscountsList) {
                addTextToPrinter(printer, twoColumns(
                        postedDiscounts.getDiscount_name(),
                        postedDiscounts.getCard_number(),
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            }


        }

        addPrinterSpace(1, printer);


        addTextToPrinter(printer, twoColumns(
                "VATABLE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVatable_sales()))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VAT AMOUNT",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_amount()))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


        addTextToPrinter(printer, twoColumns(
                "VAT-EXEMPT SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getVat_exempt_sales()))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


        addTextToPrinter(printer, twoColumns(
                "ZERO-RATED SALES",
                "0.00",
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addPrinterSpace(1, printer);

        addTextToPrinter(printer, twoColumns(
                "SUB TOTAL",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getGross_sales()))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "AMOUNT DUE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(amountDue)),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


        Double payments = 0.00;
        List<Integer> tmpArray = new ArrayList<>();
        String pymntType = "";
        for (Payments pym : transactionCompleteDetails.paymentsList) {
            if (!tmpArray.contains(pym.getCore_id())) {
                tmpArray.add(pym.getCore_id());
                pymntType = pym.getName();
            }
            payments += Utils.roundedOffTwoDecimal(pym.getAmount());
        }

        addTextToPrinter(printer, twoColumns(
                "TENDERED",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(payments))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "CHANGE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffTwoDecimal(transactionCompleteDetails.transactions.getChange()))),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addPrinterSpace(1, printer);

        addTextToPrinter(printer, twoColumns(
                "PAYMENT TYPE",
                tmpArray.size() > 1 ? "MULTIPLE" : pymntType
                ,
                40,
                2,context), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addPrinterSpace(1, printer);

        addTextToPrinter(printer, "SOLD TO", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "NAME:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
        addTextToPrinter(printer, "ADDRESS:________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
        addTextToPrinter(printer, "TIN#:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
        addTextToPrinter(printer, "BUSINESS STYLE:_________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);


        addPrinterSpace(1, printer);
        addTextToPrinter(printer, "THIS SERVES AS YOUR", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, "OFFICIAL RECEIPT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

        addTextToPrinter(printer, "THANK YOU COME AGAIN", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);


        PrinterUtils.addFooterToPrinter(printer);

        try {

            printer.addCut(Printer.CUT_FEED);
            if (printer.getStatus().getConnection() == 1) {
                printer.sendData(Printer.PARAM_DEFAULT);
                printer.clearCommandBuffer();
            }

        } catch (Epos2Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
