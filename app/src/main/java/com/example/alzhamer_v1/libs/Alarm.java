package com.example.alzhamer_v1.libs;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.alzhamer_v1.R;

/**
 * Created by empcl on 12 - 11 - 2017.
 */

public class Alarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "";
        String contant = "";



        int icon = R.drawable.logo;
        if (intent.hasExtra("icon")) {
            icon = intent.getIntExtra("icon", R.drawable.logo);
        }




        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title");
        }


        if (intent.hasExtra("content")) {
            contant = intent.getStringExtra("content");
        }



/*

        Intent intent2 = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntentMaybe = PendingIntent.getActivity(context, 12345, intent2, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context);
        notBuilder.setAutoCancel(true);
        notBuilder.setSmallIcon(icon);

       // notBuilder.setTicker("string tiker");
       //  notBuilder.setWhen(System.currentTimeMillis());
        //notBuilder.setContentTitle(  title);
        //notBuilder.setContentText(contant);
     //   notBuilder.setContentIntent(pendingIntentMaybe);
     //   notBuilder.addAction(android.R.drawable.ic_menu_camera, context.getString(R.string.notbtn), pendingIntentMaybe);

     //   notBuilder.setSound(Settings.System.DEFAULT_RINGTONE_URI);
     //   notif.notify(0, notBuilder.build());

*/
        Json j= new Json();
        j.sendNotificatio(context,icon,title,contant);


     //   onSendClick(context, context.getString(R.string.nottitle) + " : " + title + " "+contant);

/*
        MediaPlayer mp =MediaPlayer.create(context ,Settings.System.DEFAULT_NOTIFICATION_URI);
        mp.start();*/
    }
/*
    SmsManager smsManager = SmsManager.getDefault();

    public void onSendClick(Context c, String mesg) {

        if (ContextCompat.checkSelfPermission(c, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //   getPermissionToReadSMS();
        } else {

            if (options.thisAccount.containsKey("phone")) {


//                smsManager.sendTextMessage(options.thisAccount.get("phone"), null, mesg, null, null);

            }

            //  Toast.makeText(c, "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void myIntentToButtonOneScreen() {


    }



}
