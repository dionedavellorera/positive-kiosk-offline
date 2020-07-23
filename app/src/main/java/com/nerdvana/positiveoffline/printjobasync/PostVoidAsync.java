package com.nerdvana.positiveoffline.printjobasync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.BusProvider;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.MainActivity;
import com.nerdvana.positiveoffline.PrinterPresenter;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.ThreadPoolManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.PrintingListModel;
import com.nerdvana.positiveoffline.model.SunmiPrinterModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.StarIoExt;
import com.sunmi.devicemanager.cons.Cons;
import com.sunmi.devicemanager.device.Device;
import com.sunmi.devicesdk.core.PrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class PostVoidAsync extends AsyncTask<Void, Void, Void> {
    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;
    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort port = null;

    private boolean isReprint = false;

    private PrinterPresenter printerPresenter;
    private SunmiPrinterService mSunmiPrintService;

    public PostVoidAsync(PrintModel printModel, Context context,
                             AsyncFinishCallBack asyncFinishCallBack,
                             DataSyncViewModel dataSyncViewModel,
                             ILocalizeReceipts iLocalizeReceipts,
                             StarIOPort starIOPort, boolean isReprint,
                         PrinterPresenter printerPresenter, SunmiPrinterService mSunmiPrintService) {
        this.context = context;
        this.printModel = printModel;
        this.asyncFinishCallBack = asyncFinishCallBack;
        this.dataSyncViewModel = dataSyncViewModel;
        this.iLocalizeReceipts = iLocalizeReceipts;
        this.port = starIOPort;
        this.isReprint = isReprint;

        this.printerPresenter = printerPresenter;
        this.mSunmiPrintService = mSunmiPrintService;

    }



    @Override
    protected Void doInBackground(Void... voids) {



        final TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);

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



            PrinterUtils.addHeader(printModel, printer);
            addPrinterSpace(1, printer);

            addTextToPrinter(printer, "VOID", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);


            if (transactionCompleteDetails.transactions.getHas_special() == 1) {

                if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT")) {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT(STORE COPY)", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
//                    BusProvider.getInstance().post(new PrintModel("PRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
                } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT")) {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT(STORE COPY)\nREPRINT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
                    BusProvider.getInstance().post(new PrintModel("REPRINT_RECEIPT_SPEC", GsonHelper.getGson().toJson(transactionCompleteDetails)));
                }

                if (printModel.getType().equalsIgnoreCase("PRINT_RECEIPT_SPEC")) {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT(CUSTOMER COPY)", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
                } else if (printModel.getType().equalsIgnoreCase("REPRINT_RECEIPT_SPEC")) {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT(CUSTOMER COPY)\nREPRINT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
                }
            } else {
                if (isReprint) {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT(REPRINT)", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
                } else {
                    addTextToPrinter(printer, "OFFICIAL RECEIPT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
                }
            }


            addPrinterSpace(1, printer);

            if (!TextUtils.isEmpty(transactionCompleteDetails.transactions.getTo_control_number())) {
                addTextToPrinter(printer, twoColumns(
                        "TO CTRL NO",
                        transactionCompleteDetails.transactions.getTo_control_number(),
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            }

            addTextToPrinter(printer, twoColumns(
                    "OR NO",
                    transactionCompleteDetails.transactions.getReceipt_number(),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, twoColumns(
                    "DATE",
                    transactionCompleteDetails.transactions.getTreg(),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            if (!TextUtils.isEmpty(transactionCompleteDetails.transactions.getRoom_number())) {
                addTextToPrinter(printer, twoColumns(
                        "TABLE",
                        transactionCompleteDetails.transactions.getRoom_number(),
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            }

            addPrinterSpace(1, printer);

            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, "QTY  DESCRIPTION          AMOUNT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            Double amountDue = 0.00;
            for (Orders orders : transactionCompleteDetails.ordersList) {
                if (!orders.getIs_void()) {
                    String qty = "";

                    qty += orders.getQty();

                    if (String.valueOf(orders.getQty()).length() < 4) {
                        for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
                            qty += " ";
                        }
                    }

                    String myString = "";
                    if (orders.getDiscountAmount() > 0) {
                        if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                            myString = "("+qty.trim()+")" +  "  " + Html.fromHtml(orders.getName_initials());
                        } else {
                            myString = "("+qty.trim()+")" +  " " + Html.fromHtml(orders.getName_initials());
                        }
                    } else {
                        if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                            myString = qty +  "  " + Html.fromHtml(orders.getName_initials());
                        } else {
                            myString = qty +  " " + Html.fromHtml(orders.getName_initials());
                        }
                    }

                    addTextToPrinter(printer, twoColumns(
                            myString,
                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
                            40,
                            2,
                            context)
                            ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

//                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
//                        addTextToPrinter(printer, twoColumns(
//                                orders.getDiscountAmount() <= 0 ? "(" + qty +  "  " + orders.getName_initials() +")": qty +  "  " + orders.getName_initials(),
//                                PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    } else {
//                        addTextToPrinter(printer, twoColumns(
//                                orders.getDiscountAmount() <= 0 ? "(" + qty +  " " + orders.getName_initials() + ")" : qty +  " " + orders.getName_initials(),
//                                PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    }

                    if (orders.getQty() > 1) {
                        addTextToPrinter(printer, twoColumns(
                                "("+String.valueOf(Utils.roundedOffTwoDecimal(orders.getAmount())) + ")",
                                "",
                                40,
                                2,
                                context)
                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
                    }


//                    if (orders.getDiscountAmount() > 0) {
//                        addTextToPrinter(printer, twoColumns(
//                                "LESS",
//                                "-" +PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    }





                    if (orders.getVatExempt() > 0) {
                        amountDue += orders.getOriginal_amount() * orders.getQty();
                    } else {
                        amountDue += orders.getAmount() * orders.getQty();
                    }
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
                    if (!postedDiscounts.getIs_void()) {
                        addTextToPrinter(printer, twoColumns(
                                postedDiscounts.getDiscount_name(),
                                postedDiscounts.getCard_number(),
                                40,
                                2,
                                context)
                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


                        if (postedDiscounts.getDiscount_name().equalsIgnoreCase("PWD") ||
                                postedDiscounts.getDiscount_name().equalsIgnoreCase("SENIOR CITIZEN")) {

                            addTextToPrinter(printer, "NAME:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                            addTextToPrinter(printer, "ADDRESS:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                            addTextToPrinter(printer, "SIGNATURE:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);

                        }

                        addTextToPrinter(printer, twoColumns(
                                "LESS ",
                                "",
                                40,
                                2,
                                context)
                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


                        addTextToPrinter(printer, twoColumns(
                                (postedDiscounts.getIs_percentage() ? postedDiscounts.getDiscount_value() + "%" : String.valueOf(Utils.roundedOffTwoDecimal(postedDiscounts.getDiscount_value()))),
                                String.valueOf((Utils.roundedOffTwoDecimal(postedDiscounts.getAmount()))),
                                40,
                                2,
                                context)
                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

                    }
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

            if (transactionCompleteDetails.transactions.getService_charge_value() > 0) {
                addTextToPrinter(printer, twoColumns(
                        "SERVICE CHARGE",
                        PrinterUtils.returnWithTwoDecimal(String.valueOf(transactionCompleteDetails.transactions.getService_charge_value())),
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            } else {
                addTextToPrinter(printer, twoColumns(
                        "SERVICE CHARGE",
                        "0.00",
                        40,
                        2,
                        context)
                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            }


            addPrinterSpace(1, printer);

            addTextToPrinter(printer, twoColumns(
                    "SUB TOTAL",
                    PrinterUtils.returnWithTwoDecimal(
                            String.valueOf(
                                    Utils.roundedOffTwoDecimal(
                                            transactionCompleteDetails.transactions.getGross_sales()
                                    ) +
                                            Utils.roundedOffTwoDecimal(
                                                    transactionCompleteDetails.transactions.getService_charge_value()
                                            ) +
                                            Utils.roundedOffTwoDecimal(
                                                    transactionCompleteDetails.transactions.getDiscount_amount()
                                            )
                            )),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "AMOUNT DUE",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(amountDue) + Utils.roundedOffTwoDecimal(
                            transactionCompleteDetails.transactions.getService_charge_value()
                    )),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


            Double payments = 0.00;
            List<Integer> tmpArray = new ArrayList<>();
            String pymntType = "";
            String cardType = "";
            String cardNumber = "";
            for (Payments pym : transactionCompleteDetails.paymentsList) {
                if (!pym.getIs_void()) {
                    if (!tmpArray.contains(pym.getCore_id())) {
                        tmpArray.add(pym.getCore_id());
                        pymntType = pym.getName();
                    }
                    if (pym.getCore_id() != 2) {
                        payments += Utils.roundedOffTwoDecimal(pym.getAmount());
                    }

                    if (pym.getCore_id() == 2) {
                        try {
                            JSONObject jsonObject = new JSONObject(pym.getOther_data());
                            cardType = jsonObject.getString("card_type");
                            cardNumber = jsonObject.getString("card_number");
                            int starCount = 0;
                            if (cardNumber.length() < 3) {
                                cardNumber = jsonObject.getString("card_number");
                            } else {
                                starCount = cardNumber.length() - 3;
                                String starString = new String(new char[starCount]).replace("\0", "*");
                                cardNumber = cardNumber.substring(0, cardNumber.length() - starCount) + starString;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

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

            if (tmpArray.size() > 1) {
                addTextToPrinter(printer, twoColumns(
                        "PAYMENT TYPE",
                        "MULTIPLE"
                        ,
                        40,
                        2,context), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            } else {
                addTextToPrinter(printer, twoColumns(
                        "PAYMENT TYPE",
                        pymntType
                        ,
                        40,
                        2,context), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
                if (pymntType.equalsIgnoreCase("card")) {
                    addTextToPrinter(printer, twoColumns(
                            cardType,
                            cardNumber
                            ,
                            40,
                            2,context), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
                }


            }




            addPrinterSpace(1, printer);

            addTextToPrinter(printer, "SOLD TO", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

            if (transactionCompleteDetails.orDetails != null) {
                addTextToPrinter(printer, "NAME:" + transactionCompleteDetails.orDetails.getName(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "TIN#:" + transactionCompleteDetails.orDetails.getTin_number(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
            } else {
                addTextToPrinter(printer, "NAME:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "ADDRESS:________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "TIN#:___________________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
                addTextToPrinter(printer, "BUSINESS STYLE:_________________", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);
            }


            if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("delivery")) {
                addTextToPrinter(printer, "CONTROL#", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
                addTextToPrinter(printer, transactionCompleteDetails.transactions.getControl_number(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
                addTextToPrinter(printer, "DELIVERY FOR", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
                addTextToPrinter(printer, transactionCompleteDetails.transactions.getDelivery_to(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
                addTextToPrinter(printer, "DELIVERY ADDRESS", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
                addTextToPrinter(printer, transactionCompleteDetails.transactions.getDelivery_address(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            }



            addPrinterSpace(1, printer);
            addTextToPrinter(printer, "THIS SERVES AS YOUR", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);
            addTextToPrinter(printer, "OFFICIAL RECEIPT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);

            addTextToPrinter(printer, "THANK YOU COME AGAIN", Printer.FALSE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 1);


            PrinterUtils.addFooterToPrinter(printer);

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
                synchronized (starIOPort) {
                    if (starIOPort != null) {
                        try {
                            if (!starIOPort.retreiveStatus().offline) {
                                byte[] command = PrinterFunctions.createTextReceiptData(
                                        StarIoExt.Emulation.StarPRNT,
                                        iLocalizeReceipts,
                                        false,
                                        EJFileCreator.postVoidString(transactionCompleteDetails, context, isReprint, printModel),
                                        false);

                                starIOPort.writePort(command, 0, command.length);
                                starIOPort.endCheckedBlock();
                                asyncFinishCallBack.doneProcessing();
                            } else {
                                asyncFinishCallBack.doneProcessing();
                                asyncFinishCallBack.error("PRINTER IS OFFLINE");
                            }
                        } catch (StarIOPortException e) {
                            asyncFinishCallBack.doneProcessing();
                            asyncFinishCallBack.error("PRINTER IS OFFLINE");
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




        } else if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY).equalsIgnoreCase("sunmi")) {
            if (printerPresenter == null) {
                printerPresenter = new PrinterPresenter(context, mSunmiPrintService);
            }
            String finalString = "";

            TransactionCompleteDetails postVoidDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);
            finalString = EJFileCreator.postVoidString(postVoidDetails, context, false, printModel);

            TypeToken<List<PrintingListModel>> myToken = new TypeToken<List<PrintingListModel>>() {};
            List<PrintingListModel> pOutList = GsonHelper.getGson().fromJson(SharedPreferenceManager.getString(context, AppConstants.PRINTER_PREFS), myToken.getType());
            PrintingListModel tmpLstModel = null;
            for (PrintingListModel list : pOutList) {
                if (list.getType().equalsIgnoreCase(printModel.getType())) {
                    String finalString1 = finalString;
                    ThreadPoolManager.getsInstance().execute(() -> {
                        for (PrintingListModel.SelectedPrinterData data : list.getSelectedPrinterList()) {
                            if (data.getId().equalsIgnoreCase(SunmiPrinterModel.PRINTER_BUILT_IN)) {
                                printerPresenter.printNormal(finalString1);
                            }
                        }
                        List<Device> deviceList = PrinterManager.getInstance().getPrinterDevice();
                        if (deviceList == null || deviceList.isEmpty()) return;
                        for (Device device : deviceList) {
                            if (device.type == Cons.Type.PRINT && device.connectType == Cons.ConT.INNER) {
                                continue;
                            }
                            if (list.getSelectedPrinterList().size() > 0) {
                                for (PrintingListModel.SelectedPrinterData data : list.getSelectedPrinterList()) {
                                    if (data.getId().equalsIgnoreCase(device.getId())) {
                                        printerPresenter.printByDeviceManager(device, finalString1);
                                    }
                                }

                            }

                        }
                    });
                }
            }

            asyncFinishCallBack.doneProcessing();
        }







        return null;
    }

}
