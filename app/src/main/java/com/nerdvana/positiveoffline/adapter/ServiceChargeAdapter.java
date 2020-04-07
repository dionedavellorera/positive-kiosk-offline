package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.entities.ServiceCharge;
import com.nerdvana.positiveoffline.intf.ServiceChargeContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ServiceChargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ServiceCharge> serviceChargeList;
    private Context context;
    private ServiceChargeContract serviceChargeContract;
    public ServiceChargeAdapter(List<ServiceCharge> serviceChargeList, Context context,
                                ServiceChargeContract serviceChargeContract) {
        this.serviceChargeList = serviceChargeList;
        this.context = context;
        this.serviceChargeContract = serviceChargeContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ServiceChargeAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_service_charge, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvStatus;
        private LinearLayout rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            rootView = itemView.findViewById(R.id.rootView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        final ServiceCharge model = serviceChargeList.get(holder.getAdapterPosition());

        ((ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceChargeContract.clicked(model, holder.getAdapterPosition());
            }
        });


        if (model.isIs_percentage()) {
            ((ServiceChargeAdapter.ViewHolder)holder).tvName.setText(String.valueOf(model.getValue()) + "%");
        } else {
            ((ServiceChargeAdapter.ViewHolder)holder).tvName.setText(String.valueOf(model.getValue()));
        }

        if (model.isIs_selected()) {
            ((ViewHolder)holder).tvStatus.setText("ACTIVE");
        } else {
            ((ViewHolder)holder).tvStatus.setText("NOT ACTIVE");
        }





    }


    @Override
    public int getItemCount() {
        return serviceChargeList.size();
    }
}
