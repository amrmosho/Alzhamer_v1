package com.example.alzhamer_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.Cons_list_adapte;

public class ContactActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_contact_phone_white_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);



        ListView home_list= findViewById(R.id.home_list);

        home_list.setAdapter(new Cons_list_adapte(this, 0, CacheManager.getData(this, "cons")));
    }

    public void gotoHome(View view) {
        startActivity(new Intent(this , HomeActivity.class));

    }

    public void add(View view) {

        startActivity(new Intent(this , AddContectActivity.class));

    }
}
