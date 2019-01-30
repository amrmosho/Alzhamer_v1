package com.example.alzhamer_v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.options;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AboutmeActivity extends AppCompatActivity {

    TextView txt_name,txt_phone,txt_live_at,txt_born_date,txt_blood_group,txt_age;
    HashMap<String, String> data =new   HashMap();
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_account_circle_black_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);


        txt_name =findViewById(R.id.txt_name);
        txt_phone =findViewById(R.id.txt_phone);
        txt_live_at =findViewById(R.id.txt_live_at);
        txt_born_date =findViewById(R.id.txt_born_date);
        txt_blood_group =findViewById(R.id.txt_blood_group);
        txt_age =findViewById(R.id.txt_age);



        img =findViewById(R.id.img);






try{

    ArrayList<HashMap<String, String>> alldata= CacheManager.getData(this, "aboutme");
if (!alldata.isEmpty()){
       data   =  alldata.get(0);

        txt_name.setText(data.get("name"));
        txt_phone.setText(data.get("phone"));
        txt_live_at.setText(data.get("live_it")+"\n"+data.get("location"));
        txt_born_date.setText(data.get("born_date"));
        txt_blood_group.setText(data.get("blood_group"));
        txt_age.setText(data.get("age"));







        String path = CacheManager.getfile_path(this, data.get("image"));

        if (path != null) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);
            }
        } }else{

    startActivity(new Intent(this , AboutMeAddActivity.class));


}
}catch (NullPointerException e){

}

    }


    public void gotohome(View view) {

        startActivity(new Intent(this, HomeActivity.class));

    }


    public void addNew(View view) {

        startActivity(new Intent(this , AboutMeAddActivity.class));

    }

    public void show_map(View view) {

        if (data.containsKey("location")){

         String[] s=   data.get("location").split(",");
            options.location.put("lng",s[1]);
            options.location.put("lat",s[0]);

            Intent i=     new Intent(this , GetLoctionActivity.class);
            i.putExtra("status","show");
            i.putExtra("from","aboutmemain");




            startActivity(i);



        }

    }
}
