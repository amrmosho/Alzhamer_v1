package com.example.alzhamer_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alzhamer_v1.libs.CacheManager;

import java.util.HashMap;

public class HereActivity extends AppCompatActivity {
    TextView txt_here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_error_white_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);


        txt_here = findViewById(R.id.txt_here);
        try {


        data = CacheManager.getData(this, "here_data").get(0);
        if (!data.isEmpty()) {
            id = data.get("id");
            txt_here.setText(data.get("here"));
        } }catch (Exception e){}
    }

    HashMap<String, String> data;
    String id = "";

    public void gotohome(View view) {

        startActivity(new Intent(this, HomeActivity.class));

    }

    public void save(View view) {


        HashMap<String, String> data = new HashMap<>();


        if (txt_here.getText().toString().trim() != "") {

            data.put("here", txt_here.getText().toString());


            if (id.equals("")) {


                CacheManager._insert(this, "here_data", data);

            } else {

                CacheManager._update(this, "here_data", id, data);

            }


            Toast.makeText(this, "Save Done successfully", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "You Name IS Empty", Toast.LENGTH_LONG).show();

        }


    }
}
