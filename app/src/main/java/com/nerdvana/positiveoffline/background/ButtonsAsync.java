package com.nerdvana.positiveoffline.background;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.intf.AsyncContract;
import com.nerdvana.positiveoffline.model.ButtonsModel;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ButtonsAsync extends AsyncTask<ButtonsModel, Void, List<ButtonsModel>> {
    private AsyncContract asyncContract;
    private Context context;
    private UserViewModel userViewModel;
    public ButtonsAsync(AsyncContract asyncContract, Context context, UserViewModel userViewModel) {
        this.asyncContract = asyncContract;
        this.context = context;
        this.userViewModel = userViewModel;
    }

    @Override
    protected List<ButtonsModel> doInBackground(ButtonsModel... buttonsModels) {




        List<ButtonsModel> buttonsModelList = new ArrayList<>();
        buttonsModelList.add(new ButtonsModel(152,"SEARCH", "",1, 0));


        //"label": "Branch > POS > Function > Discount"
        buttonsModelList.add(new ButtonsModel(115,"DISCOUNT", "",2, 62));
        //"label": "POS > Function > Void > Item"
        buttonsModelList.add(new ButtonsModel(101,"ITEM CANCEL", "",4, 68));
        //"label": "Branch > POS > Function > Save Transaction"
        buttonsModelList.add(new ButtonsModel(100,"PAUSE TRANSACTION", "",5, 127));
        //"label": "Branch > POS > Function > Resume Transaction"
        buttonsModelList.add(new ButtonsModel(9988,"RESUME TRANSACTION", "",6, 128));

        //"label": "Branch > POS > Function > Sync Data"
        buttonsModelList.add(new ButtonsModel(134,"SYNC DATA", "",9, 126));

        //"label": "Branch > POS > Settings"
        buttonsModelList.add(new ButtonsModel(129,"SETTINGS", "",12, 124));
        //0 FOR ALL
        buttonsModelList.add(new ButtonsModel(99,"CHANGE QTY", "",3, 0));
        buttonsModelList.add(new ButtonsModel(116,"CANCEL", "",7, 0));

        buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",13, 0));
//        buttonsModelList.add(new ButtonsModel(110,"TEST PRINT", "",13, 0));
        buttonsModelList.add(new ButtonsModel(122,"DISCOUNT EXEMPT", "",2, 0));
//        buttonsModelList.add(new ButtonsModel(124,"INTRANSIT", "",2, 0));

        buttonsModelList.add(new ButtonsModel(108,"CLEAR TRANSACTION", "",1, 0));
//        buttonsModelList.add(new ButtonsModel(120,"SET SERIAL NUMBER", "",4, 0));

        if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {

            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE))) {
                buttonsModelList.add(new ButtonsModel(176,"ORDERS", "",10, 0));
                //"label": "Branch > POS > Function > Payment"
                buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
                //"label": "POS > Function > Void > Post"
                buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
                buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
                buttonsModelList.add(new ButtonsModel(125,"SPOT AUDIT", "",2, 0));
                buttonsModelList.add(new ButtonsModel(200,"PAYOUT", "",10, 0));
//                    buttonsModelList.add(new ButtonsModel(172,"AR REDEEMING", "",4, 0));
                //"label": "Branch > POS > Function > View Receipt"
                buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
            } else {
                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("cashier")) {
                    buttonsModelList.add(new ButtonsModel(176,"ORDERS", "",10, 0));
                    //"label": "Branch > POS > Function > Payment"
                    buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
                    //"label": "POS > Function > Void > Post"
                    buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
                    buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
                    buttonsModelList.add(new ButtonsModel(125,"SPOT AUDIT", "",2, 0));
                    buttonsModelList.add(new ButtonsModel(200,"PAYOUT", "",10, 0));
//                        buttonsModelList.add(new ButtonsModel(172,"AR REDEEMING", "",4, 0));
                    //"label": "Branch > POS > Function > View Receipt"
                    buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("to")) {
                    buttonsModelList.add(new ButtonsModel(175,"SAVE ORDER", "",4, 0));
                }
            }


