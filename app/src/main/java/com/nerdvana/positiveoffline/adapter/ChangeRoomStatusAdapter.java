package com.nerdvana.positiveoffline.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.RoomStatus;
import com.nerdvana.positiveoffline.intf.Action;

import java.util.List;

public class ChangeRoomStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoomStatus> statusList;
    private Action actionContract;
    public ChangeRoomStatusAdapter(List<RoomStatus> statusList, Action actionContract) {
        this.statusList = statusList;
        this.actionContract = actionContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChangeRoomStatusAdapter.ListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_new_status, viewGroup, false));
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private RelativeLayout rootView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.rootView);
        }

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        if(holder instanceof ChangeRoomStatusAdapter.ListViewHolder){
            final RoomStatus model = statusList.get(holder.getAdapterPosition());
            ((ChangeRoomStatusAdapter.ListViewHolder) holder).name.setText(model.getRoom_status().toUpperCase());

            ((ChangeRoomStatusAdapter.ListViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionContract.clicked(
                            model.getCore_id(),
                            model.getRoom_status(),
                            model.getHex_color());
                }
            });


//            //CLEAN, DIRTY, PREVENTIVE MAINTENANCE only for change status
//            if (statusList.get(i).getCoreId() != 1 && statusList.get(i).getCoreId() != 3 &&
//                    statusList.get(i).getCoreId() != 35) {
//
//                ((ListViewHolder) holder).rootView.setVisibility(View.GONE);
//                ((ListViewHolder) holder).rootView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//
//            } else {
//                ((ListViewHolder) holder).rootView.setVisibility(View.VISIBLE);
//                ((ListViewHolder) holder).rootView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                ((ChangeRoomStatusAdapter.ListViewHolder) holder).name.setText(statusList.get(i).getRoomStatus());
//
//                ((ChangeRoomStatusAdapter.ListViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        newRoomStatus.clicked(statusList.get(i).getCoreId(), statusList.get(i).getRoomStatus());
//                    }
//                });
//            }




        }
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }
}
