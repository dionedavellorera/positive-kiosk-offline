package com.nerdvana.positiveoffline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Application;
import android.content.Intent;
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
import com.nerdvana.positiveoffline.intf.AsyncFinishCallBack;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.PrintJobModel;
import com.nerdvana.positiveoffline.model.PrintModel;
import com.nerdvana.positiveoffline.printer.SPrinter;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AsyncFinishCallBack {


    private UserViewModel userViewModel;

    private DataSyncViewModel dataSyncViewModel;

    private List<PrintJobModel> myPrintJobs;
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
        try {
            new SPrinter(
                    dataSyncViewModel.getActivePrinterSeries().getModel_constant(),
                    dataSyncViewModel.getActivePrinterLanguage().getLanguage_constant(),
                    getApplicationContext()
            );
        } catch (Exception e) {

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
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this, this, dataSyncViewModel), "print_receipt");
                break;
            case "PRINT_XREAD":
                addAsync(new CutOffAsync(printModel, MainActivity.this, this, dataSyncViewModel), "print_xread");
                break;
            case "PRINT_ZREAD":
                addAsync(new EndOfDayAsync(printModel, MainActivity.this, this, dataSyncViewModel), "print_zread");
                break;
        }

        try {
            if (SPrinter.getPrinter() != null) {
                SPrinter.getPrinter().addCut(Printer.CUT_FEED);
                if (SPrinter.getPrinter().getStatus().getConnection() == 1) {
                    SPrinter.getPrinter().sendData(Printer.PARAM_DEFAULT);
                    SPrinter.getPrinter().clearCommandBuffer();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Printer not setup", Toast.LENGTH_SHORT).show();
            }
        } catch (Epos2Exception e) {
            e.printStackTrace();
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
}
