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
import com.nerdvana.positiveoffline.intf.SelectedbgpaContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class SelectedBranchGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BranchGroup> branchGroupList;
    private Context context;
    private SelectedbgpaContract selectedbgpaContract;
    public SelectedBranchGroupAdapter(List<BranchGroup> branchGroupList, Context context,
                                      SelectedbgpaContract selectedbgpaContract) {
        this.branchGroupList = branchGroupList;
        this.context = context;
        this.selectedbgpaContract = selectedbgpaContract;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelectedBranchGroupAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_group, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvQty;
        private ImageView ivImage;
        private RelativeLayout row;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvQty = itemView.findViewById(R.id.tvQty);
            row = itemView.findViewById(R.id.row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        final BranchGroup model = branchGroupList.get(holder.getAdapterPosition());

        File direct = new File(Environment.getExternalStorageDirectory()
                + "/POS/PRODUCTS/" + model.getImg_file());
        Picasso.get()
                .load(direct)
                .placeholder(R.drawable.pos_logo_edited)
                .into(((SelectedBranchGroupAdapter.ViewHolder)holder).ivImage);

        ((SelectedBranchGroupAdapter.ViewHolder)holder).tvName.setText(model.getProduct());
        ((ViewHolder)holder).tvQty.setText(String.valueOf(model.getSelectedQty()) + " PC/S");
        ((ViewHolder)holder).row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedbgpaContract.clicked(model, holder.getAdapterPosition());
            }
        });



    }


    @Override
    public int getItemCount() {
        return branchGroupList.size();
    }


}
