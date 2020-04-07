package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.Orders;
import com.nerdvana.positiveoffline.entities.Products;
import com.nerdvana.positiveoffline.intf.OrdersContract;
import com.nerdvana.positiveoffline.intf.ProductsContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Orders> checkoutList;
    private OrdersContract ordersContract;
    private Context context;
    private Boolean isDarkMode;
    public CheckoutAdapter(List<Orders> checkoutList,
                           OrdersContract ordersContract,
                           Context context,
                           Boolean isDarkMode) {
        this.checkoutList = checkoutList;
        this.context = context;
        this.ordersContract = ordersContract;
        this.isDarkMode = isDarkMode;

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
        private LinearLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.listItemName);
            listItemQty = itemView.findViewById(R.id.listItemQty);
            listItemPrice = itemView.findViewById(R.id.listItemPrice);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final Orders productsModel = checkoutList.get(i);
        if (productsModel.getProduct_alacart_id() == 0 && productsModel.getProduct_group_id() == 0) {
            ((CheckoutAdapter.ViewHolder)holder).rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ordersContract.longClicked(productsModel, ((CheckoutAdapter.ViewHolder)holder).rootView);
                    return true;
                }
            });
            ((CheckoutAdapter.ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordersContract.clicked(productsModel);
                }
            });
        }



        if (productsModel.getProduct_alacart_id() != 0 || productsModel.getProduct_group_id() != 0) {
            ((CheckoutAdapter.ViewHolder)holder).listItemName.setText("  " +productsModel.getName());
//            ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setText(Utils.digitsWithComma(0.00));
        } else {
            if (productsModel.getIs_discount_exempt() == 1) {
                ((CheckoutAdapter.ViewHolder)holder).listItemName.setText("(DE)" +productsModel.getName());
            } else {
                ((CheckoutAdapter.ViewHolder)holder).listItemName.setText(productsModel.getName());
            }

        }

        if (!TextUtils.isEmpty(productsModel.getNotes())) {
            ((CheckoutAdapter.ViewHolder)holder).listItemName.setText(
                "*" + ((CheckoutAdapter.ViewHolder)holder).listItemName.getText().toString()
            );
        }

        ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setText(Utils.digitsWithComma(productsModel.getOriginal_amount() * productsModel.getQty()));


        ((CheckoutAdapter.ViewHolder)holder).listItemQty.setText(String.valueOf(productsModel.getQty()));

        if (!productsModel.getIs_editing()) {
            if (isDarkMode) {
                ((CheckoutAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
                ((CheckoutAdapter.ViewHolder)holder).listItemName.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
                ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));
                ((CheckoutAdapter.ViewHolder)holder).listItemQty.setBackgroundColor(context.getResources().getColor(R.color.colorDarkLighter));

                ((CheckoutAdapter.ViewHolder)holder).listItemName.setTextColor(context.getResources().getColor(R.color.colorWhite));
                ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setTextColor(context.getResources().getColor(R.color.colorWhite));
                ((CheckoutAdapter.ViewHolder)holder).listItemQty.setTextColor(context.getResources().getColor(R.color.colorWhite));
            } else {
                ((CheckoutAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                ((CheckoutAdapter.ViewHolder)holder).listItemName.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                ((CheckoutAdapter.ViewHolder)holder).listItemQty.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

                ((CheckoutAdapter.ViewHolder)holder).listItemName.setTextColor(context.getResources().getColor(R.color.colorBlack));
                ((CheckoutAdapter.ViewHolder)holder).listItemPrice.setTextColor(context.getResources().getColor(R.color.colorBlack));
                ((CheckoutAdapter.ViewHolder)holder).listItemQty.setTextColor(context.getResources().getColor(R.color.colorBlack));
            }
        } else {
            ((CheckoutAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
        }
    }



    @Override
    public int getItemCount() {
        return checkoutList.size();
    }


}
