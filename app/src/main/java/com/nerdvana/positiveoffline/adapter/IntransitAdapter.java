package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class IntransitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TransactionWithOrders> transactionWithOrdersList;
    private Context context;
    private PaymentsContract paymentsContract;
    public IntransitAdapter(List<TransactionWithOrders> transactionWithOrdersList, Context context) {
        this.transactionWithOrdersList = transactionWithOrdersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new IntransitAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_intransit, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView total;
        private TextView dateCreated;
        private TextView elapsed;
        private TextView items;
        private TextView name;
        private RelativeLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            total = itemView.findViewById(R.id.total);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            elapsed = itemView.findViewById(R.id.elapsed);
            items = itemView.findViewById(R.id.items);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final TransactionWithOrders model = transactionWithOrdersList.get(holder.getAdapterPosition());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(model.transactions.getSaved_at());

        ((IntransitAdapter.ViewHolder)holder).name.setText(model.transactions.getTrans_name().toUpperCase());
        ((IntransitAdapter.ViewHolder)holder).total.setText(String.valueOf(model.transactions.getGross_sales()));
        ((IntransitAdapter.ViewHolder)holder).dateCreated.setText(model.transactions.getSaved_at());
        ((IntransitAdapter.ViewHolder)holder).elapsed.setText(String.valueOf(Minutes.minutesBetween(dt, new DateTime()).getMinutes()) + " MINS");
        ((IntransitAdapter.ViewHolder)holder).items.setText(String.valueOf(model.ordersList.size()));

    }


    @Override
    public int getItemCount() {
        return transactionWithOrdersList.size();
    }


}
