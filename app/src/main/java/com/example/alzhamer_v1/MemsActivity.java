package com.example.alzhamer_v1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.alzhamer_v1.libs.Alarm;
import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.res_list_adapte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mems);


        ListView home_list = findViewById(R.id.home_list);
        ArrayList<HashMap<String, String>> data=     CacheManager.getData(this, "memos");


        home_list.setAdapter(new res_list_adapte(this, 0,data));

        for(HashMap<String, String> d:data){
          String[] time=  d.get("time").split(":");

            d.put("h",time[0]);
            d.put("m",time[1]);

            newAlram(d);

        }



    }


    public void gotoHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));

    }

    public void add(View view) {

        startActivity(new Intent(this, MemsAddActivity.class));




    }


    long prv_alrm_time = 0;
    long next_alrm_time = Long.MAX_VALUE;
    AlarmManager alarmManager;
    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();


    void newAlram(HashMap<String, String> data) {


        Calendar calendar = Calendar.getInstance();

        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                Integer.parseInt(data.get("h").trim()),
                Integer.parseInt(data.get("m").trim())

        );

        setAlarm(calendar.getTimeInMillis(), data);


    }


    void setAlarm(Long time, HashMap<String, String> data) {


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("title", data.get("title"));
        intent.putExtra("content", (data.get("notes")));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    List<String>   days= Arrays.asList(data.get("days").split(";"));


    if (days.contains("all")){
        alarmManager.setRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_DAY, pendingIntent);
    }


        intentArray.add(pendingIntent);


    }


/*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    void setalarm(HashMap<String, String> data) {


        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data.get("h").trim()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(data.get("m").trim()));
        calendar.set(Calendar.SECOND, 0);



        long time = calendar.getTimeInMillis();


        Date a = calendar.getTime();

        if (time > System.currentTimeMillis()) {

            if (time < next_alrm_time) {

                next_alrm_time = time;
                next_alrm = data;
            }


            if (data != null && data.size() > 0) {


                Intent intent = new Intent(this, Alarm.class);

                intent.putExtra("title", data.get("name"));
                intent.putExtra("content", (data.get("nots") + new SimpleDateFormat("h:m").format(thisDate)) + ":" + (new SimpleDateFormat("h:m").format(a)));


                if (data.containsKey("image")) {
                    int image = getResources().getIdentifier(data.get("image"), "drawable", getPackageName());
                    intent.putExtra("icon", image);
                }

                intent.putExtra("id", data.get("id"));


                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,(int) time, intent, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                intentArray.add(pendingIntent);
            }


        } else {

            if (time > prv_alrm_time) {

                prv_alrm_time = time;
                prv_alrm = data;
            }


        }

    }
*/


}
