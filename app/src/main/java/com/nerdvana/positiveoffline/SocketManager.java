package com.nerdvana.positiveoffline;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.nerdvana.positiveoffline.model.RefreshCashierOrderList;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketManager {
    static Socket mSocket;
    private static Context context;
    private static Handler handler;
    private static boolean isConnected;
    public SocketManager(Context context) {
        SocketManager.context = context;
        initializeSocket();
        handler = new Handler(Looper.getMainLooper());
    }
    public static Socket getInstance() {
        if (mSocket == null) {
            initializeSocket();
        }
        return mSocket;
    }

    public void disconnectSocket() {
        mSocket.disconnect();
    }

    private static void initializeSocket() {
        IO.Options opts = new IO.Options();
        try {
//            mSocket = IO.socket("http://192.168.1.23:6965", opts);

            mSocket = IO.socket(SharedPreferenceManager.getString(null, AppConstants.NORE_URL), opts);

            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d("SUCKET", "CONNECTED");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            isConnected = true;
//                            BusProvider.getInstance().post(new SocketConnectionModel("T"));
                        }
                    });
                }

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    isConnected = false;
                    Log.d("SUCKET", "ERROR");

//                    BusProvider.getInstance().post(new SocketConnectionModel("T"));
                }

            }).on("refreshorder", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("SUCKET", "LOAD ROOM");
                    JSONObject data = (JSONObject) args[0];
                    String company = "";
                    String branch = "";
                    try {
                        company = data.getString("company");
                        branch = data.getString("branch");

                        Log.d("SUCKET0", company + "-" + SharedPreferenceManager.getString(context, AppConstants.BRANCH));
                        Log.d("SUCKET1", branch + "-" + SharedPreferenceManager.getString(context, AppConstants.BRANCH_CODE));

                        if (company.equalsIgnoreCase(SharedPreferenceManager.getString(context, AppConstants.BRANCH)) &&
                                branch.equalsIgnoreCase(SharedPreferenceManager.getString(context, AppConstants.BRANCH_CODE))) {
                            BusProvider.getInstance().post(new RefreshCashierOrderList("yes"));
                        }
                    } catch (JSONException e) {
                        Log.d("SUCKET", e.getMessage());
                        return;
                    }
                }
            });
            if (!mSocket.connected()) {
                mSocket.connect();
            }


//            Log.d("SUCKET", SharedPreferenceManager.getString(SocketManager.context, ApplicationConstants.NODE_URL));
//            if (android.util.Patterns.WEB_URL.matcher(SharedPreferenceManager.getString(SocketManager.context, ApplicationConstants.NODE_URL)).matches()) {
//
//            } else {
//                Toast.makeText(context, "Invalid node url", Toast.LENGTH_LONG).show();
//            }


        } catch (URISyntaxException e) {
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

//    private static void showNotification(String content) {
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(SocketManager.context, "101")
//                .setSmallIcon(R.mipmap.baseline_nfc_black_24)
//                .setContentTitle("Autopilot")
//                .setContentText(content)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(content))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "channelname";
//            String description = "channel description";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("101", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = SocketManager.context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            notificationManager.notify(101, mBuilder.build());
//        }
//    }


    public static void emitToServer(Context context) {
        Log.d("SUCKET_EMIT", "START EMIT");
        try {
            JSONObject emitObj = new JSONObject();
            emitObj.put("company", SharedPreferenceManager.getString(context, AppConstants.BRANCH));
            emitObj.put("branch", SharedPreferenceManager.getString(context, AppConstants.BRANCH_CODE));

            if (mSocket != null) {
                mSocket.emit("refreshorder",emitObj);
                Log.d("SUCKET_EMIT", "SUCCESS EMIT");
            } else {
                Log.d("SUCKET_EMIT", "FAIL EMIT");
            }
        } catch (Exception e) {
            Log.d("SUCKET-ERR", e.getMessage());
        }



    }

}
