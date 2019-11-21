package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.apiresponses.FetchCashDenominationResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;

public class InsertCashDenominationAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchCashDenominationResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertCashDenominationAsync(List<FetchCashDenominationResponse.Result> list, SyncCallback syncCallback,
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
        List<CashDenomination> cashDenominationList = new ArrayList<>();
        for (final FetchCashDenominationResponse.Result r : list) {

            CashDenomination cashDenomination = new CashDenomination(
                    r.getCore_id(),
                    r.getDenomination(),
                    r.getAmount());


            cashDenominationList.add(cashDenomination);
        }

        dataSyncViewModel.insertCashDenomination(cashDenominationList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("cash_denomination");
    }




}
