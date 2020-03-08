package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.ProductAlacart;
import com.nerdvana.positiveoffline.model.BranchGroupFilterModel;
import com.nerdvana.positiveoffline.view.dialog.BundleCompositionDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class BranchGroupFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BranchGroupFilterModel> branchGroupFilterModelList;
    private Context context;
    private BundleCompositionDialog.Filter filter;
    public BranchGroupFilterAdapter(List<BranchGroupFilterModel> branchGroupFilterModelList, Context context,
                                    BundleCompositionDialog.Filter filter) {
        this.branchGroupFilterModelList = branchGroupFilterModelList;
        this.context = context;
        this.filter = filter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BranchGroupFilterAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_branch_group_filter, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        final BranchGroupFilterModel model = branchGroupFilterModelList.get(holder.getAdapterPosition());

        ((ViewHolder)holder).name.setText(String.format("%s(%s)", model.getBranch_group_name(), String.valueOf(model.getBranch_qty())));
        ((ViewHolder)holder).name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.clicked(holder.getAdapterPosition());
            }
        });



    }


    @Override
    public int getItemCount() {
        return branchGroupFilterModelList.size();
    }


}
