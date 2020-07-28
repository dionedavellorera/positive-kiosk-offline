package com.nerdvana.positiveoffline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.apirequests.ServerDataRequest;
import com.nerdvana.positiveoffline.apirequests.TestRequest;
import com.nerdvana.positiveoffline.apiresponses.CutoffServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.EndOfDayServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.OrDetailsServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.OrderDiscountsServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.OrdersServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.PaymentsServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.PayoutServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.PostingDiscountServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.SerialNumbersServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.ServiceChargeServerDataResponse;
import com.nerdvana.positiveoffline.apiresponses.TestResponse;
import com.nerdvana.positiveoffline.apiresponses.TransactionsServerDataResponse;
import com.nerdvana.positiveoffline.background.IntransitAsync;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Payout;
import com.nerdvana.positiveoffline.entities.PostedDiscounts;
import com.nerdvana.positiveoffline.entities.SerialNumbers;
import com.nerdvana.positiveoffline.entities.ServiceCharge;
import com.nerdvana.positiveoffline.entities.ThemeSelection;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.model.CloseInputDialogModel;
import com.nerdvana.positiveoffline.model.HasPendingDataOnLocalModel;
import com.nerdvana.positiveoffline.model.ItemScannedModel;
import com.nerdvana.positiveoffline.model.PrintingListModel;
import com.nerdvana.positiveoffline.model.ServerDataCompletionModel;
import com.nerdvana.positiveoffline.model.ShiftUpdateModel;
import com.nerdvana.positiveoffline.model.TimerUpdateModel;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;
import com.nerdvana.positiveoffline.printer.PrinterUtils;
import com.nerdvana.positiveoffline.printjobasync.BackoutAsync;
import com.nerdvana.positiveoffline.background.CheatAsync;
import com.nerdvana.positiveoffline.printjobasync.FosAsync;
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
import com.nerdvana.positiveoffline.printjobasync.PrintItemCancelledAsync;
import com.nerdvana.positiveoffline.printjobasync.PrintReceiptAsync;
import com.nerdvana.positiveoffline.printjobasync.ShortOverAsync;
import com.nerdvana.positiveoffline.printjobasync.SoaAsync;
import com.nerdvana.positiveoffline.view.checkoutmenu.LeftFrameFragment;
import com.nerdvana.positiveoffline.view.dialog.InputDialog;
import com.nerdvana.positiveoffline.view.login.LoginActivity;
import com.nerdvana.positiveoffline.view.posmenu.BottomFrameFragment;
import com.nerdvana.positiveoffline.view.productsmenu.RightFrameFragment;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.CutOffViewModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.DiscountViewModel;
import com.nerdvana.positiveoffline.viewmodel.ServiceChargeViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;
import com.squareup.otto.Subscribe;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarResultCode;
import com.starmicronics.starioextension.ConnectionCallback;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExtManager;
import com.starmicronics.starioextension.StarIoExtManagerListener;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nerdvana.positiveoffline.printer.PrinterUtils.twoColumns;

public class MainActivity extends AppCompatActivity implements AsyncFinishCallBack {



    private InputDialog inputDialog;

    private static final String ACTION_DATA_CODE_RECEIVED =
            "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
    private static final String DATA = "data";
    private static final String SOURCE = "source_byte";

    private SunmiPrinterService mSunmiPrintService;
    private PrinterPresenter printerPresenter;

    private ProgressDialog datafromServerProgressBar;

    private int totalSyncCount = 0;
    private final int totalSyncRequired = 11;

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

    private TransactionsViewModel transactionsViewModel;
    private ServiceChargeViewModel serviceChargeViewModel;
    private DiscountViewModel discountViewModel;

    private List<PrintJobModel> myPrintJobs;

    private ILocalizeReceipts iLocalizeReceipts;
    private StarIOPort starIoPort;

    private Switch toggleTheme;

    private FrameLayout leftFrame;
    private FrameLayout rightFrame;
    private FrameLayout bottomFrame;

    private ProgressBar progressNotOkay;
    private ImageView progressOkay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SocketManager(getApplicationContext());

        Log.d("SUSMODE", SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE));
        SharedPreferenceManager.saveString(MainActivity.this, "", AppConstants.HAS_CONNECTION_TO_SERVER);


