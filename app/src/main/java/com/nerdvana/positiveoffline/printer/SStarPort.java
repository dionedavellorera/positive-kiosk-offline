package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import java.util.concurrent.ExecutionException;

public class SStarPort {
    private static StarIOPort starIOPort;
    public SStarPort(Context context) {
        if (starIOPort == null) {
            try {
                starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 0, context);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static StarIOPort getStarIOPort() {
        return starIOPort;
    }

    public static void changePort(final Context context) {
        Log.d("PORT", "CHANGING PORT");

        Log.d("PORT", SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY));
        if (starIOPort != null) {
//            starIOPort = null;
            try {
                StarIOPort.releasePort(starIOPort);

                new AsyncTask<Void, Void, StarIOPort>() {

                    @Override
                    protected StarIOPort doInBackground(Void... voids) {

                        try {
                            synchronized (context) {
                                Log.d("PORT", "CHANGING PORT TRYING");
                                starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                                if (starIOPort != null) {
                                    Log.d("PORT", "MERON NA DIN");
                                }
                            }
                        } catch (StarIOPortException e) {
                            // Do Nothing
                            Log.d("PORT", e.getLocalizedMessage());
                        }
                        return starIOPort;
                    }

                    @Override
                    protected void onPostExecute(StarIOPort port) {

                    }
                }.execute();


            } catch (StarIOPortException e) {
                e.printStackTrace();
            }
        } else {
            new AsyncTask<Void, Void, StarIOPort>() {

                @Override
                protected StarIOPort doInBackground(Void... voids) {

                    try {
                        synchronized (context) {
                            Log.d("PORT", "CHANGING PORT TRYING 1");
                            starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                            if (starIOPort != null) {
                                Log.d("PORT", "MERON NA");
                            }
                        }
                    } catch (StarIOPortException e) {
                        Log.d("PORT", e.getLocalizedMessage());
                        // Do Nothing
                    }
                    return starIOPort;
                }

                @Override
                protected void onPostExecute(StarIOPort port) {
                    Toast.makeText(context, "SUCCESSFULLY CONNECTED TO PRINTER", Toast.LENGTH_SHORT).show();
                }
            }.execute();


        }



    }
}

