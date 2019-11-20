package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.entities.Transactions;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.intf.TransactionsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResumeTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Transactions> transactionList;
    private List<Transactions> transactionFilteredList;
    private Context context;
    private TransactionsContract transactionsContract;
    public ResumeTransactionAdapter(List<Transactions> transactionList, Context context, TransactionsContract transactionsContract) {
        this.transactionList = new ArrayList<>(transactionList);
        this.context = context;
        transactionFilteredList = new ArrayList<>(transactionList);
        this.transactionsContract = transactionsContract;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ResumeTransactionAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_resume_trans, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                transactionFilteredList = new ArrayList<>();
                if (transactionList.size() > 0) {
                    if (charSting.isEmpty()) {
                        transactionFilteredList = transactionList;
                    } else {
                        List<Transactions> filteredList = new ArrayList<>();
                        for (Transactions pm : transactionList) {
                            if (pm.getTrans_name().toLowerCase().contains(charSting.toLowerCase())) {
                                filteredList.add(pm);
                            }
                        }
                        transactionFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                transactionFilteredList = (ArrayList<Transactions>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private CardView rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        final Transactions model = transactionFilteredList.get(i);
        ((ResumeTransactionAdapter.ViewHolder)holder).name.setText(TextUtils.isEmpty(model.getTrans_name()) ? model.getControl_number() : model.getTrans_name());
        ((ViewHolder)holder).price.setText(String.format("CTRL NO: %s", model.getControl_number()));
        ((ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionsContract.clicked(model);
            }
        });
    }


    public void addItems(List<Transactions> productsModelList) {
        this.transactionList = new ArrayList<>(productsModelList);
        this.transactionFilteredList = productsModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return transactionFilteredList.size();
    }

}
