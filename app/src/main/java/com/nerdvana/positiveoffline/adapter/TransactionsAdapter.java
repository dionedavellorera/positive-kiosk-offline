package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.nerdvana.positiveoffline.model.TransactionWithOrders;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<TransactionWithOrders> transactionsList;
    private List<TransactionWithOrders> transactionsFilteredList;
    private Context context;
    private TransactionsContract transactionsContract;
    private Boolean forVoid;
    public TransactionsAdapter(
            List<TransactionWithOrders> transactionsList,
            Context context,
            TransactionsContract transactionsContract,
            Boolean forVoid) {
        this.transactionsList = transactionsList;
        this.context = context;
        this.transactionsContract = transactionsContract;
        transactionsFilteredList = transactionsList;
        this.forVoid = forVoid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TransactionsAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_transactions, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                transactionsFilteredList = new ArrayList<>();
                if (transactionsList.size() > 0) {
                    if (charSting.isEmpty()) {
                        transactionsFilteredList = transactionsList;
                    } else {
                        List<TransactionWithOrders> filteredList = new ArrayList<>();
                        for (TransactionWithOrders pm : transactionsList) {
                            if (pm.transactions.getReceipt_number().toLowerCase().contains(charSting.toLowerCase())) {
                                filteredList.add(pm);
                            }
                        }
                        transactionsFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                transactionsFilteredList = (ArrayList<TransactionWithOrders>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView controlNumberValue;
        private TextView receiptNumberValue;
        private TextView listItemTotal;
        private TextView dateValue;
        private LinearLayout linOrders;
        private Button btnVoidTransaction;
//        private ImageView close;
//        private RelativeLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            controlNumberValue = itemView.findViewById(R.id.controlNumberValue);
            listItemTotal = itemView.findViewById(R.id.listItemTotal);
            receiptNumberValue = itemView.findViewById(R.id.receiptNumberValue);
            linOrders = itemView.findViewById(R.id.linOrders);
            btnVoidTransaction = itemView.findViewById(R.id.btnVoidTransaction);
            dateValue = itemView.findViewById(R.id.dateValue);
//            rootView = itemView.findViewById(R.id.rootView);
//            close = itemView.findViewById(R.id.close);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        ((ViewHolder)holder).linOrders.removeAllViews();
        final TransactionWithOrders model = transactionsFilteredList.get(holder.getAdapterPosition());
        ((ViewHolder)holder).controlNumberValue.setText(model.transactions.getControl_number());
        ((ViewHolder)holder).receiptNumberValue.setText(model.transactions.getReceipt_number());
        ((ViewHolder)holder).dateValue.setText(model.transactions.getCompleted_at());


        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        LinearLayout.LayoutParams llp1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f);

        LinearLayout.LayoutParams linearParams0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        LinearLayout headerLayout = new LinearLayout(
                context);
        headerLayout.setLayoutParams(linearParams0);
        headerLayout.addView(custTextView(context, "ITEM", llp, Gravity.LEFT));
        headerLayout.addView(custTextView(context, "QTY", llp, Gravity.CENTER_VERTICAL| Gravity.CENTER));
        headerLayout.addView(custTextView(context, "AMOUNT", llp1, Gravity.RIGHT|Gravity.CENTER_VERTICAL));
        addView(((ViewHolder)holder).linOrders, headerLayout);
        Double totalAmount = 0.00;

        for (Orders order : model.ordersList) {


            LinearLayout orderLayout = new LinearLayout(
                    context);
            orderLayout.setLayoutParams(linearParams0);
            orderLayout.addView(custTextView(context, order.getName(), llp, Gravity.LEFT));
            orderLayout.addView(custTextView(context, String.valueOf(order.getQty()), llp, Gravity.CENTER_VERTICAL| Gravity.CENTER));
            orderLayout.addView(custTextView(context, Utils.digitsWithComma(order.getAmount() * order.getQty()), llp1, Gravity.RIGHT|Gravity.CENTER_VERTICAL));
            addView(((ViewHolder)holder).linOrders, orderLayout);

            totalAmount += order.getAmount() * order.getQty();

        }


        ((ViewHolder)holder).listItemTotal.setText(String.format("TOTAL : %s", Utils.digitsWithComma(totalAmount)));

        ((TransactionsAdapter.ViewHolder)holder).btnVoidTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionsContract.clicked(model);
            }
        });

        if (!forVoid) {

            ((TransactionsAdapter.ViewHolder)holder).btnVoidTransaction.setText("REPRINT");

        } else {

            ((TransactionsAdapter.ViewHolder)holder).btnVoidTransaction.setText("VOID");

        }

    }

    private void addView(LinearLayout linearLayout, View childLayout) {

        linearLayout.addView(childLayout);


    }

    private TextView custTextView(Context context, String data, ViewGroup.LayoutParams llp, int gravity) {
        TextView v = new TextView(context);
        v.setGravity(gravity);
        v.setPadding(5, 5, 5, 5);
        v.setText(data.toUpperCase());
        v.setLayoutParams(llp);
        return v;
    }


    @Override
    public int getItemCount() {
        return transactionsFilteredList.size();
    }


}
