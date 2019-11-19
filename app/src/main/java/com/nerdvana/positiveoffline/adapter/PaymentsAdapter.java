package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.intf.PaymentTypeContract;
import com.nerdvana.positiveoffline.intf.PaymentsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PaymentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Payments> paymentsList;
    private Context context;
    private PaymentsContract paymentsContract;
    public PaymentsAdapter(List<Payments> paymentsList, Context context, PaymentsContract paymentsContract) {
        this.paymentsList = paymentsList;
        this.context = context;
        this.paymentsContract = paymentsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PaymentsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_payments, viewGroup, false));
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

        final Payments model = paymentsList.get(holder.getAdapterPosition());
        ((PaymentsAdapter.ViewHolder)holder).name.setText(Utils.digitsWithComma(model.getAmount()));

        ((ViewHolder)holder).close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentsContract.clicked(model);
            }
        });

    }


    @Override
    public int getItemCount() {
        return paymentsList.size();
    }




}
