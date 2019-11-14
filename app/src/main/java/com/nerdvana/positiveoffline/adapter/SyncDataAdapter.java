package com.nerdvana.positiveoffline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.entities.DataSync;
import com.nerdvana.positiveoffline.intf.SyncDataAdapterListener;

import java.util.List;

public class SyncDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataSync> syncList;
    private SyncDataAdapterListener syncDataAdapterListener;
    public SyncDataAdapter(List<DataSync> syncList, SyncDataAdapterListener syncDataAdapterListener) {
        this.syncList = syncList;
        this.syncDataAdapterListener = syncDataAdapterListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SyncDataAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sync_data, viewGroup, false));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listItemName;
        private ImageView finishedIndicator;
        private ProgressBar progressBar;
        private ConstraintLayout row;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItemName = itemView.findViewById(R.id.listItemName);
            finishedIndicator = itemView.findViewById(R.id.finishedIndicator);
            progressBar = itemView.findViewById(R.id.progressBar);
            row = itemView.findViewById(R.id.row);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final DataSync model = syncList.get(holder.getAdapterPosition());
        if(holder instanceof ViewHolder){
            ((ViewHolder) holder).listItemName.setText(model.getTable());
            if (model.getSynced()) {
                ((ViewHolder) holder).progressBar.setVisibility(View.GONE);
                ((ViewHolder) holder).finishedIndicator.setVisibility(View.VISIBLE);
            } else {
                ((ViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).finishedIndicator.setVisibility(View.GONE);
            }


            ((ViewHolder) holder)
                    .row
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            syncDataAdapterListener.clicked(model);
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return syncList.size();
    }
}