//            buttonsModelList.add(new ButtonsModel(176,"ORDERS", "",10, 0));
////"label": "Branch > POS > Function > Payment"
//            buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
//            //"label": "POS > Function > Void > Post"
//            buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
//            buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
//            buttonsModelList.add(new ButtonsModel(125,"SPOT AUDIT", "",2, 0));
//            buttonsModelList.add(new ButtonsModel(200,"PAYOUT", "",10, 0));
////            buttonsModelList.add(new ButtonsModel(172,"AR REDEEMING", "",4, 0));
//            //"label": "Branch > POS > Function > View Receipt"
//            buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
        } else {
            if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {

                if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE))) {
                    buttonsModelList.add(new ButtonsModel(176,"ORDERS", "",10, 0));
                    //"label": "Branch > POS > Function > Payment"
                    buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
                    //"label": "POS > Function > Void > Post"
                    buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
                    buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
                    buttonsModelList.add(new ButtonsModel(125,"SPOT AUDIT", "",2, 0));
                    buttonsModelList.add(new ButtonsModel(200,"PAYOUT", "",10, 0));
//                    buttonsModelList.add(new ButtonsModel(172,"AR REDEEMING", "",4, 0));
                    //"label": "Branch > POS > Function > View Receipt"
                    buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
                } else {
                    if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("cashier")) {
                        buttonsModelList.add(new ButtonsModel(176,"ORDERS", "",10, 0));
                        //"label": "Branch > POS > Function > Payment"
                        buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
                        //"label": "POS > Function > Void > Post"
                        buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
                        buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
                        buttonsModelList.add(new ButtonsModel(125,"SPOT AUDIT", "",2, 0));
                        buttonsModelList.add(new ButtonsModel(200,"PAYOUT", "",10, 0));
//                        buttonsModelList.add(new ButtonsModel(172,"AR REDEEMING", "",4, 0));
                        //"label": "Branch > POS > Function > View Receipt"
                        buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
                    } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_MODE).equalsIgnoreCase("to")) {
                        buttonsModelList.add(new ButtonsModel(175,"SAVE ORDER", "",4, 0));
                    }
                }


            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
                //"label": "Branch > POS > Function > Switch Room"
                buttonsModelList.add(new ButtonsModel(107,"TRANSFER ROOM", "",1, 69));

                //"label": "Branch > POS > Function > SOA"
                buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
                //0 FOR ALL
                buttonsModelList.add(new ButtonsModel(106,"ROOMS", "",1, 0));

                //"label": "Branch > POS > Function > View Receipt"
                buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
            } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
                //"label": "Branch > POS > Function > SOA"
                buttonsModelList.add(new ButtonsModel(107,"TRANSFER TABLE", "",1, 69));
                buttonsModelList.add(new ButtonsModel(111,"DINE IN/TAKE OUT", "",1, 69));
                buttonsModelList.add(new ButtonsModel(112,"SHARE TRANSACTION", "",1, 69));

                buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
                //0 FOR ALL
                buttonsModelList.add(new ButtonsModel(106,"TABLES", "",1, 0));

                buttonsModelList.add(new ButtonsModel(171,"FOR DELIVERY", "",4, 0));
                //"label": "Branch > POS > Function > View Receipt"
                buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
            }
        }
        Collections.sort(buttonsModelList);
        return buttonsModelList;
    }

