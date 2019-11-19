package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;

import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ButtonsAsync extends AsyncTask<ButtonsModel, Void, List<ButtonsModel>> {
    private AsyncContract asyncContract;
    private Context context;
    public ButtonsAsync(AsyncContract asyncContract, Context context) {
        this.asyncContract = asyncContract;
        this.context = context;
    }

    @Override
    protected List<ButtonsModel> doInBackground(ButtonsModel... buttonsModels) {
        List<ButtonsModel> buttonsModelList = new ArrayList<>();
        String[]images = {"", ""};

        buttonsModelList.add(new ButtonsModel(100,"SAVE TRANSACTION", "",3));
        buttonsModelList.add(new ButtonsModel(9988,"RESUME TRANSACTION", "",2));
        buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",6));
        buttonsModelList.add(new ButtonsModel(126,"FOC", "",24)); // return later
        buttonsModelList.add(new ButtonsModel(115,"DISCOUNT", "",7));
//        buttonsModelList.add(new ButtonsModel(102,"DEPOSIT", "",9));
        buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",12));
        buttonsModelList.add(new ButtonsModel(101,"ITEM VOID", "",13));
//        buttonsModelList.add(new ButtonsModel(118,"SAFEKEEPING", "",17));
        buttonsModelList.add(new ButtonsModel(116,"CANCEL", "",20));
        buttonsModelList.add(new ButtonsModel(129,"SETTINGS", "",24));
        buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",100));
        buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT.", "",100));
        buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",24));
        buttonsModelList.add(new ButtonsModel(134,"SYNC DATA", "",24));
        buttonsModelList.add(new ButtonsModel(99,"CHANGE QTY", "",1));
        Collections.sort(buttonsModelList);
        return buttonsModelList;
    }

    @Override
    protected void onPostExecute(List<ButtonsModel> buttonsModels) {
        this.asyncContract.doneLoading(buttonsModels, "buttons");
        super.onPostExecute(buttonsModels);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
