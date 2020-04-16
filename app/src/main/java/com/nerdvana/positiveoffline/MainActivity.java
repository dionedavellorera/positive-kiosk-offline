package com.nerdvana.positiveoffline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdvana.positiveoffline.apirequests.TestRequest;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.background.IntransitAsync;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.model.ShiftUpdateModel;
import com.nerdvana.positiveoffline.model.TimerUpdateModel;
import com.nerdvana.positiveoffline.printjobasync.BackoutAsync;
import com.nerdvana.positiveoffline.background.CheatAsync;
import com.nerdvana.positiveoffline.printjobasync.PayoutAsync;
import com.nerdvana.positiveoffline.printjobasync.PostVoidAsync;
import com.nerdvana.positiveoffline.background.TimerService;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.localizereceipts.ILocalizeReceipts;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;
import com.nerdvana.positiveoffline.model.PrintJobModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.model.RefreshViewModel;
import com.nerdvana.positiveoffline.model.ReprintReceiptData;
import com.nerdvana.positiveoffline.model.ServerConnectionTest;
import com.nerdvana.positiveoffline.model.TransactionCompleteDetails;
import com.nerdvana.positiveoffline.printer.EJFileCreator;
import com.nerdvana.positiveoffline.printer.SPrinter;
import com.nerdvana.positiveoffline.printer.SStarPort;
import com.nerdvana.positiveoffline.printjobasync.CutOffAsync;
import com.nerdvana.positiveoffline.printjobasync.EndOfDayAsync;
import com.nerdvana.positiveoffline.printjobasync.PrintReceiptAsync;
import com.nerdvana.positiveoffline.printjobasync.ShortOverAsync;
import com.nerdvana.positiveoffline.printjobasync.SoaAsync;
import com.nerdvana.positiveoffline.view.checkoutmenu.LeftFrameFragment;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.view.posmenu.BottomFrameFragment;
import com.nerdvana.positiveoffline.view.productsmenu.RightFrameFragment;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarResultCode;
import com.starmicronics.starioextension.ConnectionCallback;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExtManager;
import com.starmicronics.starioextension.StarIoExtManagerListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AsyncFinishCallBack {

    private StarIoExtManager mStarIoExtManager;

    private CutOffViewModel cutOffViewModel;
    boolean hasError = false;
    private boolean isDarkMode = false;
    private ImageView onlineImageIndicator;
    private TextView onlineTextIndicator;
    private TextView tvTime;
    private TextView user;
    private TextView shift;

    private Intent timerIntent;

    private UserViewModel userViewModel;

    private DataSyncViewModel dataSyncViewModel;

    private List<PrintJobModel> myPrintJobs;

    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort starIoPort;

    private Switch toggleTheme;

    private FrameLayout leftFrame;
    private FrameLayout rightFrame;
    private FrameLayout bottomFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openFragment(R.id.bottomFrame, new BottomFrameFragment());
        openFragment(R.id.leftFrame, new LeftFrameFragment());
        openFragment(R.id.rightFrame, new RightFrameFragment());
        myPrintJobs = new ArrayList<>();
        initViews();
        tvTime.setText(Utils.getDateTimeToday());


        initUserViewModel();
        setUserData();
        initDataSyncViewModel();
        initILocalizeReceipts();
        startTimerService();
        initCutOffViewModel();
        initToggleThemeListener();



        initThemeSelectionListener();
    }

    private void updateThemeSelection(boolean isChecked) throws ExecutionException, InterruptedException {
        List<ThemeSelection> myList = new ArrayList<>();
        ThemeSelection themeSelection = null;
        for (ThemeSelection tsl : dataSyncViewModel.getThemeSelectionList()) {
            if (isChecked) {
                if (tsl.getTheme_id() == 101) { // DARK MODE
                    tsl.setIs_selected(true);
                } else {
                    tsl.setIs_selected(false);
                }
            } else {
                if (tsl.getTheme_id() == 100) { // DARK MODE
                    tsl.setIs_selected(true);
                } else {
                    tsl.setIs_selected(false);
                }
            }
            myList.add(tsl);
        }

        dataSyncViewModel.updateThemeSelection(myList);
//        dataSyncViewModel.getThemeSelectionLiveData().postValue(myList);


    }

    private void initToggleThemeListener() {

        toggleTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //update theme selected db

                try {
                    updateThemeSelection(isChecked);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private void initCutOffViewModel() {
        cutOffViewModel = new ViewModelProvider(this).get(CutOffViewModel.class);
    }

    private void setUserData() {
        try {
            List<User> currentUser = userViewModel.searchLoggedInUser();
            if (currentUser.size() > 0) {
                user.setText(currentUser.get(0).getName().toUpperCase());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void initViews() {
        leftFrame = findViewById(R.id.leftFrame);
        rightFrame = findViewById(R.id.rightFrame);
        bottomFrame = findViewById(R.id.bottomFrame);

        toggleTheme = findViewById(R.id.toggleTheme);
        onlineImageIndicator = findViewById(R.id.onlineImageIndicator);
        onlineTextIndicator = findViewById(R.id.onlineTextIndicator);
        tvTime = findViewById(R.id.tvTime);
        user = findViewById(R.id.user);
        shift = findViewById(R.id.shift);
    }

    private void startTimerService() {
        timerIntent = new Intent(this, TimerService.class);
        startService(timerIntent);
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

        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.HAS_CHANGED))) {
            if (SharedPreferenceManager.getString(null, AppConstants.HAS_CHANGED).equalsIgnoreCase("1")) {
                refreshBottomSelection();
                SharedPreferenceManager.saveString(null, "0", AppConstants.HAS_CHANGED);
            }
        }
    }

    private void refreshBottomSelection() {
        BusProvider.getInstance().post(new RefreshViewModel(""));
    }

    private void loadPrinter() {

        if (SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.EPSON))) {
            try {
                if (SPrinter.getPrinter() != null) {
                    SPrinter.getPrinter().disconnect();
                    new SPrinter(
                            dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                            dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                            getApplicationContext()
                    );
                } else {
                    new SPrinter(
                            dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                            dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                            getApplicationContext()
                    );
                }
            } catch (Exception e) {

            }
        } else if (SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MODEL).equalsIgnoreCase(String.valueOf(OtherPrinterModel.STAR_PRINTER))){


//           mStarIoExtManager = new StarIoExtManager(StarIoExtManager.Type.WithBarcodeReader, SharedPreferenceManager.getString(MainActivity.this, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, MainActivity.this);



            try {
                if (StarIOPort.searchPrinter("BT:").size() > 0) {
                    if (starIoPort == null) {

//                        starIoPort = SStarPort.getStarIOPort();
                    }
                }
            } catch (StarIOPortException e) {

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

    private void saveEjFile(PrintModel printModel) {
        String finalString = "";
        try {
            switch (printModel.getType()) {
                case "PRINT_PAYOUT":
                    Payout payoutDetails = GsonHelper.getGson().fromJson(printModel.getData(), Payout.class);
                    finalString = EJFileCreator.payoutString(payoutDetails, MainActivity.this);
                    break;
                case "POST_VOID":
                    TransactionCompleteDetails postVoidDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);
                    finalString = EJFileCreator.postVoidString(postVoidDetails, MainActivity.this, false, printModel);
                    break;
                case "BACKOUT":
                    TransactionCompleteDetails backoutDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);
                    finalString = EJFileCreator.backoutString(backoutDetails, MainActivity.this, false, printModel);
                    break;
                case "PRINT_RECEIPT":
                    TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);
                    finalString = EJFileCreator.orString(transactionCompleteDetails, MainActivity.this, false, printModel);
                    break;
                case "PRINT_XREAD":
                    CutOff cutOff = GsonHelper.getGson().fromJson(printModel.getData(), CutOff.class);
                    finalString = EJFileCreator.cutOffString(cutOff, MainActivity.this, false, false);
                    break;
                case "PRINT_ZREAD":
                    EndOfDay endOfDay = GsonHelper.getGson().fromJson(printModel.getData(), EndOfDay.class);
                    finalString = EJFileCreator.endOfDayString(endOfDay, MainActivity.this, false);
                    break;
            }
            File root = new File(Environment.getExternalStorageDirectory(), "POS/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, Utils.getCurrentDate() +".txt");
            FileWriter writer = null;
            writer = new FileWriter(gpxfile, true);
            writer.append(finalString);
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }


    }

    @Subscribe
    public void print(PrintModel printModel) {

        switch (printModel.getType()) {
            case "PRINT_PAYOUT":
                addAsync(new PayoutAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "print_payout");
                break;
            case "PRINT_INTRANSIT":
                addAsync(new IntransitAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "intransit");
                break;
            case "SOA":
                addAsync(new SoaAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "soa");
                break;
            case "CHEAT":
                addAsync(new CheatAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "cheat");
                break;
            case "POST_VOID":
                addAsync(new PostVoidAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "post_void");
                break;
            case "BACKOUT":
                addAsync(new BackoutAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "backout");
                break;
            case "PRINT_RECEIPT_SPEC":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "print_receipt_spec");
                break;
            case "REPRINT_RECEIPT_SPEC":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "reprint_receipt_spec");
                break;
            case "REPRINT_RECEIPT":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "reprint_receipt");
                break;
            case "PRINT_RECEIPT":
                saveEjFile(printModel);
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, null, false), "print_receipt");
                break;
            case "REPRINT_XREAD":
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), true), "print_xread");
                break;
            case "PRINT_SPOT_AUDIT":
