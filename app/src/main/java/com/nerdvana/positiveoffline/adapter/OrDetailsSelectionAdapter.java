package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.OrDetails;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.OrDetailsContract;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrDetailsSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<OrDetails> orDetailsList;
    private List<OrDetails> orDetailsFilteredList;
    private OrDetailsContract orDetailsContract;
    private Context context;
    public OrDetailsSelectionAdapter(List<OrDetails> orDetailsList, OrDetailsContract orDetailsContract, Context context) {
        this.orDetailsList = new ArrayList<>(orDetailsList);
        this.context = context;
        this.orDetailsContract = orDetailsContract;
        this.orDetailsFilteredList = new ArrayList<>(orDetailsList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrDetailsSelectionAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_or_details_selection, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                orDetailsFilteredList = new ArrayList<>();
                if (orDetailsList.size() > 0) {
                    if (charSting.isEmpty()) {
                        orDetailsFilteredList = orDetailsList;
                    } else {
                        List<OrDetails> filteredList = new ArrayList<>();
                        for (OrDetails pm : orDetailsList) {
                            if (pm.getName().toLowerCase().contains(charSting.toLowerCase()) ||
                                    pm.getAddress().contains(charSting.toLowerCase()) ||
                                    pm.getBusiness_style().toLowerCase().contains(charSting.toLowerCase()) ||
                                    pm.getTin_number().toLowerCase().contains(charSting.toLowerCase())) {
                                filteredList.add(pm);
                            }
                        }
                        orDetailsFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orDetailsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                orDetailsFilteredList = (ArrayList<OrDetails>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvBusinessStyle;
        private TextView tvTinNumber;
        private LinearLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvBusinessStyle = itemView.findViewById(R.id.tvBusinessStyle);
            tvTinNumber = itemView.findViewById(R.id.tvTinNumber);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        final OrDetails model = orDetailsFilteredList.get(i);
        ((ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orDetailsContract.clicked(model);
            }
        });
        ((ViewHolder)holder).tvName.setText(model.getName().toUpperCase());
        ((ViewHolder)holder).tvAddress.setText(model.getAddress().toUpperCase());
        ((ViewHolder)holder).tvTinNumber.setText(model.getTin_number().toUpperCase());
        ((ViewHolder)holder).tvBusinessStyle.setText(model.getBusiness_style().toUpperCase());


    }

    @Override
    public int getItemCount() {
        return orDetailsFilteredList.size();
    }


}
