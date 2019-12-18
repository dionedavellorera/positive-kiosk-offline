package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchDiscountResponse;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.DiscountSettings;
import com.nerdvana.positiveoffline.entities.Discounts;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertDiscountsAsync extends AsyncTask<Void, Void, Void> {
    private SyncCallback syncCallback;
    private List<FetchDiscountResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertDiscountsAsync(List<FetchDiscountResponse.Result> list, SyncCallback syncCallback,
                                DataSyncViewModel dataSyncViewModel, Context context) {
        this.syncCallback = syncCallback;
        this.list = list;
        this.dataSyncViewModel = dataSyncViewModel;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Discounts> discountList = new ArrayList<>();
        List<DiscountSettings> discountSettingsList = new ArrayList<>();


        Discounts manual = new Discounts(
                1000,
                "MANUAL",
                0,
                0,
                0,
                0);
        discountList.add(manual);


        Discounts custom = new Discounts(
                1001,
                "CUSTOM",
                0,
                0,
                0,
                0);
        discountList.add(custom);

        for (FetchDiscountResponse.Result r : list) {

            Discounts dc = new Discounts(
                    r.getId(),
                    r.getDiscountCard(),
                    r.getIsCustomDiscount(),
                    r.getIsCard(),
                    r.getIsEmployee(),
                    r.getIsSpecial());
            discountList.add(dc);

            for (FetchDiscountResponse.DiscountSetting ds : r.getDiscountSettings()) {
                DiscountSettings discountSettings = new DiscountSettings(
                        ds.getId(),
                        r.getId(),
                        ds.getProductId() == null ? "" : ds.getProductId().toString(),
                        ds.getDepartmentId() == null ? "" : ds.getDepartmentId().toString(),
                        ds.getRoomTypeId() == null ? "" : ds.getRoomTypeId().toLowerCase(),
                        ds.getRoomRateId() == null ? "" : ds.getRoomRateId(),
                        ds.getPercentage(),
                        r.getDiscountCard()
                );
                discountSettingsList.add(discountSettings);
            }
        }
        dataSyncViewModel.insertDiscountWithSettings(discountList, discountSettingsList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("discounts");
    }
}
