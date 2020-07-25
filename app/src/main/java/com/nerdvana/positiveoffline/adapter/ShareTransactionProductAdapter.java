package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.intf.StOrderContract;

import java.util.List;

public class ShareTransactionProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Orders> ordersList;
    private Context context;
    private StOrderContract stOrderContract;
    public ShareTransactionProductAdapter(List<Orders> ordersList, Context context,
                                          StOrderContract stOrderContract) {
        this.ordersList = ordersList;
        this.context = context;
        this.stOrderContract = stOrderContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ShareTransactionProductAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_st_products, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listItemName;
        private TextView listItemQty;
        private TextView listItemPrice;
        private LinearLayout rootView;
        private Button btnMove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.listItemName);
            listItemQty = itemView.findViewById(R.id.listItemQty);
            listItemPrice = itemView.findViewById(R.id.listItemPrice);
            rootView = itemView.findViewById(R.id.rootView);
            btnMove = itemView.findViewById(R.id.btnMove);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        final Orders model = ordersList.get(holder.getAdapterPosition());

        ((ViewHolder)holder).btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stOrderContract.clicked(holder.getAdapterPosition());
            }
        });
        ((ViewHolder)holder).listItemName.setText(
                String.format("%sx %s", String.valueOf(model.getQty()), model.getName())
        );
//        ((ViewHolder)holder).listItemQty.setText(String.valueOf(model.getQty()));
        ((ViewHolder)holder).listItemPrice.setText(String.valueOf(Utils.roundedOffFourDecimal(model.getAmount() * model.getQty())));




    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }



}
