package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.StarIoExt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class CheatAsync extends AsyncTask<Void, Void, Void> {

    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;
    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort port = null;

    private boolean isReprint = false;


    public CheatAsync(PrintModel printModel, Context context,
                         AsyncFinishCallBack asyncFinishCallBack,
                         DataSyncViewModel dataSyncViewModel,
                         ILocalizeReceipts iLocalizeReceipts,
                         StarIOPort starIOPort, boolean isReprint) {
        this.context = context;
        this.printModel = printModel;
        this.asyncFinishCallBack = asyncFinishCallBack;
        this.dataSyncViewModel = dataSyncViewModel;
        this.iLocalizeReceipts = iLocalizeReceipts;
        this.port = starIOPort;
        this.isReprint = isReprint;
    }



    @Override
    protected Void doInBackground(Void... voids) {



//        TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);

        if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.EPSON))) {

            try {

                printer = new Printer(
                        dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                        dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                        context
                );
                printer.addPulse(Printer.DRAWER_HIGH, Printer.PULSE_100);
            } catch (Epos2Exception e) {
                try {
                    printer.disconnect();
                } catch (Epos2Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (InterruptedException e) {
                try {
                    printer.disconnect();
                } catch (Epos2Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (ExecutionException e) {
                try {
                    printer.disconnect();
                } catch (Epos2Exception e1) {
                    e1.printStackTrace();
                }
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
                                try {
                                    printer.disconnect();
                                } catch (Epos2Exception e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });


//            PrinterUtils.connect(context, printer);

            if (!TextUtils.isEmpty(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY))) {
                try {
                    if (printer != null) {
                        printer.connect(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), Printer.PARAM_DEFAULT);
                    }
                } catch (Epos2Exception e) {
                    try {
                        asyncFinishCallBack.retryProcessing();
                        printer.disconnect();
                    } catch (Epos2Exception e1) {
                        asyncFinishCallBack.retryProcessing();
                        e1.printStackTrace();
                    }
                    asyncFinishCallBack.retryProcessing();
                    e.printStackTrace();
                }
            }


            addPrinterSpace(1, printer);


            addTextToPrinter(printer, "HI I AM JUST A TEST", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

            addPrinterSpace(1, printer);
//            addTextToPrinter(printer, "Citysuper Incorporated", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "EDSA cor Mindanao Ave Ext", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer," Quezon City", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "VAT REG TIN NO: 205-412-358-000" , Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "POS381-SN:41-DLA83", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1,1 ,1 );
//            addTextToPrinter(printer, "MIN#16102713390090828", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1,1);
//
//
//
//            addPrinterSpace(1, printer);
//
//
//            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//            addTextToPrinter(printer, "01/18/2020 71:130  #392   REG", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//            addTextToPrinter(printer, twoColumns(
//                    "2402 CHNCLLR8005 PANTS",
//                    "2    720.00",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "",
//                    "-----------",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "",
//                    "  1,440.00  ",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addPrinterSpace(1, printer );
//            addTextToPrinter(printer, twoColumns(
//                    "TOTAL",
//                    "  1,440.00  ",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "EPS",
//                    "  1,440.00  ",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "Card No:",
//                    "  ************6068  ",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "ApprCode",
//                    "  531469  ",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addPrinterSpace(1, printer);
//
//
//            addTextToPrinter(printer, twoColumns(
//                    "VATable",
//                    "1285.71",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addTextToPrinter(printer, twoColumns(
//                    "VAT-Exempt",
//                    "0.00",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//
//            addTextToPrinter(printer, twoColumns(
//                    "VAT Zero-Rated",
//                    "0.00",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//
//            addTextToPrinter(printer, twoColumns(
//                    "VAT 12%",
//                    "1285.71",
//                    40,
//                    2,
//                    context)
//                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//
//            addPrinterSpace(1, printer);
//
//
//            addTextToPrinter(printer, "CHECKER: 07201719-Bernadeth Balgimino", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1,1);
//            addTextToPrinter(printer, "Cashier: 07187219-Ronalyn Felicia Villoria", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1,1);
//            addTextToPrinter(printer, "Transaction# 0202 38100064781", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1,1);
//            addTextToPrinter(printer, "SI# 38100064792", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1,1);
//            addTextToPrinter(printer, "LM# 020238100064391", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1,1);
//
//
//            addTextToPrinter(printer, "SOLD TO:", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
//            addTextToPrinter(printer, "TIN: 009772500000", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
//            addTextToPrinter(printer, "NAME: NERDVANA CORP.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
//            addTextToPrinter(printer, "ADDRESS: 1 CANLEY RD. BRGY. BAGONG ILOG PASIG CITY", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
//            addTextToPrinter(printer, "BUS STYLE:______________________", Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
//
//
//
//
//
//
//            addPrinterSpace(1, printer);
//
//            addTextToPrinter(printer, "Thank you for shopping at", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "LANDMARK. PLEASE COME AGAIN.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "THIS SERVES AS YOUR SALES INVOICE.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//
//            addPrinterSpace(1, printer);
//
//            addTextToPrinter(printer, "ANSI Information Systems, Inc.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Tytana St., Manila", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "VAT Reg TIN: 000-330-515-000", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "ACCR # 030-000330515-00000769642", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Date Issued: 12/12/2013", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Valid Until: 07/31/2020", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "PTU No.FP102016-116-0102519-00000", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Date Issued: 10/27/2016", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "Valid Until: 10/26/2021", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "THIS INVOICE/RECEIPT SHALL BE VALID", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "FOR FIVE(5) YEARS FROM THE DATE OF THE", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//            addTextToPrinter(printer, "PERMIT TO USE.", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
//

            try {

                printer.addCut(Printer.CUT_FEED);

                printer.sendData(Printer.PARAM_DEFAULT);
                printer.clearCommandBuffer();


            } catch (Epos2Exception e) {
                try {
                    printer.disconnect();
                } catch (Epos2Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        } else if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.STAR_PRINTER))){
            try {
                final StarIOPort starIOPort =  StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (starIOPort != null) {
                            try {
                                if (!starIOPort.retreiveStatus().offline) {
                                    byte[] command = PrinterFunctions.createTextReceiptData(
                                            StarIoExt.Emulation.StarPRNT,
                                            iLocalizeReceipts,
                                            false,
                                            "HI I AM JUST A TEST",
                                            true);
                                    starIOPort.writePort(command, 0, command.length);
                                    starIOPort.endCheckedBlock();
                                    asyncFinishCallBack.doneProcessing();
                                } else {
                                    asyncFinishCallBack.doneProcessing();
//                                    asyncFinishCallBack.error("PRINTER IS OFFLINE");
                                }
                            } catch (StarIOPortException e) {
                                asyncFinishCallBack.doneProcessing();
//                                asyncFinishCallBack.error("PRINTER IS OFFLINE");
                            }
                        } else {
                            asyncFinishCallBack.doneProcessing();
                            asyncFinishCallBack.error("PRINTER NOT CONNECTED");
                        }
                    }
                }, 500);
            } catch (StarIOPortException e) {
                asyncFinishCallBack.doneProcessing();
                asyncFinishCallBack.error("PRINTER NOT CONNECTED");
            }
        }







        return null;
    }


}
