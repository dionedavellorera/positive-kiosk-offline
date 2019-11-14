package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Orders> checkoutList;
    private ProductsContract productsContract;
    private Context context;
    public CheckoutAdapter(List<Orders> checkoutList, ProductsContract productsContract, Context context) {
        this.checkoutList = checkoutList;
        this.context = context;
        this.productsContract = productsContract;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CheckoutAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_checkout, viewGroup, false));
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listItemName;
        private TextView listItemQty;
        private TextView listItemPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.listItemName);
            listItemQty = itemView.findViewById(R.id.listItemQty);
            listItemPrice = itemView.findViewById(R.id.listItemPrice);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        final Orders productsModel = checkoutList.get(i);
//        ((ProductsAdapter.ProductsViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                productsContract.productClicked(productsModel);
//            }
//        });
        ((CheckoutAdapter.ViewHolder)holder).listItemName.setText(productsModel.getName());
        ((CheckoutAdapter.ViewHolder)holder).listItemQty.setText(String.valueOf(productsModel.getQty()));
        ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setText(Utils.digitsWithComma(productsModel.getAmount()));

    }



    @Override
    public int getItemCount() {
        return checkoutList.size();
    }


}
