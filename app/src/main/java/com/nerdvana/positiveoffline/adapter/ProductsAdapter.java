package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.gson.reflect.TypeToken;
import com.nerdvana.positiveoffline.GsonHelper;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.apiresponses.FetchProductsResponse;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Products> productsModelList;
    private List<Products> productsFilteredList;
    private ProductsContract productsContract;
    private Context context;
    private boolean isDarkMode;
    public ProductsAdapter(List<Products> productsModelList, ProductsContract productsContract,
                           Context context, boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
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
                FilterResults filterResults = new FilterResults();

                synchronized (filterResults) {
                    if (constraint != null) {
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
                                            pm.getDepartment().toLowerCase().contains(charSting.toLowerCase()) ||
                                            String.valueOf(pm.getProduct_barcode().toLowerCase()).contains(charSting.toLowerCase())) {
                                        filteredList.add(pm);
                                    }
                                }
                                productsFilteredList = filteredList;
                            }
                        }

                        filterResults.values = productsFilteredList;

                    }
                }


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
        private TextView promoPrice;
        private ImageView imageUrl;
        private CardView rootView;
        private RelativeLayout productBg;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            promoPrice = itemView.findViewById(R.id.promoPrice);
            imageUrl = itemView.findViewById(R.id.image);
            rootView = itemView.findViewById(R.id.rootView);
            productBg = itemView.findViewById(R.id.productBg);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        if(holder instanceof ChangeRoomStatusAdapter.ListViewHolder){

        }
        final Products productsModel = productsFilteredList.get(i);
        ((ProductsViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsContract.productClicked(productsModel);
            }
        });
//        ((ProductsViewHolder)holder).name.setText(productsModel.getProduct());
        ((ProductsViewHolder)holder).name.setText(Html.fromHtml(productsModel.getProduct()));

        ((ProductsViewHolder)holder).price.setText(Utils.digitsWithComma(productsModel.getAmount()));

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PRODUCTS/" + productsModel.getImage_file());
        Picasso.get()
                .load(direct)
                .placeholder(R.drawable.pos_logo_edited)
                .into(((ProductsViewHolder)holder).imageUrl);

        if (!isDarkMode) {
            ((ProductsViewHolder)holder).productBg.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            ((ProductsViewHolder)holder).name.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            ((ProductsViewHolder)holder).price.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            ((ProductsViewHolder)holder).promoPrice.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            ((ProductsViewHolder)holder).name.setTextColor(Color.BLACK);
            ((ProductsViewHolder)holder).price.setTextColor(Color.BLACK);
        } else {
            ((ProductsViewHolder)holder).productBg.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
            ((ProductsViewHolder)holder).name.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
            ((ProductsViewHolder)holder).price.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
            ((ProductsViewHolder)holder).promoPrice.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
            ((ProductsViewHolder)holder).name.setTextColor(Color.WHITE);
            ((ProductsViewHolder)holder).price.setTextColor(Color.WHITE);
        }

        TypeToken<List<FetchProductsResponse.ProductPromo>> token = new TypeToken<List<FetchProductsResponse.ProductPromo>>() {};
        List<FetchProductsResponse.ProductPromo> promo = GsonHelper.getGson().fromJson(productsModel.getJson_promo(), token.getType());

        Double productFinalAmount = productsModel.getAmount();
        DateTime dateToday = new DateTime(Utils.getCurrentDate());
        if (promo.size() > 0) {
            for (FetchProductsResponse.ProductPromo pp : promo) {
                if (TextUtils.isEmpty(pp.getEndDate()) && TextUtils.isEmpty(pp.getEndTime())) {
                    //PRICE CHANGE
                    DateTime promoDateStart =  new DateTime(pp.getStartDate());
                    if (dateToday.isAfter(promoDateStart)) {
                        if (pp.getIsPercentage() == 1) {
                            productFinalAmount = productsModel.getAmount() - (productsModel.getAmount() * (pp.getValue() / 100));
                        } else {
                            productFinalAmount = pp.getValue();
                        }
                    }

                }
            }

            for (FetchProductsResponse.ProductPromo pp : promo) {
                if (!TextUtils.isEmpty(pp.getStartDate()) && !TextUtils.isEmpty(pp.getEndDate())) {
                    DateTime promoDateStart =  new DateTime(pp.getStartDate());
                    DateTime promoDateEnd =  new DateTime(pp.getEndDate());
                    //PROMO
                    if (dateToday.isAfter(promoDateStart) && dateToday.isBefore(promoDateEnd)) {
                        if (pp.getIsPercentage() == 1) {
                            productFinalAmount = productFinalAmount - (productFinalAmount * (pp.getValue() / 100));
                        } else {
                            productFinalAmount = pp.getValue();
                        }
                    }
                }
            }
            ((ProductsViewHolder)holder).price.setPaintFlags(((ProductsViewHolder)holder).price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((ProductsViewHolder)holder).price.setText(((ProductsViewHolder)holder).price.getText().toString() + " BEFORE");
            ((ProductsViewHolder)holder).promoPrice.setVisibility(View.VISIBLE);
            ((ProductsViewHolder)holder).promoPrice.setText(Utils.digitsWithComma(productFinalAmount) + " NOW");
        } else {
            ((ProductsViewHolder)holder).promoPrice.setVisibility(View.GONE);
            ((ProductsViewHolder)holder).price.setPaintFlags( ((ProductsViewHolder)holder).price.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }



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
