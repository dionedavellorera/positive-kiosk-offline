package com.nerdvana.positiveoffline.printjobasync;

import android.content.Context;
import android.os.AsyncTask;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class CutOffAsync extends AsyncTask<Void, Void, Void> {

    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;
    public CutOffAsync(PrintModel printModel, Context context,
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


        CutOff cutOff = GsonHelper.getGson().fromJson(printModel.getData(), CutOff.class);


        PrinterUtils.addHeader(printModel, printer);
        addPrinterSpace(1, printer);
        addTextToPrinter(printer, "X READING", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
        addPrinterSpace(1, printer);

        addTextToPrinter(printer, "POSTING DATE:" + cutOff.getCreated_at(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
        addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addTextToPrinter(printer, "DESCRIPTION               VALUE", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
        addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "GROSS SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getGross_sales())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "NET SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getNet_sales())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VATABLE SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVatable_sales())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VAT EXEMPT SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVat_exempt_sales())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VAT DISCOUNT",
//                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getGross_sales())),
                PrinterUtils.returnWithTwoDecimal("0.00"),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VAT AMOUNT",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVat_amount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "ZERO RATED SALES",
                "0.00",
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "CASH SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_cash_payments())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "CARD SALES",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getTotal_card_payments())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "VOID",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getVoid_amount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "SENIOR",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getSeniorAmount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "SENIOR(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getSeniorCount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "PWD",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getPwdAmount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "PWD(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getPwdCount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "OTHERS",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getOthersAmount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

        addTextToPrinter(printer, twoColumns(
                "OTHERS(COUNT)",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(cutOff.getOthersCount())),
                40,
                2,
                context)
                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
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
