package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.SimpleListModel;

import java.util.ArrayList;
import java.util.List;

public class SimpleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SimpleListModel> simpleListModel;
    private Context context;
    public SimpleListAdapter(List<SimpleListModel> simpleListModel, Context context) {
        this.context = context;
        this.simpleListModel = simpleListModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SimpleListAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_simple, viewGroup, false));
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private LinearLayout rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.rootView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        SimpleListModel model = simpleListModel.get(holder.getAdapterPosition());
        ((SimpleListAdapter.ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buttonsContract.clicked(buttonsModelList.get(i));
            }
        });

        ((ViewHolder)holder).name.setText(model.getName().toUpperCase());


        if (model.getActive()) {
            ((SimpleListAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorLtGrey));
        } else {
            ((SimpleListAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }



    }


    @Override
    public int getItemCount() {
        return simpleListModel.size();
    }
}
