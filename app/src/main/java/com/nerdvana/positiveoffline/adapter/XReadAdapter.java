package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.intf.XReadContract;

import java.util.List;

public class XReadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CutOff> cutOffList;
    private XReadContract xReadContract;
    private Context context;
    public XReadAdapter(List<CutOff> cutOffList, XReadContract xReadContract, Context context) {
        this.cutOffList = cutOffList;
        this.xReadContract = xReadContract;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new XReadAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_xread, viewGroup, false));
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private Button btnReprint;
        private TextView title;
        private TextView grossSalesValue;
        private TextView netSalesValue;
        private TextView vatableSalesValue;
        private TextView vatExemptSalesValue;
        private TextView vatDiscountValue;
        private TextView twelveVatValue;
        private TextView nonVatValue;
        private TextView cashSalesValue;
        private TextView cardSalesValue;
        private TextView voidValue;
        private TextView seniorValue;
        private TextView seniorCount;
        private TextView pwdValue;
        private TextView pwdCount;
        private TextView othersValue;
        private TextView othersCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnReprint = itemView.findViewById(R.id.btnReprint);
            title = itemView.findViewById(R.id.title);
            grossSalesValue = itemView.findViewById(R.id.grossSalesValue);
            netSalesValue = itemView.findViewById(R.id.netSalesValue);
            vatableSalesValue = itemView.findViewById(R.id.vatableSalesValue);
            vatExemptSalesValue = itemView.findViewById(R.id.vatExemptSalesValue);
            vatDiscountValue = itemView.findViewById(R.id.vatDiscountValue);
            twelveVatValue = itemView.findViewById(R.id.twelveVatValue);
            nonVatValue = itemView.findViewById(R.id.nonVatValue);
            cashSalesValue = itemView.findViewById(R.id.cashSalesValue);
            cardSalesValue = itemView.findViewById(R.id.cardSalesValue);
            voidValue = itemView.findViewById(R.id.voidValue);
            seniorValue = itemView.findViewById(R.id.seniorValue);
            seniorCount = itemView.findViewById(R.id.seniorCount);
            pwdValue = itemView.findViewById(R.id.pwdValue);
            pwdCount = itemView.findViewById(R.id.pwdCount);
            othersValue = itemView.findViewById(R.id.othersValue);
            othersCount = itemView.findViewById(R.id.othersCount);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        final CutOff model = cutOffList.get(holder.getAdapterPosition());
        ((ViewHolder)holder).btnReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xReadContract.clicked(model);
            }
        });


        ((ViewHolder)holder).title.setText("X READING");
        ((ViewHolder)holder).grossSalesValue.setText(Utils.digitsWithComma(model.getGross_sales()));
        ((ViewHolder)holder).netSalesValue.setText(Utils.digitsWithComma(model.getNet_sales()));
        ((ViewHolder)holder).vatableSalesValue.setText(Utils.digitsWithComma(model.getVatable_sales()));
        ((ViewHolder)holder).vatExemptSalesValue.setText(Utils.digitsWithComma(model.getVat_exempt_sales()));
        ((ViewHolder)holder).vatDiscountValue.setText("0.00");
        ((ViewHolder)holder).twelveVatValue.setText(Utils.digitsWithComma(model.getVat_amount()));
        ((ViewHolder)holder).nonVatValue.setText("0.00");
        ((ViewHolder)holder).cashSalesValue.setText(Utils.digitsWithComma(model.getTotal_cash_payments()));
        ((ViewHolder)holder).cardSalesValue.setText(Utils.digitsWithComma(model.getTotal_card_payments()));
        ((ViewHolder)holder).voidValue.setText(Utils.digitsWithComma(model.getVoid_amount()));
        ((ViewHolder)holder).seniorValue.setText(Utils.digitsWithComma(model.getSeniorAmount()));
        ((ViewHolder)holder).seniorCount.setText(String.valueOf(model.getSeniorCount()));
        ((ViewHolder)holder).pwdValue.setText(Utils.digitsWithComma(model.getPwdAmount()));
        ((ViewHolder)holder).pwdCount.setText(String.valueOf(model.getPwdCount()));
        ((ViewHolder)holder).othersValue.setText(Utils.digitsWithComma(model.getOthersAmount()));
        ((ViewHolder)holder).othersCount.setText(String.valueOf(model.getOthersCount()));
    }



    @Override
    public int getItemCount() {
        return cutOffList.size();
    }



}
