package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.intf.DiscountsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.DiscountWithSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscountMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiscountWithSettings> discountWithSettingsList;
    private DiscountsContract discountsContract;
    public DiscountMenuAdapter(List<DiscountWithSettings> discountWithSettingsList, Context context, DiscountsContract discountsContract) {
        this.discountWithSettingsList = discountWithSettingsList;
        this.discountsContract = discountsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DiscountMenuAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_buttons, viewGroup, false));
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
//        private CardView rootView;
        private RelativeLayout relView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
//            rootView = itemView.findViewById(R.id.rootView);
            relView = itemView.findViewById(R.id.relView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final DiscountWithSettings model = discountWithSettingsList.get(holder.getAdapterPosition());

        ((ViewHolder)holder).relView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    discountsContract.clicked(model);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ((DiscountMenuAdapter.ViewHolder)holder).name.setText(model.discounts.getDiscount_card());

    }

    @Override
    public int getItemCount() {
        return discountWithSettingsList.size();
    }





}
