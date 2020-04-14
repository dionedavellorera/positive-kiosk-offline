package com.nerdvana.positiveoffline.view.rooms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.adapter.RoomTablesAdapter;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.RoomRates;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.RoomContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.view.dialog.ChangeRoomStatusDialog;
import com.nerdvana.positiveoffline.view.dialog.InputWithTimeDialog;
import com.nerdvana.positiveoffline.view.dialog.RoomRateSelectionDialog;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoomsActivity extends AppCompatActivity implements RoomContract {

    private SwipeRefreshLayout swipeRefreshRoom;
    private RecyclerView rvRoomTables;

    private RoomTablesAdapter roomsTablesAdapter;

    private RoomsViewModel roomsViewModel;

    private TransactionsViewModel transactionsViewModel;

    private int transactionId = 0;
    private String isTransfer = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            transactionId = Integer.valueOf(bundle.getInt(AppConstants.TRANS_ID, 0));
            isTransfer = bundle.getString(AppConstants.TRANSFER);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initRoomsViewModel();
        initTransactionViewModel();
        initRoomListener();

        setRoomsTableAdapter();
    }

    private void initTransactionViewModel() {
        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    private void initRoomListener() {
        roomsViewModel.getRoomsLiveData().observe(this, new Observer<Rooms>() {
            @Override
            public void onChanged(Rooms rooms) {
                setRoomsTableAdapter();
            }
        });
    }

    private void initRoomsViewModel() {
        roomsViewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
    }

    private void initViews() {
        swipeRefreshRoom = findViewById(R.id.swipeRefreshRoom);
        rvRoomTables = findViewById(R.id.rvRoomTables);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }


    private void setRoomsTableAdapter() {
        try {
            roomsTablesAdapter = new RoomTablesAdapter(roomsViewModel.getRooms(), this, RoomsActivity.this);
            rvRoomTables.setLayoutManager(new GridLayoutManager(RoomsActivity.this, 5));
            rvRoomTables.setAdapter(roomsTablesAdapter);
            roomsTablesAdapter.notifyDataSetChanged();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void clicked(final Rooms rooms) {

        if (rooms.getStatus_id() != 222) {
            try {

                ChangeRoomStatusDialog changeRoomStatusDialog =
                        new ChangeRoomStatusDialog(RoomsActivity.this, roomsViewModel.getRoomStatus()) {
                            @Override
                            public void changeStatus(final int room_status_id, final String status_description, final String hex_color) {
                                if (room_status_id != rooms.getStatus_id()) {
                                    if (room_status_id == 12) {

                                        InputWithTimeDialog inputWithTimeDialog = new InputWithTimeDialog(RoomsActivity.this, "Reservation", rooms.getReservation_name()) {
                                            @Override
                                            public void confirm(String str1, String str2) {
                                                rooms.setStatus_description(status_description);
                                                rooms.setStatus_id(room_status_id);
                                                rooms.setHex_color(hex_color);

                                                rooms.setReservation_name(str1);
                                                rooms.setReservation_time(str2);
                                                rooms.setTime_reservation_made(Utils.getDateTimeToday());
                                                roomsViewModel.update(rooms);
                                            }
                                        };
                                        inputWithTimeDialog.show();

                                    } else {
                                        rooms.setStatus_description(status_description);
                                        rooms.setStatus_id(room_status_id);
                                        rooms.setHex_color(hex_color);

                                        rooms.setReservation_name("");
                                        rooms.setReservation_time("");
                                        rooms.setTime_reservation_made("");
                                        roomsViewModel.update(rooms);


                                    }
                                } else {
                                    Helper.showDialogMessage(RoomsActivity.this, "No change in room status", getString(R.string.header_message));
                                }




                            }
                        };
                changeRoomStatusDialog.show();
            } catch (Exception e) {

            }
        }

    }


    @Override
    public void showRates(int room_id) {
        try {
            final Rooms rooms = roomsViewModel.getRoomViaId(room_id);
            if (isTransfer.equalsIgnoreCase("y")) {
                if (rooms.getStatus_id() == 1) {
                    RoomRateSelectionDialog roomRateSelectionDialog = new RoomRateSelectionDialog(RoomsActivity.this, roomsViewModel.getRoomRates(room_id)) {
                        @Override
                        public void rateSelected(RoomRates roomRates) {
                            attachRoomToTransaction(roomRates.getRoom_id(), transactionId);
                            try {


                                voidRoomRate(transactionId);

                                Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(transactionId);

                                changeRoomStatus(tmpRm, 3, true);

                                changeRoomStatus(rooms, 2, false);

                                Intent intent = new Intent();
                                intent.putExtra("type", "room");
                                intent.putExtra("selected_room", GsonHelper.getGson().toJson(roomRates));
                                setResult(RESULT_OK, intent);
                                finish();


                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }



                        }
                    };
                    roomRateSelectionDialog.show();
                }
            } else {
                if (rooms.getStatus_id() == 1 || rooms.getStatus_id() == 2) {
                    Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(transactionId);

                    if (tmpRm != null) {
                        if (tmpRm.getRoom_id() == room_id) {
                            RoomRateSelectionDialog roomRateSelectionDialog = new RoomRateSelectionDialog(RoomsActivity.this, roomsViewModel.getRoomRates(room_id)) {
                                @Override
                                public void rateSelected(RoomRates roomRates) {
                                    attachRoomToTransaction(roomRates.getRoom_id(), transactionId);

                                    changeRoomStatus(rooms, 2, false);


                                    Intent intent = new Intent();
                                    intent.putExtra("type", "room");
                                    intent.putExtra("selected_room", GsonHelper.getGson().toJson(roomRates));
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            };
                            roomRateSelectionDialog.show();
                        }
                        else {
                            Helper.showDialogMessage(RoomsActivity.this, "You have selected another room, your room is " + tmpRm.getRoom_name(), getString(R.string.header_message));
                        }

                    } else {
                        RoomRateSelectionDialog roomRateSelectionDialog = new RoomRateSelectionDialog(RoomsActivity.this, roomsViewModel.getRoomRates(room_id)) {
                            @Override
                            public void rateSelected(RoomRates roomRates) {

                                attachRoomToTransaction(roomRates.getRoom_id(), transactionId);

                                changeRoomStatus(rooms, 2, false);

                                Intent intent = new Intent();
                                intent.putExtra("type", "room");
                                intent.putExtra("selected_room", GsonHelper.getGson().toJson(roomRates));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        };
                        roomRateSelectionDialog.show();
                    }



                } else {
                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                         Helper.showDialogMessage(RoomsActivity.this, "ROOM IN USE", getString(R.string.header_message));
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                         Helper.showDialogMessage(RoomsActivity.this, "TABLE IN USE", getString(R.string.header_message));
                    }

                }
            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showTableOptions(int room_id) {
        try {
            final Rooms rooms = roomsViewModel.getRoomViaId(room_id);
            if (isTransfer.equalsIgnoreCase("y")) {
                if (rooms.getStatus_id() == 1) {
                    Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(transactionId);

                    changeRoomStatus(tmpRm, 3, true);

                    changeRoomStatus(rooms, 2, false);

                    Intent intent = new Intent();
                    intent.putExtra("type", "table");
                    intent.putExtra("case", "transfer_table");
                    intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                if (transactionId != 0) {
                    //CHECK IF TRANSACTION IS ALREADY EXISTING ON OTHER ROOM
                    //IF NOT EXISTING ATTACH IT TO ROOM
                    //ELSE PROMT THAT IT IS ALREADY ATTACHED TO THIS ROOM
                    Rooms tmpRm = roomsViewModel.getRoomViaTransactionId(transactionId);

                    if (tmpRm == null) {

                        Log.d("CHECKDATA", "WITWIT");

                        if (rooms.getStatus_id() == 1 || rooms.getStatus_id() == 12) {
                            Log.d("CHECKDATA", "WITH TRANSACTION ATTACH TO ROOM");
                            attachRoomToTransaction(room_id, transactionId);
                            changeRoomStatus(rooms, 2, false);

                            Intent intent = new Intent();
                            intent.putExtra("type", "table");
                            intent.putExtra("case", "existing_trans_new_room");
                            intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (rooms.getStatus_id() == 2) {
                            Log.d("CHECKDATA", "WITH TRANSACTION BUT LOAD DATA OF SELECTED ROOM");
                            Intent intent = new Intent();
                            intent.putExtra("type", "table");
                            intent.putExtra("case", "load_existing_data");
                            intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                            setResult(RESULT_OK, intent);
                            //LOAD DETAILS OF SELECTED ROOM
                        } else {
                            Log.d("CHECKDATA", "WITH TRANSACTION BUT ROOM IS IN USE");
//                            Helper.showDialogMessage(RoomsActivity.this, "Room is in use", getString(R.string.header_message));
                        }

                    } else {
                        Log.d("CHECKDATA", "WITHOUT TRANSACTION ");
                        if (rooms.getRoom_name().equalsIgnoreCase(tmpRm.getRoom_name())) {
                            Log.d("CHECKDATA", "LOAD EXIST ");
                            Intent intent = new Intent();
                            intent.putExtra("type", "table");
                            intent.putExtra("case", "load_existing_data");
                            intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            if (rooms.getStatus_id() == 1 || rooms.getStatus_id() == 12) {
                                Log.d("CHECKDATA", "CREATE ");
                                changeRoomStatus(rooms, 2, false);

                                Intent intent = new Intent();
                                intent.putExtra("type", "table");
                                intent.putExtra("case", "new_trans_with_room");
                                intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                if (rooms.getStatus_id() == 2) {
                                    Log.d("CHECKDATA", "WITHOUT TRANSACTION BUT LOAD DATA OF SELECTED ROOM");
                                    Intent intent = new Intent();
                                    intent.putExtra("type", "table");
                                    intent.putExtra("case", "load_existing_data");
                                    intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    //LOAD DETAILS OF SELECTED ROOM
                                } else {
                                    Helper.showDialogMessage(RoomsActivity.this, "Your transaction is already attached to room " + tmpRm.getRoom_name(), getString(R.string.header_message));
                                    Log.d("CHECKDATA", "WITH TRANSACTION BUT ROOM IS IN USE");
//                            Helper.showDialogMessage(RoomsActivity.this, "Room is in use", getString(R.string.header_message));
                                }


                            }

                        }

                    }
                } else {


                    Log.d("CHECKDATA", "WALA TRANS ID");
                    if (rooms.getStatus_id() == 1 || rooms.getStatus_id() == 12) {
                        //CREATE TRANSACTION
                        changeRoomStatus(rooms, 2, false);

                        Intent intent = new Intent();
                        intent.putExtra("type", "table");
                        intent.putExtra("case", "new_trans_with_room");
                        intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                        setResult(RESULT_OK, intent);
                        finish();


                    } else if (rooms.getStatus_id() == 2) {
                        //LOAD DETAILS OF SELECTED ROOM
                        Intent intent = new Intent();
                        intent.putExtra("type", "table");
                        intent.putExtra("case", "load_existing_data");
                        intent.putExtra("selected_room", GsonHelper.getGson().toJson(rooms));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Helper.showDialogMessage(RoomsActivity.this, "Room is in use", getString(R.string.header_message));
                    }
                }


            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void attachRoomToTransaction(int room_id, int transactionId) {
        try {
            Rooms newRoom = roomsViewModel.getRoomViaId(room_id);
            Transactions tmpTr = transactionsViewModel.loadedTransactionList(String.valueOf(transactionId)).get(0);
            tmpTr.setId(transactionId);
            tmpTr.setRoom_id(newRoom.getRoom_id());
            tmpTr.setRoom_number(newRoom.getRoom_name());
            tmpTr.setCheck_in_time(Utils.getDateTimeToday());
            tmpTr.setIs_sent_to_server(0);
            transactionsViewModel.update(tmpTr);
        } catch (Exception e) {
            Log.d("UPDATING", e.getMessage());
        }

    }

    private void changeRoomStatus(Rooms room, int status_id, boolean is_transfer) {
        try {

            RoomStatus roomStatus = roomsViewModel.getRoomStatusViaId(status_id);
            room.setStatus_id(roomStatus.getCore_id());
            room.setStatus_description(roomStatus.getRoom_status());
            room.setHex_color(roomStatus.getHex_color());
            if (is_transfer) {
                room.setTransaction_id("");
                room.setCheck_in_time("");
            }
            roomsViewModel.update(room);
        } catch (Exception e) {

        }
    }

    private void voidRoomRate(int transactionId) {
        try {
            for (Orders orders  : transactionsViewModel.roomRateList(String.valueOf(transactionId))) {
                orders.setIs_sent_to_server(0);
                orders.setIs_void(true);
                transactionsViewModel.updateOrder(orders);
            }
        } catch (Exception e) {

        }

    }
}
