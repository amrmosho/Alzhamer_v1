package com.example.alzhamer_v1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void gotoContact(View view) {

        startActivity(new Intent(this , ContactActivity.class));

    }

    public void gotowhere(View view) {
        startActivity(new Intent(this , WhereamiActivity.class));

    }

    public void gotosettings(View view) {
    }

    public void gotowhat(View view) {
        startActivity(new Intent(this , HereActivity.class));

    }

    public void gotoAboutme(View view) {
        startActivity(new Intent(this , AboutmeActivity.class));

    }

    public void gotoMems(View view) {
        startActivity(new Intent(this , MemsActivity.class));

    }

    public void gotoSOS(View view) {
        startActivity(new Intent(this , SosActivity.class));

    }

    public void gotoAboutUS(View view) {
    }
}
