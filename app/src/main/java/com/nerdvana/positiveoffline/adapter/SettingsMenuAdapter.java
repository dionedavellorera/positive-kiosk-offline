package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.intf.SettingsMenuContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.model.SettingsMenuModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SettingsMenuModel> settingsList;
    private SettingsMenuContract settingsMenuContract;
    private Context context;
    public SettingsMenuAdapter(List<SettingsMenuModel> settingsList, SettingsMenuContract settingsMenuContract,
                               Context context) {
        this.settingsList = settingsList;
        this.settingsMenuContract = settingsMenuContract;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SettingsMenuAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_setting_menu, viewGroup, false));
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private RelativeLayout rootView;
        private RelativeLayout relView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rootView = itemView.findViewById(R.id.rootView);
            relView = itemView.findViewById(R.id.relView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        final SettingsMenuModel model = settingsList.get(holder.getAdapterPosition());
        ((SettingsMenuAdapter.ViewHolder)holder).rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsMenuContract.clicked(model, holder.getAdapterPosition());
            }
        });

        if (model.isActive()) {
            ((SettingsMenuAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorLtGrey));
        } else {
            ((SettingsMenuAdapter.ViewHolder)holder).rootView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }

        ((SettingsMenuAdapter.ViewHolder)holder).name.setText(model.getMenuName());

    }


    @Override
    public int getItemCount() {
        return settingsList.size();
    }


}