//        SharedPreferenceManager.saveString(MainActivity.this, "sunmi", AppConstants.SELECTED_PRINTER_MANUALLY);
        savePrinterPreferences();
        connectInnerPrinter();
        if (TextUtils.isEmpty(SharedPreferenceManager.getString(MainActivity.this, AppConstants.TYPE_VALUE))) {
            SharedPreferenceManager.saveString(MainActivity.this, "retail", AppConstants.TYPE_VALUE);
        }

        datafromServerProgressBar = new ProgressDialog(MainActivity.this);
        datafromServerProgressBar.setMax(11);
        datafromServerProgressBar.setProgress(0);
        datafromServerProgressBar.setMessage("Retrieving data from server");
        datafromServerProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        datafromServerProgressBar.setIndeterminate(false);


        String apiBaseUrlCompany = String.format("%s/%s/",
                SharedPreferenceManager.getString(MainActivity.this, AppConstants.HOST),
                "api");
        PosClientCompany.changeApiBaseUrl(apiBaseUrlCompany);

        initViewModels();
        initCutOffViewModel();
        if (SharedPreferenceManager.getString(MainActivity.this, AppConstants.HAS_CHECKED_DATA_FROM_SERVER).equalsIgnoreCase("1")) {
            initDataSyncViewModel();
            openFragment(R.id.bottomFrame, new BottomFrameFragment());
            openFragment(R.id.leftFrame, new LeftFrameFragment());
            openFragment(R.id.rightFrame, new RightFrameFragment());
            myPrintJobs = new ArrayList<>();
            initViews();
            tvTime.setText(Utils.getDateTimeToday());
            initUserViewModel();
            setUserData();
            initILocalizeReceipts();
            startTimerService();

            initToggleThemeListener();
            initThemeSelectionListener();
        } else {
            datafromServerProgressBar.show();
            IUsers iUsers = PosClientCompany.mRestAdapter.create(IUsers.class);
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("company_code", SharedPreferenceManager.getString(MainActivity.this, AppConstants.BRANCH));
            dataMap.put("branch_code", SharedPreferenceManager.getString(MainActivity.this, AppConstants.CODE));
            dataMap.put("machine_id", SharedPreferenceManager.getString(MainActivity.this, AppConstants.MACHINE_ID));
            ServerDataRequest data = new ServerDataRequest(dataMap);

            payoutDataRequest(data, iUsers);
            serialNumberDataRequest(data, iUsers);
            serviceChargeDataRequest(data, iUsers);
            postingDiscountDataRequest(data, iUsers);
            endOfDayDataRequest(data, iUsers);
            cutOffDataRequest(data, iUsers);
            transactionsDataRequest(data, iUsers);
            orDetailsDataRequest(data, iUsers);
            paymentsDataRequest(data, iUsers);
            ordersDataRequest(data, iUsers);
            orderDiscountsDataRequest(data, iUsers);
        }





    }

    private void savePrinterPreferences() {
        if (TextUtils.isEmpty(SharedPreferenceManager.getString(MainActivity.this, AppConstants.PRINTER_PREFS))) {
            List<PrintingListModel> printoutList = new ArrayList<>();
            printoutList.add(new PrintingListModel("PRINT_ITEM_CANCELLED", "ITEM VOID", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_PAYOUT", "PAYOUT", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_INTRANSIT", "INTRANSIT", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("SOA", "STATEMENT OF ACCOUNT", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("POST_VOID", "POST VOID", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("BACKOUT", "BACKOUT", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_RECEIPT_SPEC", "OFFICIAL RECEIPT(SPECIAL)", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("REPRINT_RECEIPT_SPEC", "REPRINT OFFICIAL RECEIPT(SPECIAL)", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("REPRINT_RECEIPT", "REPRINT OFFICIAL RECEIPT(OFFICIAL)", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_RECEIPT", "OFFICIAL RECEIPT(OFFICIAL)", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("REPRINT_XREAD", "REPRINT X READING", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_SPOT_AUDIT", "SPOT AUDIT", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_SHORTOVER", "SHORT / OVER", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_XREAD", "X READING", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("REPRINT_ZREAD", "REPRINT Z READING", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_ZREAD", "Z READING", false, new ArrayList<>()));
            printoutList.add(new PrintingListModel("PRINT_FOS", "ORDER SLIP", false, new ArrayList<>()));
            SharedPreferenceManager.saveString(MainActivity.this, GsonHelper.getGson().toJson(printoutList) , AppConstants.PRINTER_PREFS);
        }
    }

    private void orderDiscountsDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<OrderDiscountsServerDataResponse> request = iUsers.orderDiscountsServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<OrderDiscountsServerDataResponse>() {
            @Override
            public void onResponse(Call<OrderDiscountsServerDataResponse> call, Response<OrderDiscountsServerDataResponse> response) {
                try {
                    if (response.body().getOrderDiscount().size() > 0) {
                        if (discountViewModel.getAllOrderDiscount().size() > 0) {
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDER DISCOUNTS"));
                        } else {
                            //add loop
                            List<OrderDiscounts> orderDiscountsList = new ArrayList<>();
                            for (OrderDiscountsServerDataResponse.OrderDiscount list : response.body().getOrderDiscount()) {
                                OrderDiscounts orderDiscounts = new OrderDiscounts();

                                orderDiscounts.setId(list.getId());
                                orderDiscounts.setTransaction_id(list.getTransactionId());
                                orderDiscounts.setProduct_id(list.getProductId());
                                orderDiscounts.setIs_percentage(list.getIsPercentage() == 1 ? true : false);
                                orderDiscounts.setValue(list.getValue());
                                orderDiscounts.setOrder_id(list.getOrderId());
                                orderDiscounts.setDiscount_name(list.getDiscountName());
                                orderDiscounts.setPosted_discount_id(list.getPostedDiscountId());
                                orderDiscounts.setIs_void(list.getIsVoid() == 1 ? true : false);
                                orderDiscounts.setIs_sent_to_server(list.getIsSentToServer());
                                orderDiscounts.setMachine_id(list.getMachineId());
                                orderDiscounts.setBranch_id(list.getBranchId());
                                orderDiscounts.setTreg(list.getTreg());

                                orderDiscounts.setTo_id(list.getToId());
                                orderDiscounts.setIs_special(list.getIsSpecial());
                                orderDiscountsList.add(orderDiscounts);
                            }

                            discountViewModel.insertOrderDiscount(orderDiscountsList);
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDER DISCOUNTS"));
                        }
                    } else {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDER DISCOUNTS"));
                    }

                } catch (Exception e) {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDER DISCOUNTS"));
                }
            }

            @Override
            public void onFailure(Call<OrderDiscountsServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDER DISCOUNTS"));
            }
        });
    }

    private void ordersDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<OrdersServerDataResponse> request = iUsers.ordersServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<OrdersServerDataResponse>() {
            @Override
            public void onResponse(Call<OrdersServerDataResponse> call, Response<OrdersServerDataResponse> response) {
                try {
                    if (response.body().getOrders().size() > 0) {
                        if (transactionsViewModel.getAllOrders().size() > 0) {
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDERS"));
                        } else {
                            //add loop
                            List<Orders> orderList = new ArrayList<>();
                            for (OrdersServerDataResponse.Order list : response.body().getOrders()) {
                                Orders orders = new Orders();
                                orders.setTransaction_id(list.getTransactionId());
                                orders.setId(list.getId());
                                orders.setCore_id(list.getCoreId());
                                orders.setQty(list.getQty());
                                orders.setAmount(list.getAmount());
                                orders.setOriginal_amount(list.getOriginalAmount());
                                orders.setName(list.getName());
                                orders.setIs_void(list.getIsVoid() == 1 ? true : false);
                                orders.setIs_editing(list.getIsEditing() == 1 ? true : false);
                                orders.setDepartmentId(list.getDepartmentId());
                                orders.setVatAmount(list.getVatAmount());
                                orders.setVatable(list.getVatable());
                                orders.setVatExempt(list.getVatExempt());
                                orders.setDiscountAmount(list.getDiscountAmount());
                                orders.setDepartmentName(list.getDepartmentName());
                                orders.setCategoryName(list.getCategoryName());
                                orders.setCategoryId(list.getCategoryId());
                                orders.setIs_sent_to_server(list.getIsSentToServer());
                                orders.setMachine_id(list.getMachineId());
                                orders.setBranch_id(list.getBranchId());
                                orders.setTreg(list.getTreg());
                                orders.setIs_room_rate(list.getIsRoomRate());
                                orders.setIs_discount_exempt(list.getIsDiscountExempt());
                                orders.setProduct_alacart_id(list.getProductAlacartId());
                                orders.setProduct_group_id(list.getProductGroupId());
                                orders.setOrders_incremental_id(list.getOrdersIncrementalId());
                                orders.setNotes(list.getNotes() == null ? "" : list.getNotes().toString());
                                orders.setIs_take_out(list.getIsTakeOut());
                                orders.setSerial_number(list.getSerialNumber());
                                orders.setIs_fixed_asset(list.getIsFixedAsset());
                                orders.setTo_id(list.getToId());
                                orders.setName_initials(list.getName_initials());
                                orders.setUnit_id(Integer.valueOf(list.getUnitId()));
                                orders.setUnit_id_description(list.getUnitIdDescription());
                                orderList.add(orders);
                            }

                            transactionsViewModel.insertOrder(orderList);
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDERS"));
                        }
                    } else {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDERS"));
                    }

                } catch (Exception e) {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDERS"));
                }
            }

            @Override
            public void onFailure(Call<OrdersServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "ORDERS"));
            }
        });

    }

    private void paymentsDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<PaymentsServerDataResponse> request = iUsers.paymentsServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<PaymentsServerDataResponse>() {
            @Override
            public void onResponse(Call<PaymentsServerDataResponse> call, Response<PaymentsServerDataResponse> response) {
                try {
                    if (response.body().getPayments().size() > 0) {
                        if (transactionsViewModel.getAllPayments().size() > 0) {


                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYMENTS"));
                        } else {
                            //add loop
                            List<Payments> paymentsList = new ArrayList<>();
                            for (PaymentsServerDataResponse.Payment list : response.body().getPayments()) {
                                Payments payments = new Payments();
                                payments.setTransaction_id(list.getTransactionId());
                                payments.setId(list.getId());
                                payments.setCore_id(list.getCoreId());
                                payments.setAmount(list.getAmount());
                                payments.setName(list.getName());
                                payments.setIs_void(list.getIsVoid() == 1 ? true : false);
                                payments.setOther_data(list.getOtherData());
                                payments.setCut_off_id(list.getCutOffId());
                                payments.setIs_sent_to_server(list.getIsSentToServer());
                                payments.setMachine_id(list.getMachineId());
                                payments.setBranch_id(list.getBranchId());
                                payments.setTreg(list.getTreg());

                                payments.setIs_redeemed(list.getIsRedeemed());
                                payments.setIs_redeemed_by(list.getIsRedeemedBy());
                                payments.setIs_redeemed_at(list.getIsRedeemedAt());
                                payments.setIs_redeemed_for(list.getIsRedeemedFor());
                                payments.setLink_payment_id(list.getLinkPaymentId());
                                payments.setIs_from_other_shift(list.getIsFromOtherShift());

                                payments.setChange(list.getChange());
                                paymentsList.add(payments);


                            }

                            transactionsViewModel.insertPayment(paymentsList);
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYMENTS"));
                        }
                    } else {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYMENTS"));
                    }

                } catch (Exception e) {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYMENTS"));
                }
            }

            @Override
            public void onFailure(Call<PaymentsServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYMENTS"));
            }
        });

    }

    private void orDetailsDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<OrDetailsServerDataResponse> request = iUsers.orDetailsServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<OrDetailsServerDataResponse>() {
            @Override
            public void onResponse(Call<OrDetailsServerDataResponse> call, Response<OrDetailsServerDataResponse> response) {
                try {
                    if (response.body().getOrDetails().size() > 0) {
                        if (transactionsViewModel.getAllOrDetails().size() > 0) {

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "OR DETAILS"));
                        } else {
                            //add loop
                            for (OrDetailsServerDataResponse.OrDetail list : response.body().getOrDetails()) {
                                OrDetails orDetails = new OrDetails();
                                orDetails.setTransaction_id(list.getTransactionId());
                                orDetails.setName(list.getName());
                                orDetails.setAddress(list.getAddress());
                                orDetails.setTin_number(list.getTinNumber());
                                orDetails.setBusiness_style(list.getBusinessStyle());
                                orDetails.setIs_sent_to_server(list.getIsSentToServer());
                                orDetails.setMachine_id(list.getMachineId());
                                orDetails.setBranch_id(list.getBranchId());
                                orDetails.setTreg(list.getTreg());
                                transactionsViewModel.insertOrDetails(orDetails);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "OR DETAILS"));
                        }
                    } else {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "OR DETAILS"));
                    }

                } catch (Exception e) {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "OR DETAILS"));
                }
            }

            @Override
            public void onFailure(Call<OrDetailsServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "OR DETAILS"));
            }
        });
    }

    private void transactionsDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<TransactionsServerDataResponse> request = iUsers.transactionsServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<TransactionsServerDataResponse>() {
            @Override
            public void onResponse(Call<TransactionsServerDataResponse> call, Response<TransactionsServerDataResponse> response) {
                if (response.body().getTransactions().size() > 0) {
                    try {
                        if (transactionsViewModel.getAllTransactions().size() > 0) {

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "TRANSACTIONS"));
                        } else {
                            //add loop
                            for (TransactionsServerDataResponse.Transaction list : response.body().getTransactions()) {
                                Transactions tr = new Transactions();
                                tr.setId(list.getId());
                                tr.setControl_number(list.getControlNumber());
                                tr.setUser_id(list.getUserId());
                                tr.setIs_void(list.getIsVoid() == 1 ? true : false);
                                tr.setIs_void_by(list.getIsVoidBy() == null ? "" : list.getIsVoidBy().toString());
                                tr.setVoid_at(list.getVoidAt() == null ? "" : list.getVoidAt().toString());

                                tr.setIs_completed(list.getIsCompleted() == 1 ? true : false);
                                tr.setIs_completed_by(list.getIsCompletedBy() == null ? "" : list.getIsCompletedBy().toString());
                                tr.setCompleted_at(list.getCompletedAt() == null ? "" : list.getCompletedAt().toString());

                                tr.setIs_saved(list.getIsSaved() == 1 ? true : false);
                                tr.setIs_saved_by(list.getIsSavedBy() == null ? "" : list.getIsSavedBy().toString());
                                tr.setSaved_at(list.getSavedAt() == null ? "" : list.getSavedAt().toString());

                                tr.setIs_cut_off(list.getIsCutOff() == 1 ? true : false);
                                tr.setIs_cut_off_by(list.getIsCutOffBy() == null ? "" : list.getIsCutOffBy().toString());
                                tr.setIs_cut_off_at(list.getIsCutOffAt() == null ? "" : list.getIsCutOffAt().toString());


                                tr.setIs_backed_out(list.getIsBackedOut() == 1 ? true : false);
                                tr.setIs_backed_out_by(list.getIsBackedOutBy() == null ? "" : list.getIsBackedOutBy().toString());
                                tr.setIs_backed_out_at(list.getIsBackedOutAt() == null ? "" : list.getIsBackedOutAt().toString());

                                tr.setTrans_name(list.getTransName() == null ? "" : list.getTransName().toString());
                                tr.setTreg(list.getTreg());
                                tr.setReceipt_number(list.getReceiptNumber() == null ? "" : list.getReceiptNumber());
                                tr.setGross_sales(list.getGrossSales());
                                tr.setNet_sales(list.getNetSales());
                                tr.setVatable_sales(list.getVatableSales());
                                tr.setVat_exempt_sales(list.getVatExemptSales());
                                tr.setVat_amount(list.getVatAmount());
                                tr.setDiscount_amount(list.getDiscountAmount());

                                tr.setChange(list.getChange());

                                tr.setCut_off_id(list.getCutOffId());
                                tr.setHas_special(list.getHasSpecial());


                                tr.setIs_cancelled(list.getIsBackedOut() == 1 ? true : false);
                                tr.setIs_cancelled_by(list.getIsCancelledBy() == null ? "" : list.getIsCancelledBy().toString());
                                tr.setIs_cancelled_at(list.getIsCancelledAt() == null ? "" : list.getIsCancelledAt().toString());

                                tr.setTin_number(list.getTinNumber() == null ? "" : list.getTinNumber());
                                tr.setIs_sent_to_server(list.getIsSentToServer());

                                tr.setMachine_id(list.getMachineId());
                                tr.setBranch_id(list.getBranchId());

                                tr.setRoom_id(list.getRoomId());

                                tr.setRoom_number(list.getRoomNumber() == null ? "" : list.getRoomNumber().toString());

                                tr.setCheck_in_time(list.getCheckInTime() == null ? "" : list.getCheckInTime().toString());
                                tr.setCheck_out_time(list.getCheckOutTime() == null ? "" : list.getCheckOutTime().toString());


                                tr.setService_charge_value(Double.valueOf(list.getServiceChargeValue()));
                                tr.setService_charge_is_percentage(list.getServiceChargeIsPercentage() == 1 ? true : false);

                                tr.setIs_shared(list.getIsShared());

                                tr.setDelivery_to(list.getDeliveryTo());
                                tr.setDelivery_address(list.getDeliveryAddress());
                                tr.setTo_id(list.getToId());


                                tr.setTo_transaction_id(list.getToTransactionId());
                                tr.setIs_temp(list.getIsTemp());

                                tr.setTo_control_number(list.getTo_control_number());
                                tr.setShift_number(list.getShiftNumber());

                                transactionsViewModel.insertTransactionWaitData(tr);

                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "TRANSACTIONS"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "TRANSACTIONS"));
                        Log.d("ERRORDATA-TRASN", e.getMessage());
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "TRANSACTIONS"));
                }
            }

            @Override
            public void onFailure(Call<TransactionsServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "TRANSACTIONS"));
            }
        });
    }

    private void cutOffDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<CutoffServerDataResponse> request = iUsers.cutOffServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<CutoffServerDataResponse>() {
            @Override
            public void onResponse(Call<CutoffServerDataResponse> call, Response<CutoffServerDataResponse> response) {
                if (response.body().getCutOff().size() > 0) {
                    try {
                        if (cutOffViewModel.getAllCutOffData().size() > 0) {

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "CUTOFF"));
                        } else {
                            //add loop
                            for (CutoffServerDataResponse.CutOff list : response.body().getCutOff()) {
                                CutOff cutOff = new CutOff();
                                cutOff.setId(list.getId());
                                cutOff.setNumber_of_transactions(list.getNumberOfTransactions());
                                cutOff.setGross_sales(list.getGrossSales());
                                cutOff.setNet_sales(list.getNetSales());
                                cutOff.setVatable_sales(list.getVatableSales());
                                cutOff.setVat_exempt_sales(list.getVatExemptSales());
                                cutOff.setVat_amount(list.getVatAmount());
                                cutOff.setVoid_amount(list.getVoidAmount());
                                cutOff.setTotal_cash_amount(list.getTotalCashAmount());
                                cutOff.setTotal_cash_payments(list.getTotalCashPayments());
                                cutOff.setTotal_card_payments(list.getTotalCardPayments());
                                cutOff.setTotal_change(Double.valueOf(list.getTotalChange()));

                                cutOff.setZ_read_id(list.getzReadId());
                                cutOff.setTreg(list.getTreg());

                                cutOff.setSeniorCount(list.getSeniorCount());
                                cutOff.setSeniorAmount(list.getSeniorAmount());

                                cutOff.setPwdCount(list.getPwdCount());
                                cutOff.setPwdAmount(list.getPwdAmount());

                                cutOff.setOthersCount(list.getOthersCount());
                                cutOff.setOthersAmount(list.getOthersAmount());

                                cutOff.setTotal_payout(Double.valueOf(list.getTotalPayout()));
                                cutOff.setBegOrNo(list.getBegOrNo());
                                cutOff.setEndOrNo(list.getEndOrNo());
                                cutOff.setIs_sent_to_server(list.getIsSentToServer());
                                cutOff.setMachine_id(list.getMachineId());
                                cutOff.setBranch_id(list.getBranchId());
                                cutOff.setTotal_service_charge(Double.valueOf(list.getTotalServiceCharge()));


                                cutOff.setCash_redeemed_from_prev_ar(list.getCashRedeemedFromPrevAr());
                                cutOff.setCard_redeemed_from_prev_ar(list.getCardRedeemedFromPrevAr());

                                cutOff.setShift_number(list.getShiftNumber());
                                cutOffViewModel.insertData(cutOff);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "CUTOFF"));
                        }
                    } catch (Exception e) {
                        Log.d("ERRORDATA-CUTOFF", e.getMessage());
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "CUTOFF"));
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "CUTOFF"));
                }
            }

            @Override
            public void onFailure(Call<CutoffServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "CUTOFF"));
            }
        });
    }

    private void endOfDayDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<EndOfDayServerDataResponse> request = iUsers.endOfDayServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<EndOfDayServerDataResponse>() {
            @Override
            public void onResponse(Call<EndOfDayServerDataResponse> call, Response<EndOfDayServerDataResponse> response) {
                if (response.body().getEndOfDay().size() > 0) {
                    try {
                        if (cutOffViewModel.getAllEndOfDayData().size() > 0) {

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "END OF DAY"));
                        } else {
                            //add loop
                            for (EndOfDayServerDataResponse.EndOfDay list : response.body().getEndOfDay()) {
                                EndOfDay endOfDay = new EndOfDay();
                                endOfDay.setId(list.getId());
                                endOfDay.setNumber_of_transactions(list.getNumberOfTransactions());
                                endOfDay.setGross_sales(list.getGrossSales());
                                endOfDay.setNet_sales(list.getNetSales());
                                endOfDay.setVatable_sales(list.getVatableSales());
                                endOfDay.setVat_exempt_sales(list.getVatExemptSales());
                                endOfDay.setVat_amount(list.getVatAmount());
                                endOfDay.setVoid_amount(list.getVoidAmount());
                                endOfDay.setTotal_cash_amount(list.getTotalCashAmount());
                                endOfDay.setTotal_cash_payments(list.getTotalCashPayments());
                                endOfDay.setTotal_card_payments(list.getTotalCardPayments());
                                endOfDay.setTotal_change(Double.valueOf(list.getTotalChange()));
                                endOfDay.setTreg(list.getTreg());
                                endOfDay.setTotal_service_charge(Double.valueOf(list.getTotalServiceCharge()));
                                endOfDay.setSeniorCount(list.getSeniorCount());
                                endOfDay.setSeniorAmount(list.getSeniorAmount());
                                endOfDay.setPwdCount(list.getPwdCount());
                                endOfDay.setPwdAmount(list.getPwdAmount());
                                endOfDay.setOthersCount(list.getOthersCount());
                                endOfDay.setOthersAmount(list.getOthersAmount());
                                endOfDay.setBegOrNo(list.getBegOrNo());
                                endOfDay.setEndOrNo(list.getEndOrNo());
                                endOfDay.setBegSales(list.getBegSales());
                                endOfDay.setEndSales(list.getEndSales());
                                endOfDay.setIs_sent_to_server(list.getIsSentToServer());
                                endOfDay.setMachine_id(list.getMachineId());
                                endOfDay.setBranch_id(list.getBranchId());
                                endOfDay.setTotal_payout(Double.valueOf(list.getTotalPayout()));

                                endOfDay.setCash_redeemed_from_prev_ar(list.getCashRedeemedFromPrevAr());
                                endOfDay.setCard_redeemed_from_prev_ar(list.getCardRedeemedFromPrevAr());
                                cutOffViewModel.insertData(endOfDay);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "END OF DAY"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "END OF DAY"));
                        Log.d("ERRORDATA-EOD", e.getMessage());
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "END OF DAY"));
                }
            }

            @Override
            public void onFailure(Call<EndOfDayServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "END OF DAY"));
            }
        });
    }

    private void postingDiscountDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<PostingDiscountServerDataResponse> request = iUsers.postedDiscountServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<PostingDiscountServerDataResponse>() {
            @Override
            public void onResponse(Call<PostingDiscountServerDataResponse> call, Response<PostingDiscountServerDataResponse> response) {
                if (response.body().getPostingDiscount().size() > 0) {
                    try {
                        if (discountViewModel.getAllPostedDiscount().size() > 0) {




                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "POSTING DISCOUNT"));
                        } else {
                            //add loop
                            for (PostingDiscountServerDataResponse.PostingDiscount list : response.body().getPostingDiscount()) {
                                PostedDiscounts postedDiscounts = new PostedDiscounts();
                                postedDiscounts.setId(list.getId());
                                postedDiscounts.setTransaction_id(list.getTransactionId());
                                postedDiscounts.setDiscount_id(list.getDiscountId());
                                postedDiscounts.setDiscount_name(list.getDiscountName());
                                postedDiscounts.setIs_void(list.getIsVoid() == 1 ? true : false);
                                postedDiscounts.setCard_number(list.getCardNumber());
                                postedDiscounts.setName(list.getName());
                                postedDiscounts.setAddress(list.getAddress());

                                postedDiscounts.setCut_off_id(list.getCutOffId());
                                postedDiscounts.setEnd_of_day_id(list.getEndOfDayId());
                                postedDiscounts.setAmount(list.getAmount());

                                postedDiscounts.setIs_percentage(list.getIsPercentage() == 1 ? true : false);
                                postedDiscounts.setDiscount_value(list.getDiscountValue());
                                postedDiscounts.setIs_sent_to_server(list.getIsSentToServer());
                                postedDiscounts.setMachine_id(list.getMachineId());
                                postedDiscounts.setBranch_id(list.getBranchId());
                                postedDiscounts.setTreg(list.getTreg());
                                postedDiscounts.setTo_id(list.getToId());

                                postedDiscounts.setQty(list.getQty());

                                discountViewModel.insertPostedDiscount(postedDiscounts);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "POSTING DISCOUNT"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "POSTING DISCOUNT"));
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "POSTING DISCOUNT"));
                }
            }

            @Override
            public void onFailure(Call<PostingDiscountServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "POSTING DISCOUNT"));
            }
        });
    }

    private void serviceChargeDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<ServiceChargeServerDataResponse> request = iUsers.serviceChargeServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<ServiceChargeServerDataResponse>() {
            @Override
            public void onResponse(Call<ServiceChargeServerDataResponse> call, Response<ServiceChargeServerDataResponse> response) {
                if (response.body().getServiceCharge().size() > 0) {
                    try {
                        if (serviceChargeViewModel.getServiceChargeList().size() > 0) {

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERVICE CHARGE"));
                        } else {
                            //add loop
                            for (ServiceChargeServerDataResponse.ServiceCharge list : response.body().getServiceCharge()) {
                                ServiceCharge serviceCharge = new ServiceCharge();
                                serviceCharge.setId(list.getId());
                                serviceCharge.setValue(Double.valueOf(list.getValue()));
                                serviceCharge.setIs_percentage(list.getIsPercentage() == 1 ? true : false);
                                serviceCharge.setIs_selected(list.getIsSelected() == 1 ? true : false);
                                serviceCharge.setIs_sent_to_server(list.getIsSentToServer());
                                serviceCharge.setMachine_id(list.getMachineId());
                                serviceCharge.setBranch_id(list.getBranchId());
                                serviceChargeViewModel.insertServiceChargeSetting(serviceCharge);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERVICE CHARGE"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERVICE CHARGE"));
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERVICE CHARGE"));
                }
            }

            @Override
            public void onFailure(Call<ServiceChargeServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERVICE CHARGE"));
            }
        });
    }

    private void serialNumberDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<SerialNumbersServerDataResponse> request = iUsers.serialNumberServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<SerialNumbersServerDataResponse>() {
            @Override
            public void onResponse(Call<SerialNumbersServerDataResponse> call, Response<SerialNumbersServerDataResponse> response) {
                if (response.body().getSerialNumbers().size() > 0) {
                    try {
                        if (transactionsViewModel.getSerialNumbers().size() > 0) {
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERIAL NUMBER"));
                        } else {
                            //add loop
                            for (SerialNumbersServerDataResponse.SerialNumber list : response.body().getSerialNumbers()) {
                                SerialNumbers serialNumbers = new SerialNumbers();
                                serialNumbers.setId(list.getId());
                                serialNumbers.setTransaction_id(list.getTransactionId());
                                serialNumbers.setSerial_number(list.getSerialNumber());
                                serialNumbers.setTreg(list.getTreg());
                                serialNumbers.setIs_void(list.getIsVoid()== 1 ? true : false);
                                serialNumbers.setIs_void_at(list.getIsVoidAt());
                                serialNumbers.setProduct_core_id(list.getProductCoreId());
                                serialNumbers.setProduct_name(list.getProductName());
                                serialNumbers.setFor_update(list.getForUpdate());
                                serialNumbers.setIs_sent_to_server(list.getIsSentToServer());
                                serialNumbers.setOrder_id(list.getOrderId());
                                serialNumbers.setMachine_id(list.getMachineId());
                                serialNumbers.setBranch_id(list.getBranchId());

                                serialNumbers.setTo_id(list.getToId());
                                transactionsViewModel.insertSerialNumbers(serialNumbers);
                            }
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERIAL NUMBER"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERIAL NUMBER"));
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERIAL NUMBER"));
                }
            }

            @Override
            public void onFailure(Call<SerialNumbersServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "SERIAL NUMBER"));
            }
        });

    }

    private void payoutDataRequest(ServerDataRequest data, IUsers iUsers) {
        Call<PayoutServerDataResponse> request = iUsers.payoutServerDataRequest(data.getMapValue());
        request.enqueue(new Callback<PayoutServerDataResponse>() {
            @Override
            public void onResponse(Call<PayoutServerDataResponse> call, Response<PayoutServerDataResponse> response) {
                if (response.body().getPayouts().size() > 0) {
                    try {
                        if (transactionsViewModel.payoutList().size() > 0) {
                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYOUT"));
                        } else {
                            for (PayoutServerDataResponse.Payout list : response.body().getPayouts()) {
                                Payout payout = new Payout();
                                payout.setId(list.getId());
                                payout.setSeries_number(list.getSeriesNumber());
                                payout.setUsername(list.getUsername());
                                payout.setAmount(Double.valueOf(list.getAmount()));
                                payout.setReason(list.getReason());
                                payout.setManager_username(list.getManagerUsername());
                                payout.setTreg(list.getTreg());
                                payout.setIs_sent_to_server(list.getIsSentToServer());
                                payout.setMachine_id(list.getMachineId());
                                payout.setBranch_id(list.getBranchId());
                                payout.setIs_cut_off(list.getIsCutOff() == 0 ? false : true);
                                payout.setIs_cut_off_by(list.getIsCutOffBy());
                                payout.setIs_cut_off_at(list.getIsCutOffAt());
                                payout.setCut_off_id(list.getCutOffId());
                                transactionsViewModel.insertPayoutData(payout);
                            }

                            BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYOUT"));
                        }
                    } catch (Exception e) {
                        BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYOUT"));
                    }

                } else {
                    BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYOUT"));
                }
            }

            @Override
            public void onFailure(Call<PayoutServerDataResponse> call, Throwable t) {
                BusProvider.getInstance().post(new ServerDataCompletionModel(true, "PAYOUT"));
            }
        });
    }

    private void initViewModels() {
        serviceChargeViewModel = new ViewModelProvider(this).get(ServiceChargeViewModel.class);
        discountViewModel = new ViewModelProvider(this).get(DiscountViewModel.class);
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
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
        progressNotOkay = findViewById(R.id.progress);
        progressOkay = findViewById(R.id.progressOkay);
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
            case 152:
                inputDialog = new InputDialog(MainActivity.this, "SEARCH", "") {

                    @Override
                    public void confirm(String str) {
                        BusProvider.getInstance().post(new ItemScannedModel(str));

                        dismiss();
                    }

                };
                inputDialog.show();
                break;
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
        try {
            if (userViewModel.searchLoggedInUser().size() < 1) {
                openLoginPage();
            }
        } catch (Exception e) {

        }


        if (!TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.HAS_CHANGED))) {
            if (SharedPreferenceManager.getString(null, AppConstants.HAS_CHANGED).equalsIgnoreCase("1")) {
                refreshBottomSelection();
                SharedPreferenceManager.saveString(null, "0", AppConstants.HAS_CHANGED);
            }
        }
        registerReceiver();


        loadUserImage();
    }

    private void loadUserImage() {
//        File direct = new File(Environment.getExternalStorageDirectory()
//                + "/POS/USERS/" + SharedPreferenceManager.getString(null, AppConstants.));
//
//        if (transactionType.equalsIgnoreCase("delivery") && model.getCore_id() == 998) {
//            Picasso.get().load(R.drawable.ic_shipping).placeholder(R.drawable.pos_logo_edited).into(((PaymentTypeAdapter.ViewHolder)holder).image);
//        } else {
//            if (model.getCore_id() == 999) {
//                Picasso.get().load(R.mipmap.baseline_assignment_black_48dp).placeholder(R.drawable.pos_logo_edited).into(((PaymentTypeAdapter.ViewHolder)holder).image);
//            } else {
//                Picasso.get().load(direct).placeholder(R.drawable.pos_logo_edited).into(((PaymentTypeAdapter.ViewHolder)holder).image);
//            }
//        }
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
        unregisterReceiver(receiver);
    }

    private void logoutUser() throws ExecutionException, InterruptedException{
//        SharedPreferenceManager.saveString(MainActivity.this, "0", AppConstants.HAS_CHECKED_DATA_FROM_SERVER);
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
                case "PRINT_ITEM_CANCELLED":
                    TypeToken<List<Orders>> token = new TypeToken<List<Orders>>() {};
                    List<Orders> ordersList = GsonHelper.getGson().fromJson(printModel.getData(), token.getType());
                    finalString = EJFileCreator.itemCancelledString(ordersList, MainActivity.this);
                    break;
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
            case "PRINT_FOS":
                addAsync(new FosAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "print_fos");
                break;
            case "PRINT_ITEM_CANCELLED":
                saveEjFile(printModel);
                addAsync(new PrintItemCancelledAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "print_item_cancelled");
                break;
            case "PRINT_PAYOUT":
                saveEjFile(printModel);
                addAsync(new PayoutAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "print_payout");
                break;
            case "PRINT_INTRANSIT":
//                intransitTest(printModel);
                addAsync(new IntransitAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "intransit");
                break;
            case "SOA":
                addAsync(new SoaAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "soa");
                break;
            case "CHEAT": //ignore
                addAsync(new CheatAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true), "cheat");
                break;
            case "POST_VOID":
                saveEjFile(printModel);
                addAsync(new PostVoidAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "post_void");
                break;
            case "BACKOUT":
                saveEjFile(printModel);
                addAsync(new BackoutAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "backout");
                break;
            case "PRINT_RECEIPT_SPEC":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "print_receipt_spec");
                break;
            case "REPRINT_RECEIPT_SPEC":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "reprint_receipt_spec");
                break;
            case "REPRINT_RECEIPT":
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(),true,
                        printerPresenter, mSunmiPrintService), "reprint_receipt");
                break;
            case "PRINT_RECEIPT":
