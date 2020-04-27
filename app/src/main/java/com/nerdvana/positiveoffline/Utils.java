package com.nerdvana.positiveoffline;

import android.text.TextUtils;
import android.util.Log;

import androidx.room.util.StringUtil;

import com.nerdvana.positiveoffline.entities.User;
import com.nerdvana.positiveoffline.viewmodel.UserViewModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Utils {

    public static boolean isPasswordProtected(UserViewModel userViewModel, String moduleId) {
        boolean isProtected = true;
        List<String> accessList = new ArrayList<>();
        String[]images = {"", ""};
        try {
            if (userViewModel.searchLoggedInUser().size() > 0) {
                User currentUser = userViewModel.searchLoggedInUser().get(0);
                if (currentUser != null) {
                    if (!TextUtils.isEmpty(currentUser.getAccess())) {
                        accessList = Arrays.asList(currentUser.getAccess().split(","));
                    }
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (accessList.size() > 0) {
            if (accessList.contains(moduleId)) {
                isProtected = false;
            }
        }


        return isProtected;
    }

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

    public static String getPayoutSeriesFormat(String value) {
        String str = String.format("%12s",value);
        return str.replace(' ','0');
    }

    public static String getOrFormat(String value) {
        String str = String.format("%12s",value);
        return str.replace(' ','0');
    }

    public static String getCtrlNumberFormat(String value) {
        String str = String.format("%12s",value);
        return str.replace(' ','0');
    }

    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(c);
    }

    public static String convertDateToReadableDate(String createdAt) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String res = "";
        try {
            DateTime jodatime = dtf.parseDateTime(createdAt);
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MMM d h:m a");
            res = dtfOut.print(jodatime);
        } catch (Exception e) {
            res  = "NA";
        }
        return res.toUpperCase();
    }

}
