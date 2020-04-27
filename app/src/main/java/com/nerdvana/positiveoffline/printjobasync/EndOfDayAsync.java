package com.nerdvana.positiveoffline.printjobasync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.StarIoExt;

import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class EndOfDayAsync extends AsyncTask<Void, Void, Void> {

    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;

    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort port = null;
    private boolean isReprint;
    public EndOfDayAsync(PrintModel printModel, Context context,
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

        final EndOfDay endOfDay = GsonHelper.getGson().fromJson(printModel.getData(), EndOfDay.class);


        if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.EPSON))) {
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





            PrinterUtils.addHeader(printModel, printer);
            addPrinterSpace(1, printer);
            if (isReprint) {
                addTextToPrinter(printer, "Z READING(REPRINT)", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
            } else {
                addTextToPrinter(printer, "Z READING", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
            }

            addPrinterSpace(1, printer);

            addTextToPrinter(printer, "POSTING DATE:" + endOfDay.getTreg(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, "DESCRIPTION               VALUE", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "GROSS SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getGross_sales())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "NET SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getNet_sales())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "VATABLE SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVatable_sales())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "VAT EXEMPT SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVat_exempt_sales())),
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
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVat_amount())),
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
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_cash_payments())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "CARD SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_card_payments())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "ONLINE SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_online_payments())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "AR SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_ar_payments())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "MOB. PMT SALES",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_mobile_payments())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);



            addTextToPrinter(printer, twoColumns(
                    "VOID",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getVoid_amount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "SENIOR",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getSeniorAmount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "SENIOR(COUNT)",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getSeniorCount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "PWD",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getPwdAmount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "PWD(COUNT)",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getPwdCount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "OTHERS",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getOthersAmount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "OTHERS(COUNT)",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getOthersCount())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            if (endOfDay.getTotal_payout() > 0) {
                addTextToPrinter(printer, twoColumns(
                        "OTHERS(COUNT)",
                        "-" + PrinterUtils.returnWithTwoDecimal(String.valueOf(endOfDay.getTotal_payout())),
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            } else {
                addTextToPrinter(printer, twoColumns(
                        "PAYOUT",
                        "0.00",
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            }

            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, twoColumns(
                    "BEG. OR NO",
                    endOfDay.getBegOrNo(),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, twoColumns(
                    "ENDING OR NO",
                    endOfDay.getEndOrNo(),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, twoColumns(
                    "BEG BALANCE",
                    String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getBegSales())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, twoColumns(
                    "ENDING BALANCE",
                    String.valueOf(Utils.roundedOffTwoDecimal(endOfDay.getEndSales())),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addPrinterSpace(1, printer);


            addTextToPrinter(printer, "Z COUNTER:" + endOfDay.getId(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);

            addPrinterSpace(1, printer);


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
        } else if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.STAR_PRINTER))){

            try {
                final StarIOPort starIOPort =  StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                synchronized (starIOPort) {
                    if (starIOPort != null) {
                        try {
                            if (!starIOPort.retreiveStatus().offline) {
                                byte[] command = PrinterFunctions.createTextReceiptData(
                                        StarIoExt.Emulation.StarPRNT,
                                        iLocalizeReceipts,
                                        false,
                                        EJFileCreator.endOfDayString(endOfDay, context, isReprint),
                                        false);

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
            } catch (StarIOPortException e) {
                asyncFinishCallBack.doneProcessing();
                asyncFinishCallBack.error("PRINTER NOT CONNECTED");
            }



        }





        return null;
    }





}
