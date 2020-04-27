package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;
import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.IntransitAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.starioextension.StarIoExt;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.addPrinterSpace;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.addTextToPrinter;
import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class IntransitAsync extends AsyncTask<Void, Void, Void> {

    private PrintModel printModel;
    private Context context;;
    private AsyncFinishCallBack asyncFinishCallBack;
    private DataSyncViewModel dataSyncViewModel;
    private Printer printer;
    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort port = null;

    private boolean isReprint = false;


    public IntransitAsync(PrintModel printModel, Context context,
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



//        final TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);

        TypeToken<List<TransactionWithOrders>> token = new TypeToken<List<TransactionWithOrders>>() {};
        List<TransactionWithOrders> transactions = GsonHelper.getGson().fromJson(printModel.getData(), token.getType());


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


            addTextToPrinter(printer, "INTRANSIT SLIP", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);


            List<String> t = new ArrayList<>();
            t.add("I");
            t.add("II");
            t.add("III");
            t.add("IV");
            t.add("V");
            addTextToPrinter(printer, intransitReceipt(t), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);



            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            for (TransactionWithOrders tr : transactions) {
                List<String> temp = new ArrayList<>();

                if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
                    DateTime dt = formatter.parseDateTime(tr.transactions.getSaved_at());
                    temp.add(tr.transactions.getTrans_name());
                    temp.add(String.valueOf(tr.ordersList.size()));
                    String dateONly = tr.transactions.getSaved_at().split(" ")[0];
                    temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                    temp.add(String.valueOf(tr.transactions.getGross_sales()));
                    temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");
                } else {
                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
                        DateTime dt = formatter.parseDateTime(tr.transactions.getSaved_at());
                        temp.add(tr.transactions.getTrans_name());
                        temp.add(String.valueOf(tr.ordersList.size()));
                        String dateONly = tr.transactions.getSaved_at().split(" ")[0];
                        temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                        temp.add(String.valueOf(tr.transactions.getGross_sales()));
                        temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                        //
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {

                        DateTime dt = formatter.parseDateTime(tr.transactions.getCheck_in_time());
                        temp.add(tr.transactions.getTrans_name());
                        temp.add(String.valueOf(tr.ordersList.size()));
                        String dateONly = tr.transactions.getCheck_in_time().split(" ")[0];
                        temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                        temp.add(String.valueOf(tr.transactions.getGross_sales()));
                        temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");


                    }
                }




                addTextToPrinter(printer, intransitReceipt(temp), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);

            }


            addPrinterSpace(1, printer);
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
                                        EJFileCreator.intransitString(transactions, context),
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
        }







        return null;
    }


    private String intransitReceipt(List<String> details) {
        String finalString = "";
        float maxColumn = Float.valueOf(SharedPreferenceManager.getString(context, AppConstants.MAX_COLUMN_COUNT));
        int perColumn = (int)maxColumn / details.size();

        for (int i = 0; i < details.size(); i++) {
            if (details.size() >= perColumn) {
                finalString += details.get(i);
            } else {
                finalString += details.get(i);
                float temp = perColumn - details.get(i).length();
                for (int j = 0; j < temp; j++) {
                    finalString += " ";
                }
            }
        }
        return finalString;
    }




}