//                orTest(printModel);
                saveEjFile(printModel);
                addAsync(new PrintReceiptAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, null, false,
                        printerPresenter, mSunmiPrintService), "print_receipt");
                break;
            case "REPRINT_XREAD":
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), true,
                        printerPresenter, mSunmiPrintService), "print_xread");
                break;
            case "PRINT_SPOT_AUDIT":
//                saveEjFile(printModel);
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false,
                        printerPresenter, mSunmiPrintService), "print_xread");
                break;
            case "PRINT_SHORTOVER":
                addAsync(new ShortOverAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false,
                        printerPresenter, mSunmiPrintService), "print_shortover");
                break;
            case "PRINT_XREAD":
                saveEjFile(printModel);
                addAsync(new CutOffAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false,
                        printerPresenter, mSunmiPrintService), "print_xread");
                break;
            case "REPRINT_ZREAD":
                addAsync(new EndOfDayAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), true,
                        printerPresenter, mSunmiPrintService), "print_zread");
                break;
            case "PRINT_ZREAD":
                saveEjFile(printModel);
                addAsync(new EndOfDayAsync(printModel, MainActivity.this,
                        this, dataSyncViewModel,
                        iLocalizeReceipts, SStarPort.getStarIOPort(), false,
                        printerPresenter, mSunmiPrintService), "print_zread");
                break;
        }

    }

    private void orTest(PrintModel printModel) {
        String finalOrString = "";
        final TransactionCompleteDetails transactionCompleteDetails = GsonHelper.getGson().fromJson(printModel.getData(), TransactionCompleteDetails.class);

        Log.d("RECEIPTDATA", new String(new char[Integer.valueOf(SharedPreferenceManager.getString(getApplicationContext(), AppConstants.MAX_COLUMN_COUNT))]));
        Log.d("RECEIPTDATA", "QTY  DESCRIPTION          AMOUNT");
        Log.d("RECEIPTDATA", new String(new char[Integer.valueOf(SharedPreferenceManager.getString(getApplicationContext(), AppConstants.MAX_COLUMN_COUNT))]));

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

                if (orders.getProduct_group_id() != 0 || orders.getProduct_alacart_id() != 0) {
                    Log.d("RECEIPTDATA", twoColumns(
                            qty +  "  " + orders.getName(),
                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
                            40,
                            2,
                            getApplicationContext()));


                } else {
                    Log.d("RECEIPTDATA", twoColumns(
                            qty +  " " + orders.getName(),
                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getOriginal_amount() * orders.getQty())),
                            40,
                            2,
                            getApplicationContext()));

                }

                if (orders.getDiscountAmount() > 0) {
                    Log.d("RECEIPTDATA", twoColumns(
                            "LESS",
                            PrinterUtils.returnWithTwoDecimal(String.valueOf(orders.getDiscountAmount() * orders.getQty())),
                            40,
                            2,
                            getApplicationContext()));

                }






                if (orders.getVatExempt() <= 0) {
                    amountDue += orders.getOriginal_amount() * orders.getQty();
                } else {
                    amountDue += orders.getAmount() * orders.getQty();
                }
            }

        }



        if (transactionCompleteDetails.postedDiscountsList.size() > 0) {
            Log.d("RECEIPTDATA" , "DISCOUNT LIST");

            for (PostedDiscounts postedDiscounts : transactionCompleteDetails.postedDiscountsList) {
                if (!postedDiscounts.getIs_void()) {

                    Log.d("RECEIPTDATA" , twoColumns(
                            postedDiscounts.getDiscount_name(),
                            postedDiscounts.getCard_number(),
                            40,
                            2,
                            getApplicationContext()));

                }
            }
        }

        Log.d("RECEIPTDATA", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffFourDecimal(transactionCompleteDetails.transactions.getVatable_sales()))));
        Log.d("RECEIPTDATA", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffFourDecimal(transactionCompleteDetails.transactions.getVat_amount()))));
        Log.d("RECEIPTDATA", PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffFourDecimal(transactionCompleteDetails.transactions.getVat_exempt_sales()))));
        Log.d("RECEIPTDATA", twoColumns(
                "ZERO-RATED SALES",
                "0.00",
                40,
                2,
                getApplicationContext()));





        if (transactionCompleteDetails.transactions.getService_charge_value() > 0) {
            Log.d("RECEIPTDATA", twoColumns(
                    "SERVICE CHARGE",
                    PrinterUtils.returnWithTwoDecimal(String.valueOf(transactionCompleteDetails.transactions.getService_charge_value())),
                    40,
                    2,
                    getApplicationContext()));

        } else {
            Log.d("RECEIPTDATA", twoColumns(
                    "SERVICE CHARGE",
                    "0.00",
                    40,
                    2,
                    getApplicationContext()));

        }

        Log.d("RECEIPTDATA", twoColumns(
                "SUB TOTAL",
                PrinterUtils.returnWithTwoDecimal(
                        String.valueOf(
                                Utils.roundedOffFourDecimal(
                                        transactionCompleteDetails.transactions.getGross_sales()
                                ) +
                                        Utils.roundedOffFourDecimal(
                                                transactionCompleteDetails.transactions.getService_charge_value()
                                        )
                        )),
                40,
                2,
                getApplicationContext()));

        Log.d("RECEIPTDATA", twoColumns(
                "AMOUNT DUE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(amountDue) + Utils.roundedOffFourDecimal(
                        transactionCompleteDetails.transactions.getService_charge_value()
                )),
                40,
                2,
                getApplicationContext()));

        Double payments = 0.00;
        List<Integer> tmpArray = new ArrayList<>();
        String pymntType = "";
        for (Payments pym : transactionCompleteDetails.paymentsList) {
            if (!tmpArray.contains(pym.getCore_id())) {
                tmpArray.add(pym.getCore_id());
                pymntType = pym.getName();
            }
            payments += Utils.roundedOffFourDecimal(pym.getAmount());
        }


        Log.d("RECEIPTDATA", twoColumns(
                "TENDERED",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffFourDecimal(payments))),
                40,
                2,
                getApplicationContext()));
        Log.d("RECEIPTDATA", twoColumns(
                "CHANGE",
                PrinterUtils.returnWithTwoDecimal(String.valueOf(Utils.roundedOffFourDecimal(transactionCompleteDetails.transactions.getChange()))),
                40,
                2,
                getApplicationContext()));


        Log.d("RECEIPTDATA", twoColumns(
                "PAYMENT TYPE",
                tmpArray.size() > 1 ? "MULTIPLE" : pymntType
                ,
                40,
                2,getApplicationContext()));
        Log.d("RECEIPTDATA", "SOLD TO");



        if (transactionCompleteDetails.orDetails != null) {
            Log.d("RECEIPTDATA", "NAME:" + transactionCompleteDetails.orDetails.getName());
            Log.d("RECEIPTDATA", "ADDRESS:" + transactionCompleteDetails.orDetails.getAddress());
            Log.d("RECEIPTDATA", "TIN#:" + transactionCompleteDetails.orDetails.getTin_number());
            Log.d("RECEIPTDATA", "BUSINESS STYLE:" + transactionCompleteDetails.orDetails.getBusiness_style());
        } else {
            Log.d("RECEIPTDATA", "NAME:" + "---");
            Log.d("RECEIPTDATA", "ADDRESS:" + "---");
            Log.d("RECEIPTDATA", "TIN#:" + "---");
            Log.d("RECEIPTDATA", "BUSINESS STYLE:" + "----");
        }


        if (transactionCompleteDetails.transactions.getTransaction_type().equalsIgnoreCase("delivery")) {

            Log.d("RECEIPTDATA", "CONTROL#");
            Log.d("RECEIPTDATA", transactionCompleteDetails.transactions.getControl_number());

            Log.d("RECEIPTDATA", "DELIVERY FOR");
            Log.d("RECEIPTDATA", transactionCompleteDetails.transactions.getDelivery_to());

            Log.d("RECEIPTDATA", "DELIVERY ADDRESS");
            Log.d("RECEIPTDATA", transactionCompleteDetails.transactions.getDelivery_address());

        }




    }

    private void intransitTest(PrintModel printModel) {
        String intransitString = "";
        TypeToken<List<TransactionWithOrders>> token = new TypeToken<List<TransactionWithOrders>>() {};
        List<TransactionWithOrders> transactions = GsonHelper.getGson().fromJson(printModel.getData(), token.getType());


//        addTextToPrinter(printer, "INTRANSIT SLIP", Printer.TRUE, Printer.FALSE, Printer.ALIGN_CENTER, 1, 1, 2);
        intransitString += "INTRANSIT SLIP\n";

        List<String> t = new ArrayList<>();
        t.add("I");
        t.add("II");
        t.add("III");
        t.add("IV");
        t.add("V");

        intransitString += intransitReceipt(t);





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
                    temp.add(tr.transactions.getRoom_number());
                    temp.add(String.valueOf(tr.ordersList.size()));
                    String dateONly = tr.transactions.getCheck_in_time().split(" ")[0];
                    temp.add(dateONly.split("-")[1] + "-" + dateONly.split("-")[2]);
                    temp.add(String.valueOf(tr.transactions.getGross_sales()));
                    temp.add(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");


                }
            }


            intransitString += intransitReceipt(temp);

            Log.d("DIONEINTRANSIT", intransitString);
