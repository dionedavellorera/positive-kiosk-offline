package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.intf.DiscountsContract;
import com.nerdvana.positiveoffline.model.TransactionWithDiscounts;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class AlacartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductAlacart> productAlacartList;
    private Context context;
    public AlacartAdapter(List<ProductAlacart> productAlacartList, Context context) {
        this.productAlacartList = productAlacartList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlacartAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_alacart, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final ProductAlacart model = productAlacartList.get(holder.getAdapterPosition());

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PRODUCTS/" + model.getImg_file());
        Picasso.get()
                .load(direct)
                .placeholder(R.drawable.pos_logo_edited)
                .into(((ViewHolder)holder).ivImage);

        ((ViewHolder)holder).tvName.setText(String.format("%s %s", model.getProduct(), String.valueOf(model.getPrice())));




    }


    @Override
    public int getItemCount() {
        return productAlacartList.size();
    }



}
