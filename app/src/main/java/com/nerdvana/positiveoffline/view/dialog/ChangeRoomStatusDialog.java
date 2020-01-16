package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.adapter.ChangeRoomStatusAdapter;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.intf.Action;

import java.util.List;

public abstract class ChangeRoomStatusDialog extends BaseDialog implements Action {

    private RecyclerView rvStatusList;
    private List<RoomStatus> roomStatusList;
    public ChangeRoomStatusDialog(Context context, List<RoomStatus> roomStatusList) {
        super(context);
        this.roomStatusList = roomStatusList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_change_room_status, "CHANGE ROOM STATUS");
        initViews();

        ChangeRoomStatusAdapter changeRoomStatusAdapter = new ChangeRoomStatusAdapter(roomStatusList, this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvStatusList.setLayoutManager(llm);
        rvStatusList.setAdapter(changeRoomStatusAdapter);
        changeRoomStatusAdapter.notifyDataSetChanged();
    }

    public abstract void changeStatus(int room_status_id, String status_description, String hex_color);

    private void initViews() {
        rvStatusList = findViewById(R.id.rvStatusList);
    }

    @Override
    public void clicked(int room_status_id, String status_description, String hex_color) {
        changeStatus(room_status_id, status_description, hex_color);
        dismiss();
    }
}
