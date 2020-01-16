package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.intf.PrinterConnection;
import com.nerdvana.positiveoffline.model.OtherPrinterModel;

import java.util.List;

public class OtherPrinterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OtherPrinterModel> otherPrinterModelList;
    private PrinterConnection printerConnection;
    private Context context;
    public OtherPrinterAdapter(List<OtherPrinterModel> otherPrinterModelList,
                               PrinterConnection printerConnection,
                               Context context) {
        this.otherPrinterModelList = otherPrinterModelList;
        this.printerConnection = printerConnection;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OtherPrinterAdapter.OtherPrinterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_other_printer, viewGroup, false));
    }

    static class OtherPrinterViewHolder extends RecyclerView.ViewHolder {
        private TextView head;
        private TextView sub;
        private TextView activeText;
        private RelativeLayout row;
        public OtherPrinterViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            sub = itemView.findViewById(R.id.sub);
            activeText = itemView.findViewById(R.id.activeText);
            row = itemView.findViewById(R.id.row);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        if(holder instanceof OtherPrinterAdapter.OtherPrinterViewHolder){

            ((OtherPrinterAdapter.OtherPrinterViewHolder)holder).row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printerConnection.clicked(holder.getAdapterPosition());
                }
            });

            String printerSelected = "";

            if (!SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY).isEmpty()) {
                printerSelected = SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY);
            } else {
                if (!SharedPreferenceManager.getString(context, AppConstants.SELECTED_PORT).isEmpty()) {
                    printerSelected = SharedPreferenceManager.getString(context, AppConstants.SELECTED_PORT);
                } else {
                    printerSelected = "N/A";
                }
            }

            if (printerSelected.equalsIgnoreCase(otherPrinterModelList.get(i).getHead())) {
                ((OtherPrinterViewHolder)holder).activeText.setVisibility(View.VISIBLE);
            } else {
                ((OtherPrinterViewHolder)holder).activeText.setVisibility(View.GONE);
            }

            ((OtherPrinterViewHolder)holder).head.setText(otherPrinterModelList.get(i).getHead());
            ((OtherPrinterViewHolder)holder).sub.setText(otherPrinterModelList.get(i).getSub());
        }
    }

    @Override
    public int getItemCount() {
        return otherPrinterModelList.size();
    }
}
