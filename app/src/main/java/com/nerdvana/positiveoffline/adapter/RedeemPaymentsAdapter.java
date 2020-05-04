package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.intf.PaymentsContract;

import java.util.List;

public class RedeemPaymentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Payments> paymentsList;
    private Context context;
    private PaymentsContract paymentsContract;
    public RedeemPaymentsAdapter(List<Payments> paymentsList, Context context, PaymentsContract paymentsContract) {
        this.paymentsList = paymentsList;
        this.context = context;
        this.paymentsContract = paymentsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RedeemPaymentsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_redeem_payments, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView value;
        private Button redeem;
        private RelativeLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            value = itemView.findViewById(R.id.value);
            rootView = itemView.findViewById(R.id.rootView);
            redeem = itemView.findViewById(R.id.redeem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        final Payments model = paymentsList.get(holder.getAdapterPosition());
        ((RedeemPaymentsAdapter.ViewHolder)holder).value.setText(Utils.digitsWithComma(model.getAmount()));
        ((RedeemPaymentsAdapter.ViewHolder)holder).name.setText(model.getName().toUpperCase());

        ((RedeemPaymentsAdapter.ViewHolder)holder).redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentsContract.clicked(model, ((RedeemPaymentsAdapter.ViewHolder)holder).redeem);
            }
        });



    }


    @Override
    public int getItemCount() {
        return paymentsList.size();
    }

}