//            addTextToPrinter(printer, intransitReceipt(temp), Printer.TRUE, Printer.FALSE, Printer.ALIGN_LEFT, 1, 1, 1);

        }

    }

    private String intransitReceipt(List<String> details) {
        String finalString = "";
        float maxColumn = Float.valueOf(SharedPreferenceManager.getString(getApplicationContext(), AppConstants.MAX_COLUMN_COUNT));
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
            case "print_fos":
                FosAsync fosAsync =(FosAsync) asyncTask;
                fosAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
            case "print_item_cancelled":
                PrintItemCancelledAsync printItemCancelledAsync = (PrintItemCancelledAsync) asyncTask;
                printItemCancelledAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                break;
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
            SharedPreferenceManager.saveString(MainActivity.this, "yes", AppConstants.HAS_CONNECTION_TO_SERVER);
            onlineTextIndicator.setText("ONLINE");
            onlineImageIndicator.setBackground(getResources().getDrawable(R.drawable.circle_online));
        } else {
            SharedPreferenceManager.saveString(MainActivity.this, "", AppConstants.HAS_CONNECTION_TO_SERVER);
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
            shift.setText("SHIFT " + (cutOffViewModel.getUnCutOffData().size() + 1) + " - VER 3.0.3");
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

    @Subscribe
    public void hasPendingDataOnLocal(HasPendingDataOnLocalModel hasPendingDataOnLocalModel) {
        if (hasPendingDataOnLocalModel.isHasPending()) {
            //show loading on progress
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressOkay.setVisibility(View.GONE);
                    progressNotOkay.setVisibility(View.VISIBLE);
                }
            });

        } else {
            //show check on progress
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressOkay.setVisibility(View.VISIBLE);
                    progressNotOkay.setVisibility(View.GONE);
                }
            });
        }
    }

    @Subscribe
    public void serverDataCompletion(ServerDataCompletionModel serverDataCompletionModel) {

        totalSyncCount += 1;

        Log.d("TOTALSYNCCOUNT", String.valueOf(totalSyncCount));
        Log.d("TOTALSYNCCOUNT", String.valueOf(totalSyncRequired));

        datafromServerProgressBar.setProgress(totalSyncCount);

        if (totalSyncCount == totalSyncRequired) {

            SharedPreferenceManager.saveString(MainActivity.this, "1", AppConstants.HAS_CHECKED_DATA_FROM_SERVER);

            initDataSyncViewModel();
            openFragment(R.id.bottomFrame, new BottomFrameFragment());
            openFragment(R.id.leftFrame, new LeftFrameFragment());
            openFragment(R.id.rightFrame, new RightFrameFragment());
            myPrintJobs = new ArrayList<>();
            initViews();
            tvTime.setText(Utils.getDateTimeToday());
            initUserViewModel();
            setUserData();
            initILocalizeReceipts();
            startTimerService();
            initCutOffViewModel();
            initToggleThemeListener();
            initThemeSelectionListener();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (datafromServerProgressBar != null) {
                        datafromServerProgressBar.dismiss();
                    }

                }
            }, 500);



        }
    }


    private void connectInnerPrinter() {
        try {
            InnerPrinterManager.getInstance().bindService(this,
                    innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {

            mSunmiPrintService = service;
            printerPresenter = new PrinterPresenter(MainActivity.this, mSunmiPrintService);

        }

        @Override
        protected void onDisconnected() {
            mSunmiPrintService = null;

        }
    };


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra(DATA);
            byte[] arr = intent.getByteArrayExtra(SOURCE);
            if (code != null && !code.isEmpty()) {

                String fnl = code.replaceAll("\n", "");

                BusProvider.getInstance().post(new ItemScannedModel(fnl));


            }
        }
    };

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DATA_CODE_RECEIVED);
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.isCtrlPressed()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_F:
                    inputDialog = new InputDialog(MainActivity.this, "SEARCH", "") {

                        @Override
                        public void confirm(String str) {
                            BusProvider.getInstance().post(new ItemScannedModel(str));

                            dismiss();
                        }

                    };
                    inputDialog.show();
                default:
                    return super.onKeyUp(keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }


    @Subscribe
    public void closeInputDialog(CloseInputDialogModel closeInputDialogModel) {
        if (inputDialog != null) {
            inputDialog.dismiss();
        }
    }

}