//                saveEjFile(printModel);
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false), "print_xread");
                break;
            case "PRINT_SHORTOVER":
                addAsync(new ShortOverAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false), "print_shortover");
                break;
            case "PRINT_XREAD":
                saveEjFile(printModel);
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false), "print_xread");
                break;
            case "REPRINT_ZREAD":
                addAsync(new EndOfDayAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), true), "print_zread");
                break;
            case "PRINT_ZREAD":
                saveEjFile(printModel);
                addAsync(new EndOfDayAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false), "print_zread");
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
            case "print_payout":
                PayoutAsync payoutAsync = (PayoutAsync) asyncTask;
                payoutAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "intransit":
                IntransitAsync intransitAsync = (IntransitAsync) asyncTask;
                intransitAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "soa":
                SoaAsync soaAsync = (SoaAsync) asyncTask;
                soaAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "cheat":
                CheatAsync cheatAsync = (CheatAsync) asyncTask;
                cheatAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "post_void":
                PostVoidAsync postVoidAsync = (PostVoidAsync) asyncTask;
                postVoidAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "backout":
                BackoutAsync backoutAsync = (BackoutAsync) asyncTask;
                backoutAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "reprint_receipt_spec":
                PrintReceiptAsync reprintAsync1 = (PrintReceiptAsync) asyncTask;
                reprintAsync1.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_receipt_spec":
                PrintReceiptAsync reprintAsync2 = (PrintReceiptAsync) asyncTask;
                reprintAsync2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "reprint_receipt":
                PrintReceiptAsync reprintAsync = (PrintReceiptAsync) asyncTask;
                reprintAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_receipt":
                PrintReceiptAsync printReceiptAsync = (PrintReceiptAsync) asyncTask;
                printReceiptAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_shortover":
                ShortOverAsync shortOverAsync = (ShortOverAsync) asyncTask;
                shortOverAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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

    private void reprintExistingData() {
        hasError = false;
        if (myPrintJobs.size() > 0) {
            if (myPrintJobs.get(0).getTaskName().equalsIgnoreCase("print_receipt") ||
                    myPrintJobs.get(0).getTaskName().equalsIgnoreCase("print_receipt_spec")) {
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


    @Override
    public void doneProcessing() {
        hasError = false;
        myPrintJobs.remove(0);
        reprintExistingData();
    }

    @Override
    public void retryProcessing() {
        hasError = true;
        myPrintJobs.get(0).getAsyncTask().cancel(true);
//        reprintExistingData();
    }

    @Override
    public void error(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Helper.showDialogMessage(MainActivity.this, message, getString(R.string.header_message));
            }
        });
    }

    @Subscribe
    public void reprintReceipt(ReprintReceiptData reprintReceiptData) {
        if (hasError) {
            reprintExistingData();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerIntent != null) {
            stopService(timerIntent);
        }
    }

    @Subscribe
    public void onConnectionTest(ServerConnectionTest serverConnectionTest) {
        IUsers iUsers = PosClient.mRestAdapter.create(IUsers.class);
        TestRequest collectionRequest = new TestRequest("dionedata");
        Call<TestResponse> call = iUsers.sendTestRequest(
                collectionRequest.getMapValue());

        call.enqueue(new Callback<TestResponse>() {
            @Override
            public void onResponse(Call<TestResponse> call, Response<TestResponse> response) {
                showConnection(true);
            }

            @Override
            public void onFailure(Call<TestResponse> call, Throwable t) {
                showConnection(false);
            }
        });

    }

    private void showConnection(boolean canConnect) {
        if (canConnect) {
            onlineTextIndicator.setText("ONLINE");
            onlineImageIndicator.setBackground(getResources().getDrawable(R.drawable.circle_online));
        } else {
            onlineTextIndicator.setText("OFFLINE");
            onlineImageIndicator.setBackground(getResources().getDrawable(R.drawable.circle_offline));
        }

    }

    @Subscribe
    public void timerUpdate(TimerUpdateModel timerUpdateModel) {
        tvTime.setText(Utils.getDateTimeToday());
    }

    @Subscribe
    public void shiftUpdate(ShiftUpdateModel shiftUpdateModel) {
        try {
            shift.setText("SHIFT " + (cutOffViewModel.getUnCutOffData().size() + 1) + " - VER 1.2.0");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void initThemeSelectionListener() {
        DataSyncViewModel dataSyncViewModel = new ViewModelProvider(this).get(DataSyncViewModel.class);
        dataSyncViewModel.getThemeSelectionLiveData().observe(this, new Observer<List<ThemeSelection>>() {
            @Override
            public void onChanged(List<ThemeSelection> themeSelectionList) {
                for (ThemeSelection tsl : themeSelectionList) {
                    if (tsl.getIs_selected()) {
                        if (tsl.getTheme_id() == 100) { // LIGHT MODE
                            leftFrame.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            rightFrame.setBackgroundColor(getResources().getColor(R.color.colorLtGrey));
                            isDarkMode = false;
                            toggleTheme.setChecked(false);
                            break;
                        } else { // DARK MODE
                            isDarkMode = true;
                            toggleTheme.setChecked(true);
                            leftFrame.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            rightFrame.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            break;
                        }
                    }
                }
            }

        });


    }


}
