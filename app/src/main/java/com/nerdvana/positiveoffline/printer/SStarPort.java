package com.nerdvana.positiveoffline.printer;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.nerdvana.positiveoffline.AppConstants;
import com.nerdvana.positiveoffline.SharedPreferenceManager;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import java.util.concurrent.ExecutionException;

public class SStarPort {
    private static StarIOPort starIOPort;
    public SStarPort(Context context) {
        if (starIOPort == null) {
            try {
                starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
            } catch (StarIOPortException e) {
                e.printStackTrace();
            }
        }
    }

    public static StarIOPort getStarIOPort() {
        return starIOPort;
    }

    public static void changePort(final Context context) {

        if (starIOPort != null) {
            starIOPort = null;
            try {
                StarIOPort.releasePort(starIOPort);

                try {
                    new AsyncTask<Void, Void, StarIOPort>() {

                        @Override
                        protected StarIOPort doInBackground(Void... voids) {

                            try {
                                synchronized (context) {
                                    starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                                }
                            } catch (StarIOPortException e) {
                                // Do Nothing
                            }
                            return starIOPort;
                        }

                        @Override
                        protected void onPostExecute(StarIOPort port) {

                        }
                    }.execute().get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (StarIOPortException e) {
                e.printStackTrace();
            }
        } else {
            try {
                new AsyncTask<Void, Void, StarIOPort>() {

                    @Override
                    protected StarIOPort doInBackground(Void... voids) {

                        try {
                            synchronized (context) {
                                starIOPort = StarIOPort.getPort(SharedPreferenceManager.getString(context, AppConstants.SELECTED_PRINTER_MANUALLY), "", 2000, context);
                            }
                        } catch (StarIOPortException e) {
                            // Do Nothing
                        }
                        return starIOPort;
                    }

                    @Override
                    protected void onPostExecute(StarIOPort port) {

                    }
                }.execute().get();


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }



    }
}

