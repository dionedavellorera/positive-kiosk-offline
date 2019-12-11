package com.nerdvana.positiveoffline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.functions.PrinterFunctions;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintJobModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.printer.SPrinter;
import com.nerdvana.positiveoffline.printer.SStarPort;
import com.nerdvana.positiveoffline.printjobasync.CutOffAsync;
import com.nerdvana.positiveoffline.printjobasync.EndOfDayAsync;
import com.nerdvana.positiveoffline.printjobasync.PrintReceiptAsync;
import com.nerdvana.positiveoffline.view.checkoutmenu.LeftFrameFragment;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.view.posmenu.BottomFrameFragment;
import com.nerdvana.positiveoffline.view.productsmenu.RightFrameFragment;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.stario.StarResultCode;
import com.starmicronics.starioextension.ConnectionCallback;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;
import com.starmicronics.starioextension.StarIoExtManager;
import com.starmicronics.starioextension.StarIoExtManagerListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.epson.epsonio.DevType.TCP;

public class MainActivity extends AppCompatActivity implements AsyncFinishCallBack {


    private UserViewModel userViewModel;

    private DataSyncViewModel dataSyncViewModel;

    private List<PrintJobModel> myPrintJobs;

    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort starIoPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openFragment(R.id.bottomFrame, new BottomFrameFragment());
        openFragment(R.id.leftFrame, new LeftFrameFragment());
        openFragment(R.id.rightFrame, new RightFrameFragment());
        myPrintJobs = new ArrayList<>();
        initUserViewModel();
        initDataSyncViewModel();
        initILocalizeReceipts();


//        try {
//            List<PortInfo> portList = StarIOPort.searchPrinter("BT:");
//            for (PortInfo port : portList) {
//                StarIOPort poty = StarIOPort.getPort("BT:" + port.getMacAddress(), "", 10000, MainActivity.this);
//
//                if (!poty.retreiveStatus().offline) {
//                    // Print end monitoring -Start
//                    StarPrinterStatus status = poty.beginCheckedBlock();
//
//                    ILocalizeReceipts dsdsadsa = new ILocalizeReceipts() {
//                        @Override
//                        public void append2inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public void append3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public void append4inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public Bitmap create2inchRasterReceiptImage() {
//                            return null;
//                        }
//
//                        @Override
//                        public Bitmap create3inchRasterReceiptImage() {
//                            return null;
//                        }
//
//                        @Override
//                        public Bitmap create4inchRasterReceiptImage() {
//                            return null;
//                        }
//
//                        @Override
//                        public Bitmap createCouponImage(Resources resources) {
//                            return null;
//                        }
//
//                        @Override
//                        public Bitmap createEscPos3inchRasterReceiptImage() {
//                            return null;
//                        }
//
//                        @Override
//                        public void appendEscPos3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public void appendDotImpact3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public Bitmap createSk12inchRasterReceiptImage() {
//                            return null;
//                        }
//
//                        @Override
//                        public void appendSk12inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public void appendTextLabelData(ICommandBuilder builder, boolean utf8) {
//
//                        }
//
//                        @Override
//                        public String createPasteTextLabelString() {
//                            return null;
//                        }
//
//                        @Override
//                        public void appendPasteTextLabelData(ICommandBuilder builder, String pasteText, boolean utf8) {
//
//                        }
//                    };
//                    byte[] command = PrinterFunctions.createTextReceiptData(StarIoExt.Emulation.StarPRNT, dsdsadsa, false);
//
//                    // Send print data
//                    poty.writePort(command, 0, command.length);
//                    // Print end monitoring -Endt
//                    status = poty.endCheckedBlock();
//
//
//                }
//            }
//
//
//        }
//        catch (StarIOPortException e) {
//
//            Log.d("MYPORT_ERR", e.getLocalizedMessage());
//            //There was an error opening the port
//        }


    }

    private void initILocalizeReceipts() {
        iLocalizeReceipts = new ILocalizeReceipts() {
            @Override
            public void append2inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public void append3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public void append4inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public Bitmap create2inchRasterReceiptImage() {
                return null;
            }

            @Override
            public Bitmap create3inchRasterReceiptImage() {
                return null;
            }

            @Override
            public Bitmap create4inchRasterReceiptImage() {
                return null;
            }

            @Override
            public Bitmap createCouponImage(Resources resources) {
                return null;
            }

            @Override
            public Bitmap createEscPos3inchRasterReceiptImage() {
                return null;
            }

            @Override
            public void appendEscPos3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public void appendDotImpact3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public Bitmap createSk12inchRasterReceiptImage() {
                return null;
            }

            @Override
            public void appendSk12inchTextReceiptData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public void appendTextLabelData(ICommandBuilder builder, boolean utf8) {

            }

            @Override
            public String createPasteTextLabelString() {
                return null;
            }

            @Override
            public void appendPasteTextLabelData(ICommandBuilder builder, String pasteText, boolean utf8) {

            }
        };
    }


    private void initDataSyncViewModel() {
        dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
    }

    private void initUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }


    private void openFragment(int container, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }


    @Subscribe
    public void menuClicked(ButtonsModel buttonsModel) throws ExecutionException, InterruptedException {
        switch (buttonsModel.getId()) {
            case 997://LOGOUT
                logoutUser();
                openLoginPage();
                break;
            case 134://SYNC DATA
                startSyncActivity(AppConstants.MAIN_ACTIVITY);
                break;
        }
    }

    private void startSyncActivity(String origin) {
        Intent syncIntent = new Intent(MainActivity.this, SyncActivity.class);
        syncIntent.putExtra(AppConstants.ORIGIN, origin);
        startActivity(syncIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        loadPrinter();
    }

    private void loadPrinter() {
        if (SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.EPSON))) {
            try {
                new SPrinter(
                        dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                        dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                        getApplicationContext()
                );
            } catch (Exception e) {
                Log.d("PIPIPI-EPSON-ERR", e.getLocalizedMessage());
            }
        } else if (SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.STAR_PRINTER))){
            if (starIoPort == null) {
                try {
                    new SStarPort(MainActivity.this);
                    starIoPort = SStarPort.getStarIOPort();
                starIoPort = StarIOPort.getPort(SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MANUALLY), "", 30000, MainActivity.this);
                } catch (StarIOPortException e) {
                    Log.d("PIPIPI-STAR-ERR", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }


        } else {
            Toast.makeText(MainActivity.this, "No printer set", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    private void logoutUser() throws ExecutionException, InterruptedException{
        User user = userViewModel.searchLoggedInUser().get(0);
        user.setIs_logged_in(false);
        userViewModel.update(user);
    }

    private void openLoginPage() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


    @Subscribe
    public void print(PrintModel printModel) {



        switch (printModel.getType()) {
            case "PRINT_RECEIPT":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, starIoPort), "print_receipt");
                break;
            case "PRINT_XREAD":
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, starIoPort), "print_xread");
                break;
            case "PRINT_ZREAD":
                addAsync(new EndOfDayAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, starIoPort), "print_zread");
                break;
        }

    }

    private void addAsync(AsyncTask asyncTask, String taskName) {
        if (myPrintJobs.size() < 1) {
            myPrintJobs.add(new PrintJobModel(taskName, asyncTask));
            runTask(taskName, asyncTask);
        } else {
            myPrintJobs.add(new PrintJobModel(taskName, asyncTask));
        }
    }

    private void runTask(String taskName, AsyncTask asyncTask) {

        switch (taskName) {
            case "print_receipt":
                PrintReceiptAsync printReceiptAsync = (PrintReceiptAsync) asyncTask;
                printReceiptAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_xread":
                CutOffAsync offAsync = (CutOffAsync) asyncTask;
                offAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_zread":
                EndOfDayAsync endOfDayAsync = (EndOfDayAsync) asyncTask;
                endOfDayAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
        }
    }


    @Override
    public void doneProcessing() {
        myPrintJobs.remove(0);
        if (myPrintJobs.size() > 0) {
            if (myPrintJobs.get(0).getTaskName().equalsIgnoreCase("print_receipt")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runTask(myPrintJobs.get(0).getTaskName(), myPrintJobs.get(0).getAsyncTask());
                    }
                }, 10000);

            } else {
                Handler hndl3 = new Handler(Looper.getMainLooper());
                hndl3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runTask(myPrintJobs.get(0).getTaskName(), myPrintJobs.get(0).getAsyncTask());
                    }
                }, 2000);


            }



        }
    }

    private final StarIoExtManagerListener mStarIoExtManagerListener = new StarIoExtManagerListener() {
        @Override
        public void onBarcodeDataReceive(byte[] barcodeData) {
//            String[] barcodeDataArray = new String(barcodeData).split("\n");
//
//            for(String data:barcodeDataArray) {
//                addItem(data);
//            }
        }

        @Override
        public void onPrinterImpossible() {
//            mComment.setText("Printer Impossible.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onPrinterOnline() {
//            mComment.setText("Printer Online.");
//
//            mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onPrinterOffline() {
//          mComment.setText("Printer Offline.");
//
//          mComment.setTextColor(Color.RED);
        }

        @Override
        public void onPrinterPaperReady() {
//          mComment.setText("Printer Paper Ready.");
//
//          mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onPrinterPaperNearEmpty() {
//            mComment.setText("Printer Paper Near Empty.");
//
//            mComment.setTextColor(0xffffa500);     // Orange
        }

        @Override
        public void onPrinterPaperEmpty() {
//            mComment.setText("Printer Paper Empty.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onPrinterCoverOpen() {
//            mComment.setText("Printer Cover Open.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onPrinterCoverClose() {
//          mComment.setText("Printer Cover Close.");
//
//          mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onCashDrawerOpen() {
//            mComment.setText("Cash Drawer Open.");
//
//            mComment.setTextColor(Color.MAGENTA);
        }

        @Override
        public void onCashDrawerClose() {
//            mComment.setText("Cash Drawer Close.");
//
//            mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onBarcodeReaderImpossible() {
//            mComment.setText("Barcode Reader Impossible.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onBarcodeReaderConnect() {
//            mComment.setText("Barcode Reader Connect.");
//
//            mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onBarcodeReaderDisconnect() {
//            mComment.setText("Barcode Reader Disconnect.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onAccessoryConnectSuccess() {
//            mComment.setText("Accessory Connect Success.");
//
//            mComment.setTextColor(Color.BLUE);
        }

        @Override
        public void onAccessoryConnectFailure() {
//            mComment.setText("Accessory Connect Failure.");
//
//            mComment.setTextColor(Color.RED);
        }

        @Override
        public void onAccessoryDisconnect() {
//            mComment.setText("Accessory Disconnect.");
//
//            mComment.setTextColor(Color.RED);
        }
    };

    private final ConnectionCallback mConnectionCallback = new ConnectionCallback() {
        @Override
        public void onConnected(boolean result, int resultCode) {
//            if (!mIsForeground) {
//                return;
//            }
//
//            if (mProgressDialog != null) {
//                mProgressDialog.dismiss();
//            }

            if (!result) {
                String message;

                if (resultCode == StarResultCode.FAILURE_IN_USE) {
                    message = "Check the device. (In use)\nThen touch up the Refresh button.";
                }
                else {
                    message = "Check the device. (Power and Bluetooth pairing)\nThen touch up the Refresh button.";
                }

//                mComment.setText(message);
//                mComment.setTextColor(Color.RED);

//                CommonAlertDialogFragment dialog = CommonAlertDialogFragment.newInstance(OPEN_FAILURE_DIALOG);
//                dialog.setTitle("Communication Result");
//                dialog.setMessage(Communication.getCommunicationResultMessage(new CommunicationResult(Result.ErrorOpenPort, resultCode)));
//                dialog.setPositiveButton("OK");
//                dialog.show(getChildFragmentManager());
            }
        }

        @Override
        public void onDisconnected() {
            // do nothing
        }
    };



}
