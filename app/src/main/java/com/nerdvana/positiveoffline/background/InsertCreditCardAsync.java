package com.nerdvana.positiveoffline.background;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.nerdvana.positiveoffline.apiresponses.FetchCreditCardResponse;
import com.nerdvana.positiveoffline.apiresponses.FetchPaymentTypeResponse;
import com.nerdvana.positiveoffline.entities.CreditCards;
import com.nerdvana.positiveoffline.entities.PaymentTypes;
import com.nerdvana.positiveoffline.intf.SyncCallback;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;
import com.nerdvana.positiveoffline.viewmodel.TransactionsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InsertCreditCardAsync extends AsyncTask<Void, Void, Void> {

    private SyncCallback syncCallback;
    private List<FetchCreditCardResponse.Result> list;
    private DataSyncViewModel dataSyncViewModel;
    private Context context;
    public InsertCreditCardAsync(List<FetchCreditCardResponse.Result> list, SyncCallback syncCallback,
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
        List<CreditCards> creditCardsList = new ArrayList<>();
        for (final FetchCreditCardResponse.Result r : list) {

            CreditCards card = new CreditCards(
                    r.getCoreId(),
                    r.getCreditCard());


            creditCardsList.add(card);
        }

        dataSyncViewModel.insertCreditCard(creditCardsList);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        syncCallback.finishedLoading("credit_card");
    }


}