//    private List<ButtonsModel> organizeButtons(List<String> accessList){
//
//        List<ButtonsModel> buttonsModelList = new ArrayList<>();
//
//        if (accessList.size() > 0) {
//            if (accessList.contains("129")) {
//                //"label": "Branch > POS > Function > Payment"
//                buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
//            }
//            if (accessList.contains("62")) {
//                //"label": "Branch > POS > Function > Discount"
//                buttonsModelList.add(new ButtonsModel(115,"DISCOUNT", "",2, 62));
//            }
//            if (accessList.contains("68")) {
//                //"label": "POS > Function > Void > Item"
//                buttonsModelList.add(new ButtonsModel(101,"ITEM CANCEL", "",4, 68));
//            }
//            if (accessList.contains("127")) {
//                //"label": "Branch > POS > Function > Save Transaction"
//                buttonsModelList.add(new ButtonsModel(100,"PAUSE TRANSACTION", "",5, 127));
//            }
//            if (accessList.contains("128")) {
//                //"label": "Branch > POS > Function > Resume Transaction"
//                buttonsModelList.add(new ButtonsModel(9988,"RESUME TRANSACTION", "",6, 128));
//            }
//            if (accessList.contains("67")) {
//                //"label": "POS > Function > Void > Post"
//                buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
//            }
//            if (accessList.contains("126")) {
//                //"label": "Branch > POS > Function > Sync Data"
//                buttonsModelList.add(new ButtonsModel(134,"SYNC DATA", "",9, 126));
//            }
//            if (accessList.contains("125")) {
//                //"label": "Branch > POS > Function > View Receipt"
//                buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
//            }
//            if (accessList.contains("124")) {
//                //"label": "Branch > POS > Settings"
//                buttonsModelList.add(new ButtonsModel(129,"SETTINGS", "",12, 124));
//            }
//            //0 FOR ALL
//            buttonsModelList.add(new ButtonsModel(99,"CHANGE QTY", "",3, 0));
//            buttonsModelList.add(new ButtonsModel(116,"CANCEL", "",7, 0));
//            buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
//            buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",13, 0));
//            buttonsModelList.add(new ButtonsModel(110,"TEST PRINT", "",13, 0));
//
//            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
//
//            } else {
//                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
//
//                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
//                    if (accessList.contains("69")) {
//                        //"label": "Branch > POS > Function > Switch Room"
//                        buttonsModelList.add(new ButtonsModel(107,"TRANSFER ROOM", "",1, 69));
//                    }
//                    if (accessList.contains("123")) {
//                        //"label": "Branch > POS > Function > SOA"
//                        buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
//                    }
//
//                    //0 FOR ALL
//                    buttonsModelList.add(new ButtonsModel(106,"ROOMS", "",1, 0));
//
//                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
//                    if (accessList.contains("123")) {
//                        //"label": "Branch > POS > Function > SOA"
//                        buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
//                    }
//                    //0 FOR ALL
//                    buttonsModelList.add(new ButtonsModel(106,"TABLES", "",1, 0));
//                }
//            }
//
//        } else {
//
//            //"label": "Branch > POS > Function > Payment"
//            buttonsModelList.add(new ButtonsModel(105,"PAYMENT", "",1, 129));
//            //"label": "Branch > POS > Function > Discount"
//            buttonsModelList.add(new ButtonsModel(115,"DISCOUNT", "",2, 62));
//            //"label": "POS > Function > Void > Item"
//            buttonsModelList.add(new ButtonsModel(101,"ITEM CANCEL", "",4, 68));
//            //"label": "Branch > POS > Function > Save Transaction"
//            buttonsModelList.add(new ButtonsModel(100,"PAUSE TRANSACTION", "",5, 127));
//            //"label": "Branch > POS > Function > Resume Transaction"
//            buttonsModelList.add(new ButtonsModel(9988,"RESUME TRANSACTION", "",6, 128));
//            //"label": "POS > Function > Void > Post"
//            buttonsModelList.add(new ButtonsModel(113,"VOID TRANSACTION", "",8, 67));
//            //"label": "Branch > POS > Function > Sync Data"
//            buttonsModelList.add(new ButtonsModel(134,"SYNC DATA", "",9, 126));
//            //"label": "Branch > POS > Function > View Receipt"
//            buttonsModelList.add(new ButtonsModel(996,"VIEW RECEIPT", "",11, 125));
//            //"label": "Branch > POS > Settings"
//            buttonsModelList.add(new ButtonsModel(129,"SETTINGS", "",12, 124));
//            //0 FOR ALL
//            buttonsModelList.add(new ButtonsModel(99,"CHANGE QTY", "",3, 0));
//            buttonsModelList.add(new ButtonsModel(116,"CANCEL", "",7, 0));
//            buttonsModelList.add(new ButtonsModel(133,"SHIFT CUT OFF", "",10, 0));
//            buttonsModelList.add(new ButtonsModel(997,"LOGOUT", "",13, 0));
//            buttonsModelList.add(new ButtonsModel(110,"TEST PRINT", "",13, 0));
//
//            if (TextUtils.isEmpty(SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE))) {
//
//            } else {
//                if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("QS")) {
//
//                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("hotel")) {
//                    //"label": "Branch > POS > Function > Switch Room"
//                    buttonsModelList.add(new ButtonsModel(107,"TRANSFER ROOM", "",1, 69));
//                    //"label": "Branch > POS > Function > SOA"
//                    buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
//                    //0 FOR ALL
//                    buttonsModelList.add(new ButtonsModel(106,"ROOMS", "",1, 0));
//
//                } else if (SharedPreferenceManager.getString(null, AppConstants.SELECTED_SYSTEM_TYPE).equalsIgnoreCase("restaurant")) {
//                    //"label": "Branch > POS > Function > SOA"
//                    buttonsModelList.add(new ButtonsModel(109,"SOA", "",4, 123));
//                    //0 FOR ALL
//                    buttonsModelList.add(new ButtonsModel(106,"TABLES", "",1, 0));
//                }
//            }
//
//
//        }
//
//
//
//        Collections.sort(buttonsModelList);
//        return buttonsModelList;
//    }

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
