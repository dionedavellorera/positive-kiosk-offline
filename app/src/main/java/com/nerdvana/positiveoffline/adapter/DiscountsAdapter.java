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
import com.nerdvana.positiveoffline.entities.OrderDiscounts;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.intf.DiscountsContract;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.nerdvana.positiveoffline.model.OrderWithDiscounts;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;

import java.util.List;

public class DiscountsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TransactionWithDiscounts> transactionWithDiscounts;
    private Context context;
    private DiscountsContract discountsContract;
    public DiscountsAdapter(List<TransactionWithDiscounts> transactionWithDiscounts, Context context,
                            DiscountsContract discountsContract) {
        this.transactionWithDiscounts = transactionWithDiscounts;
        this.context = context;
        this.discountsContract = discountsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DiscountsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_discounts, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView value;
        private ImageView close;
        private RelativeLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            value = itemView.findViewById(R.id.value);
            rootView = itemView.findViewById(R.id.rootView);
            close = itemView.findViewById(R.id.close);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final TransactionWithDiscounts model = transactionWithDiscounts.get(holder.getAdapterPosition());

        ((ViewHolder)holder).name.setText(model.getPosted_discount_name());

        ((ViewHolder)holder).value.setText(String.valueOf(model.getPercentage()) + "%");


        ((DiscountsAdapter.ViewHolder)holder).close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discountsContract.clicked(model);
            }
        });

    }


    @Override
    public int getItemCount() {
        return transactionWithDiscounts.size();
    }


}
