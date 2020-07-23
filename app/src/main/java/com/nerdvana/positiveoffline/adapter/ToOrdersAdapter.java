package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.Helper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.apiresponses.FetchPosToTransactions;
import com.nerdvana.positiveoffline.entities.Rooms;
import com.nerdvana.positiveoffline.intf.RoomContract;
import com.nerdvana.positiveoffline.intf.ToTransactionsContract;
import com.nerdvana.positiveoffline.intf.TransactionsContract;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ToOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<FetchPosToTransactions.ToTransactionsData> orderList;
    private List<FetchPosToTransactions.ToTransactionsData> orderFilteredList;

    private ToTransactionsContract toTransactionsContract;
    private Context context;
    public ToOrdersAdapter(List<FetchPosToTransactions.ToTransactionsData> orderList, Context context,
                           ToTransactionsContract toTransactionsContract) {
        this.orderList = new ArrayList<>(orderList);
        this.orderFilteredList = new ArrayList<>(orderList);
        this.context = context;
        this.toTransactionsContract = toTransactionsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ToOrdersAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_to_orders, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                orderFilteredList = new ArrayList<>();
                if (charSting.isEmpty()) {
                    orderFilteredList = orderList;
                } else {
                    List<FetchPosToTransactions.ToTransactionsData> filteredList = new ArrayList<>();
                    for (FetchPosToTransactions.ToTransactionsData pm : orderList) {
                        if (pm.getTransactions().getControlNumber().contains(charSting)) {
                            filteredList.add(pm);
                        }
                    }
                    orderFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = orderFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                orderFilteredList = (ArrayList<FetchPosToTransactions.ToTransactionsData>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView status;
        private RelativeLayout rel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            rel = itemView.findViewById(R.id.rel);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        final FetchPosToTransactions.ToTransactionsData model = orderFilteredList.get(i);
        ((ToOrdersAdapter.ViewHolder)holder).name.setText(model.getTransactions().getControlNumber().toUpperCase());
        ((ViewHolder)holder).rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTransactionsContract.clicked(model);
            }
        });



    }


    @Override
    public int getItemCount() {
        return orderFilteredList.size();
    }


}
