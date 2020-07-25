package com.nerdvana.positiveoffline.printjobasync;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.PrinterPresenter;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.ThreadPoolManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.PrintingListModel;
import com.nerdvana.positiveoffline.model.SunmiPrinterModel;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.starioextension.StarIoExt;
import com.sunmi.devicemanager.cons.Cons;
import com.sunmi.devicemanager.device.Device;
import com.sunmi.devicesdk.core.PrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class FosAsync extends AsyncTask<Void, Void, Void> {
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


    public FosAsync(PrintModel printModel, Context context,
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


        TypeToken<List<Orders>> token = new TypeToken<List<Orders>>() {};
        List<Orders> ordersList = GsonHelper.getGson().fromJson(printModel.getData(), token.getType());


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

            addTextToPrinter(printer, "ORDER SLIP", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
            if (!TextUtils.isEmpty(printModel.getAdditionalData())) {
                addTextToPrinter(printer, printModel.getAdditionalData(), Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
            }



            addPrinterSpace(1, printer);

//            for (Orders orders : ordersList) {
//                addTextToPrinter(printer, twoColumns(
//                        orders.getName(),
//                        PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getAmount())),
//                        40,
//                        2,
//                        context)
//                        ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//            }
            double totalAmount = 0.00;

            addTextToPrinter(printer, "QTY  DESCRIPTION          AMOUNT", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
            addTextToPrinter(printer, new String(new char[Integer.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT))]).replace("\0", "-"), Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            for (Orders orders : ordersList) {
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
                            myString = "("+qty.trim()+")" +  "  " + Html.fromHtml(orders.getName());
                        } else {
                            myString = "("+qty.trim()+")" +  " " + Html.fromHtml(orders.getName());
                        }
                    } else {
                        if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                            myString = qty +  "  " + Html.fromHtml(orders.getName());
                        } else {
                            myString = qty +  " " + Html.fromHtml(orders.getName());
                        }
                    }


                    addTextToPrinter(printer, twoColumns(
                            myString,
                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
                            40,
                            2,
                            context)
                            ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

                    if (orders.getQty() > 1) {

                        addTextToPrinter(printer, twoColumns(
                                "("+String.valueOf(Utils.roundedOffFourDecimal(orders.getOriginal_amount())) + ")",
                                "",
                                40,
                                2,
                                context)
                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);


                    }


                    totalAmount += orders.getOriginal_amount()  * orders.getQty();


                }

//                if (!orders.getIs_void()) {
//                    totalAmount += orders.getAmount() * orders.getQty();
//                    String qty = "";
//
//                    qty += orders.getQty();
//
//                    if (String.valueOf(orders.getQty()).length() < 4) {
//                        for (int i = 0; i < 4 - String.valueOf(orders.getQty()).length(); i++) {
//                            qty += " ";
//                        }
//                    }
//
//                    if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
//                        addTextToPrinter(printer, twoColumns(
//                                qty +  "  " + Html.fromHtml(orders.getName()),
//                                PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    } else {
//                        addTextToPrinter(printer, twoColumns(
//                                qty +  " " + Html.fromHtml(orders.getName()),
//                                PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    }
//
//                    if (orders.getQty() > 1) {
//                        addTextToPrinter(printer, twoColumns(
//                                "("+String.valueOf(Utils.roundedOffFourDecimal(orders.getAmount())) + ")",
//                                "",
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    }
//
//                    if (orders.getDiscountAmount() > 0) {
//                        addTextToPrinter(printer, twoColumns(
//                                "LESS",
//                                "-" +PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount())),
//                                40,
//                                2,
//                                context)
//                                ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);
//                    }
//                }
            }

            addTextToPrinter(printer, "", Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "TOTAL",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(totalAmount)),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);

            addTextToPrinter(printer, twoColumns(
                    "PRINTED DATE",
                    Utils.getDateTimeToday(),
                    40,
                    2,
                    context)
                    ,Printer.FALSE, Printer.FALSE, Printer.ALIGN_LEFT, 1,1,1);





            addPrinterSpace(1, printer);

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
                                        EJFileCreator.fosString(ordersList, context, printModel.getAdditionalData()),
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
        } else if (SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY).equalsIgnoreCase("sunmi")) {
            if (printerPresenter == null) {
                printerPresenter = new PrinterPresenter(context, mSunmiPrintService);
            }
            String finalString = "";

            finalString = EJFileCreator.fosString(ordersList, context, printModel.getAdditionalData());

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
