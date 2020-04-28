package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.nerdvana.positiveoffline.intf.StringSelectionContract;
import com.nerdvana.positiveoffline.model.StringModel;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<StringModel> stringList;
    private List<StringModel> stringFilteredList;
    private StringSelectionContract stringSelectionContract;
    private Context context;
    private boolean isDarkMode;
    public StringAdapter(List<StringModel> strList, StringSelectionContract stringSelectionContract,
                           Context context, boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        this.stringList = new ArrayList<>(strList);
        this.context = context;
        this.stringSelectionContract = stringSelectionContract;
        stringFilteredList = new ArrayList<>(strList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StringAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_strings, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                synchronized (filterResults) {
                    if (constraint != null) {
                        String charSting = constraint.toString();

                        stringFilteredList = new ArrayList<>();
                        if (stringList.size() > 0) {
                            if (charSting.isEmpty()) {
                                stringFilteredList = stringList;
                            } else {
                                List<StringModel> filteredList = new ArrayList<>();
                                for (StringModel pm : stringList) {
                                    if (pm.getString().toLowerCase().contains(charSting.toLowerCase())) {
                                        filteredList.add(pm);
                                    }
                                }
                                stringFilteredList = filteredList;
                            }
                        }

                        filterResults.values = stringFilteredList;

                    }
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                stringFilteredList = (ArrayList<StringModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

//        if(holder instanceof ChangeRoomStatusAdapter.ListViewHolder){
//
//        }
        final StringModel model = stringFilteredList.get(i);

        TooltipCompat.setTooltipText(((StringAdapter.ViewHolder)holder).name, model.getString().toUpperCase());


        ((StringAdapter.ViewHolder)holder).name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringSelectionContract.clicked(model);
            }
        });

        ((StringAdapter.ViewHolder)holder).name.setText(model.getString().toUpperCase());


        if (!isDarkMode) {
            ((StringAdapter.ViewHolder)holder).name.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            ((StringAdapter.ViewHolder)holder).name.setTextColor(Color.BLACK);
            ;
        } else {
            ((StringAdapter.ViewHolder)holder).name.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
            ((StringAdapter.ViewHolder)holder).name.setTextColor(Color.WHITE);
        }

    }


    @Override
    public int getItemCount() {
        return stringFilteredList.size();
    }


}
