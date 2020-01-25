package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
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

        buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1));
        buttonsModelList.add(new ButtonsModel(115,"DISCOUNT", "",2));

        buttonsModelList.add(new ButtonsModel(99,"CHANGE QTY", "",3));
        buttonsModelList.add(new ButtonsModel(101,"ITEM CANCEL", "",4));

        buttonsModelList.add(new ButtonsModel(100,"PAUSE TRANSACTION", "",5));
        buttonsModelList.add(new ButtonsModel(9988,"RESUME TRANSACTION", "",6));


        buttonsModelList.add(new ButtonsModel(116,"CANCEL", "",7));
        buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8));

        buttonsModelList.add(new ButtonsModel(134,"SYNC DATA", "",9));
        buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10));

        buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11));
        buttonsModelList.add(new ButtonsModel(129,"SETTINGS", "",12));

        buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",13));
        buttonsModelList.add(new ButtonsModel(110,"TEST PRINT", "",13));

        if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {

        } else {
            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {

            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                buttonsModelList.add(new ButtonsModel(106,"ROOMS", "",1));
                buttonsModelList.add(new ButtonsModel(107,"TRANSFER ROOM", "",1));
                buttonsModelList.add(new ButtonsModel(109,"SOA", "",4));
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                buttonsModelList.add(new ButtonsModel(106,"TABLES", "",1));
                buttonsModelList.add(new ButtonsModel(109,"SOA", "",4));
            }
        }
//        buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",13));



//        buttonsModelList.add(new ButtonsModel(126,"FOC", "",24)); // return later





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
