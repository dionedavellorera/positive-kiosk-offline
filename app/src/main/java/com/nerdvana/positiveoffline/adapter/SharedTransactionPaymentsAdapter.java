package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Payments;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.intf.SharedTransactionsContract;
import com.nerdvana.positiveoffline.model.StPaymentsModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class SharedTransactionPaymentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StPaymentsModel> stPaymentsList;
    private Context context;
    private SharedTransactionsContract sharedTransactionsContract;
    public SharedTransactionPaymentsAdapter(List<StPaymentsModel> stPaymentsList, Context context,
                                            SharedTransactionsContract sharedTransactionsContract) {
        this.stPaymentsList = stPaymentsList;
        this.context = context;
        this.sharedTransactionsContract = sharedTransactionsContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SharedTransactionPaymentsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_st_payments, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listItemName;
        private TextView stOrderTotal;
        private TextView stOrderTendered;
        private TextView stOrderChange;
        private LinearLayout paymentContainer;
        private LinearLayout orderContainer;
        private LinearLayout rootView;
        private LinearLayout paymentTypeContainer;
        private Button btnCash;
        private Button btnCard;
        private Button btnClose;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.listItemName);
            stOrderTotal = itemView.findViewById(R.id.stOrderTotal);
            stOrderTendered = itemView.findViewById(R.id.stOrderTendered);
            stOrderChange = itemView.findViewById(R.id.stOrderChange);
            paymentContainer = itemView.findViewById(R.id.paymentContainer);
            orderContainer = itemView.findViewById(R.id.orderContainer);
            rootView = itemView.findViewById(R.id.rootView);
            paymentTypeContainer = itemView.findViewById(R.id.paymentTypeContainer);
            btnCash = itemView.findViewById(R.id.btnCash);
            btnCard = itemView.findViewById(R.id.btnCard);
            btnClose = itemView.findViewById(R.id.btnClose);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        Double orderTotal = 0.00;
        Double tenderedTotal = 0.00;
        Double changeTotal = 0.00;
        final StPaymentsModel model = stPaymentsList.get(holder.getAdapterPosition());

        if (model.isSelected()) {
            ((ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorLtGrey));
        } else {
            ((ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        if (model.getOrdersList().size() > 0) {
            ((ViewHolder)holder).paymentTypeContainer.setVisibility(View.VISIBLE);
        } else {
            ((ViewHolder)holder).paymentTypeContainer.setVisibility(View.GONE);
        }

        ((ViewHolder)holder).listItemName.setText(model.getName());

        ((ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTransactionsContract.customerClicked(holder.getAdapterPosition());
            }
        });

        ((ViewHolder)holder).btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTransactionsContract.customerRemoved(holder.getAdapterPosition());
            }
        });

        ((ViewHolder)holder).btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTransactionsContract.cashClicked(holder.getAdapterPosition());
            }
        });

        ((ViewHolder)holder).btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedTransactionsContract.cardClicked(holder.getAdapterPosition());
            }
        });

        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ((ViewHolder)holder).orderContainer.removeAllViews();
        int counter = 0;
        for (Orders ord : model.getOrdersList()) {
            orderTotal += ord.getQty() * ord.getAmount();
            final View child = mLayoutInflater.inflate(R.layout.list_item_selected_st_products, null, false);
            child.setTag(counter);
            TextView liName = child.findViewById(R.id.listItemName);
            liName.setText(String.format("%sx %s", String.valueOf(ord.getQty()), ord.getName()));
//            TextView liQty = child.findViewById(R.id.listItemQty);
//            liQty.setText(String.valueOf(ord.getQty()));
            TextView liPrice = child.findViewById(R.id.listItemPrice);
            liPrice.setText(String.valueOf(ord.getQty() * ord.getAmount()));
            Button btnRemoveQty = child.findViewById(R.id.btnRemove);
            btnRemoveQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedTransactionsContract.orderRemovedClicked(holder.getAdapterPosition(), (int) (child.getTag()));
                }
            });
            ((ViewHolder)holder).orderContainer.addView(child);
            counter++;
        }

        ((ViewHolder)holder).paymentContainer.removeAllViews();
        int counter1 = 0;
        for (Payments pym : model.getPaymentsList()) {
            tenderedTotal += pym.getAmount();
            final View child = mLayoutInflater.inflate(R.layout.list_item_selected_st_payments, null, false);
            child.setTag(counter1);
            TextView liName = child.findViewById(R.id.listItemName);
            liName.setText(pym.getName());
            TextView liPrice = child.findViewById(R.id.listItemPrice);
            liPrice.setText(String.valueOf(pym.getAmount()));
            Button btnRemoveQty = child.findViewById(R.id.btnRemove);
            btnRemoveQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedTransactionsContract.paymentRemovedClicked(holder.getAdapterPosition(), (int) (child.getTag()));
                }
            });
            ((ViewHolder)holder).paymentContainer.addView(child);
            counter1++;
        }

        changeTotal = tenderedTotal - orderTotal;

        ((ViewHolder)holder).stOrderTotal.setText(String.valueOf("TOTAL:" + orderTotal));
        ((ViewHolder)holder).stOrderTendered.setText(String.valueOf("TENDERED:" + tenderedTotal));
        ((ViewHolder)holder).stOrderChange.setText(String.valueOf("CHANGE:" + changeTotal));




    }


    @Override
    public int getItemCount() {
        return stPaymentsList.size();
    }



}
