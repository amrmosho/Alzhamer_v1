package com.example.alzhamer_v1.libs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by empcl on 09 - 11 - 2017.
 */

public class Json {

    String TAG = "";

    private static HashMap<String, String> josnMap(JSONObject Row) {
        HashMap<String, String> r = new HashMap<String, String>();
        try {
            Iterator<String> ir = Row.keys();
            while (ir.hasNext()) {
                String currentKey = ir.next();
                r.put(currentKey, Row.getString(currentKey));
            }

        } catch (JSONException e) {
            Log.d("", "  Error" + e.getMessage());
        }
        return r;
    }

    private static ArrayList<HashMap<String, String>> josnsMap(JSONObject Row) {
        ArrayList<HashMap<String, String>> returndata = new ArrayList();

        try {
            Iterator<String> ir = Row.keys();
            while (ir.hasNext()) {
                String currentKey = ir.next();
                JSONArray subdata = Row.getJSONArray(currentKey);

                for (int i = 0; i < subdata.length(); i++) {
                    returndata.add(josnMap(subdata.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            Log.d("", "  Error" + e.getMessage());
            /// ui.send_msg(null, e.getMessage());

        }
        return returndata;
    }


    public static ArrayList<HashMap<String, String>> jsonToMap(String sdata) {
        ArrayList<HashMap<String, String>> returndata = new ArrayList();
        try {
            JSONArray a = new JSONArray(sdata);
            //     JSONObject Row = new JSONObject(sdata);

            for (int i = 0; i < a.length(); i++) {
                returndata.add(josnMap(a.getJSONObject(i)));
            }
        } catch (ClassCastException e) {
            Log.d("xxxxx", "  Error" + e.getMessage());
        } catch (NullPointerException e) {
            Log.d("xxxxx", "  Error" + e.getMessage());
        } catch (JSONException e) {
            Log.d("xxxxx", "  Error" + e.getMessage());
        }

        return returndata;
    }

    public static String MapTojson(ArrayList<HashMap<String, String>> sdata) {

        String r = "";
        try {

            List<JSONObject> jsonObj = new ArrayList<JSONObject>();
            for (HashMap<String, String> data : sdata) {
                JSONObject obj = new JSONObject(data);
                jsonObj.add(obj);
            }

            JSONArray test = new JSONArray(jsonObj);
            r = test.toString();
        } catch (ClassCastException e) {
            Log.d("xxxxx", "  Error" + e.getMessage());
        } catch (NullPointerException e) {
            Log.d("xxxxx", "  Error" + e.getMessage());
        }
        return r;
    }


    public void sendNotificatio(Context context, int icon, String title, String contant) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(contant);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        notificationManager.notify(1, mBuilder.build());


    }


}
