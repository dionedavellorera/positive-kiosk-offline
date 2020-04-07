package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.intf.ButtonsContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;

import java.util.ArrayList;
import java.util.List;


public class ButtonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ButtonsModel> buttonsModelList;
    private ButtonsContract buttonsContract;
    private List<String> shortcutString;
    private Context context;

    private boolean isDarkMode;
    public ButtonsAdapter(List<ButtonsModel> buttonsModelList,
                          ButtonsContract buttonsContract,
                          Context context,
                          boolean isDarkMode) {
        this.buttonsModelList = buttonsModelList;
        this.buttonsContract = buttonsContract;
        this.context = context;
        this.shortcutString = new ArrayList<>();
        this.isDarkMode = isDarkMode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ButtonsAdapter.ButtonsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_buttons, viewGroup, false));
    }



    static class ButtonsViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
//        private CardView rootView;
        private RelativeLayout relView;
        public ButtonsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
//            rootView = itemView.findViewById(R.id.rootView);
            relView = itemView.findViewById(R.id.relView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        ((ButtonsViewHolder)holder).relView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsContract.clicked(buttonsModelList.get(i));
            }
        });




        String firstString = buttonsModelList.get(i).getName().substring(0, 1);
        String remainingString = buttonsModelList.get(i).getName().substring(1);
        String finalString = String.format("<b><u>%s</b></u>%s", firstString, remainingString);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((ButtonsViewHolder)holder).name.setText(Html.fromHtml(finalString, Html.FROM_HTML_MODE_LEGACY));
        } else {
            ((ButtonsViewHolder)holder).name.setText(Html.fromHtml(finalString));
        }



        if (buttonsModelList.get(i).isEnabled()) {
            ((ButtonsViewHolder)holder).relView.setEnabled(true);
        } else {
            ((ButtonsViewHolder)holder).relView.setEnabled(false);
        }


        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (isDarkMode) {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ((ButtonsViewHolder)holder).relView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_selector_dark));
            } else {
                ((ButtonsViewHolder)holder).relView.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selector_dark));
            }
            ((ButtonsViewHolder)holder).name.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));
        } else {
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ((ButtonsViewHolder)holder).relView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_selector));
            } else {
                ((ButtonsViewHolder)holder).relView.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selector));
            }
            ((ButtonsViewHolder)holder).name.setTextColor(context.getResources().getColorStateList(R.color.colorBlack));
        }





    }


    public void addItems(List<ButtonsModel> buttonsModelList) {
        this.buttonsModelList = buttonsModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return buttonsModelList.size();
    }

}
