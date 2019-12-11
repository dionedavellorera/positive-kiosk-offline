package com.nerdvana.positiveoffline;

import android.text.TextUtils;
import android.util.Log;

import androidx.room.util.StringUtil;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

    public static String digitsWithComma(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(value);
    }

    public static Double roundedOffTwoDecimal(Double value) {
        return Double.valueOf(String.format("%.2f", value));
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

    public static final String getDateTimeToday() {
        DateTime dateTime = new DateTime();
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public static String getOrFormat(String value) {
        String str = String.format("OR-%8s",value);
        return str.replace(' ','0');
    }

    public static String getCtrlNumberFormat(String value) {
        String str = String.format("CN-%8s",value);
        return str.replace(' ','0');
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(c);
    }

}
