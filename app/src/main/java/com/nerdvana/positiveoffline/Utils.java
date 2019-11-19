package com.nerdvana.positiveoffline;

import android.text.TextUtils;
import android.util.Log;

import androidx.room.util.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String digitsWithComma(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    public static String getInitials(String value) {
        List<String> finalList = new ArrayList<>();
        if (!TextUtils.isEmpty(value)) {
            String tmp = value.trim().replaceAll("[AEIOUaeiou]", "");
            String[] data = tmp.split(" ");
            for (String d : data) {
                if (d.length() > 4) {
                    finalList.add(d.substring(0, 4));
                } else {
                    finalList.add(d);
                }
            }
        }

        return finalList.size() > 0 ? TextUtils.join(" ", finalList) : "";
    }

}
