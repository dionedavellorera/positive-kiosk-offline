package com.nerdvana.positiveoffline;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Helper {
    public static void showDialogMessage(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        if (!alert.isShowing()) {
            alert.show();
        }
    }

    public static String durationOfStay(String dateToday, String checkInDate) {
        String finalString = "";
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime today = formatter.parseDateTime(dateToday);
        DateTime yesterday = formatter.parseDateTime(checkInDate);
        Duration duration = new Duration(yesterday, today);

        Double days_to_hours = 0.00;
        Double running_hours = 0.00;
        Double running_hours_to_minutes = 0.00;
        Double running_minutes = 0.00;
        if (duration.getStandardDays() > 0) {
            days_to_hours = Double.valueOf(duration.getStandardDays() * 24);
            running_hours = duration.getStandardHours() - days_to_hours;
            running_hours_to_minutes = days_to_hours * 60;
            running_minutes = Double.valueOf(String.valueOf((duration.getStandardMinutes() - running_hours_to_minutes) % 60));
            finalString = String.format("%s days, %s hrs, %s mins", (int) duration.getStandardDays(),(int)Math.round(running_hours), (int)Math.round(running_minutes));
        } else {
            running_hours = duration.getStandardHours() - days_to_hours;
            running_hours_to_minutes = days_to_hours * 60;
            running_minutes = Double.valueOf(String.valueOf((duration.getStandardMinutes() - running_hours_to_minutes) % 60));
            finalString = String.format("%s hrs, %s mins", (int)Math.round(running_hours), (int)Math.round(running_minutes));
        }
        return finalString;
    }

}
