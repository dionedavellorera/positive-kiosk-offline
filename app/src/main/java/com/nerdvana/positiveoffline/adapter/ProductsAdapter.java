package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
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
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Products> productsModelList;
    private List<Products> productsFilteredList;
    private ProductsContract productsContract;
    private Context context;
    public ProductsAdapter(List<Products> productsModelList, ProductsContract productsContract, Context context) {
        this.productsModelList = new ArrayList<>(productsModelList);
        this.context = context;
        this.productsContract = productsContract;
        productsFilteredList = new ArrayList<>(productsModelList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProductsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_products, viewGroup, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSting = constraint.toString();
                productsFilteredList = new ArrayList<>();
                if (productsModelList.size() > 0) {
                    if (charSting.isEmpty()) {
                        productsFilteredList = productsModelList;
                    } else {
                        List<Products> filteredList = new ArrayList<>();
                        for (Products pm : productsModelList) {
                            if (pm.getProduct().toLowerCase().contains(charSting.toLowerCase()) ||
                                    String.valueOf(pm.getAmount()).contains(charSting.toLowerCase()) ||
                                    pm.getDepartment().toLowerCase().contains(charSting.toLowerCase())) {
                                filteredList.add(pm);
                            }
                        }
                        productsFilteredList = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productsFilteredList = (ArrayList<Products>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    static class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private ImageView imageUrl;
        private CardView rootView;
        private RelativeLayout productBg;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            imageUrl = itemView.findViewById(R.id.image);
            rootView = itemView.findViewById(R.id.rootView);
            productBg = itemView.findViewById(R.id.productBg);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {




        final Products productsModel = productsFilteredList.get(i);
        ((ProductsViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsContract.productClicked(productsModel);
            }
        });
        ((ProductsViewHolder)holder).name.setText(productsModel.getProduct());

        ((ProductsViewHolder)holder).price.setText(Utils.digitsWithComma(productsModel.getAmount()));

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PRODUCTS/" + productsModel.getImage_file());
        Picasso.get().load(direct).into(((ProductsViewHolder)holder).imageUrl);

    }


    public void addItems(List<Products> productsModelList) {
        this.productsModelList = new ArrayList<>(productsModelList);
        this.productsFilteredList = productsModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productsFilteredList.size();
    }


}
