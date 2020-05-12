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
import com.nerdvana.positiveoffline.entities.BranchGroup;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.intf.BgpaContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class BranchGroupProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BranchGroup> branchGroupList;
    private Context context;
    private BgpaContract bgpaContract;
    public BranchGroupProductAdapter(List<BranchGroup> branchGroupList, Context context,
                                     BgpaContract bgpaContract) {
        this.branchGroupList = branchGroupList;
        this.context = context;
        this.bgpaContract = bgpaContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BranchGroupProductAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_branch_product, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivImage;
        private RelativeLayout row;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            row = itemView.findViewById(R.id.row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {

        final BranchGroup model = branchGroupList.get(holder.getAdapterPosition());

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PRODUCTS/" + model.getImg_file());
        Picasso.get()
                .load(direct)
                .placeholder(R.drawable.pos_logo_edited)
                .into(((BranchGroupProductAdapter.ViewHolder)holder).ivImage);

        ((BranchGroupProductAdapter.ViewHolder)holder).tvName.setText(String.format("%s %s", model.getProduct(), String.valueOf(model.getPrice())));

        ((BranchGroupProductAdapter.ViewHolder)holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgpaContract.clicked(model);
            }
        });


    }


    @Override
    public int getItemCount() {
        return branchGroupList.size();
    }


}
