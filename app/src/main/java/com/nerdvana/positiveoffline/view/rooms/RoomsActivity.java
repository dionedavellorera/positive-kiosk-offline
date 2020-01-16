package com.nerdvana.positiveoffline.view.rooms;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.RoomTablesAdapter;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.intf.RoomContract;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.nerdvana.positiveoffline.view.dialog.ChangeRoomStatusDialog;
import com.nerdvana.positiveoffline.view.sync.SyncActivity;
import com.nerdvana.positiveoffline.viewmodel.RoomsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoomsActivity extends AppCompatActivity implements RoomContract {

    private SwipeRefreshLayout swipeRefreshRoom;
    private RecyclerView rvRoomTables;

    private RoomTablesAdapter roomsTablesAdapter;

    private RoomsViewModel roomsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        initRoomsViewModel();
        initRoomListener();

        setRoomsTableAdapter();
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
        try {

            ChangeRoomStatusDialog changeRoomStatusDialog =
                    new ChangeRoomStatusDialog(RoomsActivity.this, roomsViewModel.getRoomStatus()) {
                        @Override
                        public void changeStatus(int room_status_id, String status_description, String hex_color) {
                            rooms.setStatus_description(status_description);
                            rooms.setStatus_id(room_status_id);
                            rooms.setHex_color(hex_color);
                            roomsViewModel.update(rooms);


                        }
                    };
            changeRoomStatusDialog.show();


        } catch (Exception e) {

        }




    }
}
