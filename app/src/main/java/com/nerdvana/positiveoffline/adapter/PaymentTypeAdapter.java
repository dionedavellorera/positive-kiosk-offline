package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
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
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.intf.PaymentTypeContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PaymentTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PaymentTypes> paymentTypesList;
    private Context context;
    private PaymentTypeContract paymentTypeContract;
    public PaymentTypeAdapter(List<PaymentTypes> paymentTypesList, Context context,
                              PaymentTypeContract paymentTypeContract) {
        this.paymentTypesList = paymentTypesList;
        this.context = context;
        this.paymentTypeContract = paymentTypeContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PaymentTypeAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_payment_types, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private CardView rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.rootView);
            image = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final PaymentTypes model = paymentTypesList.get(holder.getAdapterPosition());

        ((PaymentTypeAdapter.ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentTypeContract.clicked(model);
            }
        });

        if (model.getCore_id() != 1 && model.getCore_id() != 2 && model.getCore_id() != 999) {
            ((ViewHolder)holder).rootView.getLayoutParams().height = 0;
        }

        ((ViewHolder)holder).name.setText(model.getPayment_type());

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PAYMENT_TYPE/" + model.getImage_url());
        Picasso.get().load(direct).into(((PaymentTypeAdapter.ViewHolder)holder).image);


    }


    @Override
    public int getItemCount() {
        return paymentTypesList.size();
    }


}
