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
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TransactionWithOrders> transactionsList;
    private Context context;
    private TransactionsContract transactionsContract;
    public TransactionsAdapter(List<TransactionWithOrders> transactionsList, Context context, TransactionsContract transactionsContract) {
        this.transactionsList = transactionsList;
        this.context = context;
        this.transactionsContract = transactionsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TransactionsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_payments, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView close;
        private RelativeLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.rootView);
            close = itemView.findViewById(R.id.close);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final TransactionWithOrders model = transactionsList.get(holder.getAdapterPosition());
        ((TransactionsAdapter.ViewHolder)holder).name.setText(model.transactions.getControl_number());

        ((TransactionsAdapter.ViewHolder)holder).close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionsContract.clicked(model);
            }
        });

    }


    @Override
    public int getItemCount() {
        return transactionsList.size();
    }


}
