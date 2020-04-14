package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.intf.RoomContract;
import com.nerdvana.positiveoffline.view.rooms.RoomsActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RoomTablesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Rooms> roomTableModelList;
    private List<Rooms> roomsFilteredList;
    private RoomContract roomContract;

    private Context context;
    public RoomTablesAdapter(List<Rooms> roomTableModelList, Context context,
                             RoomContract roomContract) {
        this.roomsFilteredList = new ArrayList<>(roomTableModelList);
        this.roomTableModelList = new ArrayList<>(roomTableModelList);
        this.context = context;
        this.roomContract = roomContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RoomTablesAdapter.ProductsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_roomtables, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                roomsFilteredList = new ArrayList<>();
                if (charSting.isEmpty()) {
                    roomsFilteredList = roomTableModelList;
                } else {
                    List<Rooms> filteredList = new ArrayList<>();
                    for (Rooms pm : roomTableModelList) {
                        if (pm.getRoom_name().contains(charSting)) {
                            filteredList.add(pm);
                        }
                    }
                    roomsFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = roomsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                roomsFilteredList = (ArrayList<Rooms>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView duration;
        private TextView status;
        private RelativeLayout rel;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            duration = itemView.findViewById(R.id.duration);
            status = itemView.findViewById(R.id.status);
            rel = itemView.findViewById(R.id.rel);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        final Rooms rooms = roomsFilteredList.get(i);

        if (rooms.getStatus_id() == 12) {
            ((ProductsViewHolder)holder).name.setText(String.format("%s\nFor:%s\nTime:%s", rooms.getRoom_name(), rooms.getReservation_name(), rooms.getReservation_time()));
        } else {
            ((ProductsViewHolder)holder).name.setText(rooms.getRoom_name());
        }


        ((ProductsViewHolder)holder).status.setText(rooms.getStatus_description());

        ((ProductsViewHolder)holder).rel.setBackgroundColor(Color.parseColor(rooms.getHex_color()));
        ((ProductsViewHolder)holder).rel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                roomContract.clicked(rooms);
                return false;
            }
        });

        ((ProductsViewHolder)holder).rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                    roomContract.showRates(rooms.getRoom_id());
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                    roomContract.showTableOptions(rooms.getRoom_id());
                }



            }
        });

        if (!rooms.getCheck_in_time().isEmpty()) {
            ((ProductsViewHolder)holder).duration.setVisibility(View.VISIBLE);
            ((ProductsViewHolder)holder).duration.setText(Helper.durationOfStay(new DateTime().toString("yyyy-MM-dd HH:mm:ss"), rooms.getCheck_in_time()));
        } else {
            ((ProductsViewHolder)holder).duration.setVisibility(View.GONE);
        }
//        duration




    }


    @Override
    public int getItemCount() {
        return roomsFilteredList.size();
    }


}

