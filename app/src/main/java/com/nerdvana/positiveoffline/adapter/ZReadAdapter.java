package com.nerdvana.positiveoffline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.entities.CutOff;
import com.nerdvana.positiveoffline.entities.EndOfDay;
import com.nerdvana.positiveoffline.intf.XReadContract;
import com.nerdvana.positiveoffline.intf.ZReadContract;

import java.util.List;

public class ZReadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<EndOfDay> endOfDayList;
    private ZReadContract zReadContract;
    private Context context;
    public ZReadAdapter(List<EndOfDay> endOfDayList, ZReadContract zReadContract, Context context) {
        this.endOfDayList = endOfDayList;
        this.zReadContract = zReadContract;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ZReadAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_zread, viewGroup, false));
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
        private TextView begTrans;
        private TextView endTrans;
        private TextView begVoidNo;
        private TextView endVoidNo;
        private TextView oldGrandTotal;
        private TextView newGrandTotal;
        private TextView zReadNo;

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
            begTrans = itemView.findViewById(R.id.begTrans);
            endTrans = itemView.findViewById(R.id.endTrans);
            begVoidNo = itemView.findViewById(R.id.begVoidNo);
            endVoidNo = itemView.findViewById(R.id.endVoidNo);
            oldGrandTotal = itemView.findViewById(R.id.oldGrandTotal);
            newGrandTotal = itemView.findViewById(R.id.newGrandTotal);
            zReadNo = itemView.findViewById(R.id.zReadNo);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
        final EndOfDay model = endOfDayList.get(holder.getAdapterPosition());
        ((ZReadAdapter.ViewHolder)holder).btnReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zReadContract.clicked(model);
            }
        });


        ((ZReadAdapter.ViewHolder)holder).title.setText("Z READING");
        ((ZReadAdapter.ViewHolder)holder).grossSalesValue.setText(Utils.digitsWithComma(model.getGross_sales()));
        ((ZReadAdapter.ViewHolder)holder).netSalesValue.setText(Utils.digitsWithComma(model.getNet_sales()));
        ((ZReadAdapter.ViewHolder)holder).vatableSalesValue.setText(Utils.digitsWithComma(model.getVatable_sales()));
        ((ZReadAdapter.ViewHolder)holder).vatExemptSalesValue.setText(Utils.digitsWithComma(model.getVat_exempt_sales()));
        ((ZReadAdapter.ViewHolder)holder).vatDiscountValue.setText("0.00");
        ((ZReadAdapter.ViewHolder)holder).twelveVatValue.setText(Utils.digitsWithComma(model.getVat_amount()));
        ((ZReadAdapter.ViewHolder)holder).nonVatValue.setText("0.00");
        ((ZReadAdapter.ViewHolder)holder).cashSalesValue.setText(Utils.digitsWithComma(model.getTotal_cash_payments()));
        ((ZReadAdapter.ViewHolder)holder).cardSalesValue.setText(Utils.digitsWithComma(model.getTotal_card_payments()));
        ((ZReadAdapter.ViewHolder)holder).voidValue.setText(Utils.digitsWithComma(model.getVoid_amount()));
        ((ZReadAdapter.ViewHolder)holder).seniorValue.setText(Utils.digitsWithComma(model.getSeniorAmount()));
        ((ZReadAdapter.ViewHolder)holder).seniorCount.setText(String.valueOf(model.getSeniorCount()));
        ((ZReadAdapter.ViewHolder)holder).pwdValue.setText(Utils.digitsWithComma(model.getPwdAmount()));
        ((ZReadAdapter.ViewHolder)holder).pwdCount.setText(String.valueOf(model.getPwdCount()));
        ((ZReadAdapter.ViewHolder)holder).othersValue.setText(Utils.digitsWithComma(model.getOthersAmount()));
        ((ZReadAdapter.ViewHolder)holder).othersCount.setText(String.valueOf(model.getOthersCount()));

        ((ZReadAdapter.ViewHolder)holder).begTrans.setText("--");
        ((ZReadAdapter.ViewHolder)holder).endTrans.setText("--");
        ((ZReadAdapter.ViewHolder)holder).begVoidNo.setText("--");
        ((ZReadAdapter.ViewHolder)holder).endVoidNo.setText("--");
        ((ZReadAdapter.ViewHolder)holder).oldGrandTotal.setText("--");
        ((ZReadAdapter.ViewHolder)holder).newGrandTotal.setText("--");
        ((ZReadAdapter.ViewHolder)holder).zReadNo.setText("--");



    }



    @Override
    public int getItemCount() {
        return endOfDayList.size();
    }



}
