package com.nerdvana.positiveoffline.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nerdvana.positiveoffline.R;
import com.nerdvana.positiveoffline.Utils;
import com.nerdvana.positiveoffline.base.BaseDialog;
import com.nerdvana.positiveoffline.entities.CashDenomination;
import com.nerdvana.positiveoffline.model.SafeKeepDataModel;
import com.nerdvana.positiveoffline.viewmodel.DataSyncViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CollectionDialog extends BaseDialog {
    private TextView totalSafeKeep;
    private List<SafeKeepDataModel> safeKeepDataModelList;

    private LinearLayout relContainer;
    private Double totalSafeKeepAmountDisp = 0.00;
    private DataSyncViewModel dataSyncViewModel;
    public CollectionDialog(Context context, DataSyncViewModel dataSyncViewModel) {
        super(context);
        this.dataSyncViewModel = dataSyncViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogLayout(R.layout.dialog_collection, "COLLECTION");
        initViews();
        setCashDenomination();
    }

    private void initViews() {
        relContainer = findViewById(R.id.relContainer);
        totalSafeKeep = findViewById(R.id.totalSafeKeep);
        safeKeepDataModelList = new ArrayList<>();
    }

    private void setCashDenomination(){
        try {
            for (CashDenomination cashDenomination : dataSyncViewModel.getCashDeno()) {
                addView(cashDenomination.getDenomination(), String.valueOf(cashDenomination.getAmount()), cashDenomination.getCore_id());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addView(String amountToDisplay, final String actualAmount, int id) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        linearLayout.setLayoutParams(parentParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(1);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.5f);

        TextView textView = new TextView(getContext());
        textView.setHeight(50);
        textView.setGravity(Gravity.CENTER);
        textView.setText(amountToDisplay);
        textView.setLayoutParams(params1);
        linearLayout.addView(textView);

        final EditText editText = new EditText(getContext());
        editText.setHeight(50);
        editText.setHint(actualAmount);
        editText.setId(id);
        editText.setLayoutParams(params1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextIsSelectable(true);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                totalSafeKeepAmountDisp = 0.00;

                for (SafeKeepDataModel skdm : safeKeepDataModelList) {
                    if (!TextUtils.isEmpty(skdm.getEditText().getText().toString())) {
                        totalSafeKeepAmountDisp += Double.valueOf(skdm.getValue()) * Double.valueOf(skdm.getEditText().getText().toString());
                    }

                }

                totalSafeKeep.setText(String.format("PHP %s", Utils.digitsWithComma(totalSafeKeepAmountDisp)));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(editText);

        safeKeepDataModelList.add(new SafeKeepDataModel(editText, actualAmount));
//        safeKeepDataModelListDisplay.add(new SafeKeepDataModel(editText, actualAmount));

        relContainer.addView(linearLayout);
    }


}
