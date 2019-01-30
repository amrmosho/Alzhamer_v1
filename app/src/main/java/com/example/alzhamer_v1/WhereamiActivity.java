package com.example.alzhamer_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.alzhamer_v1.libs.maps;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class WhereamiActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    double longitude = 0;
    TextView text_data, text_cor;
    double latitude = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whereami);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_map_white_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        text_data = findViewById(R.id.text_data);
        text_cor = findViewById(R.id.text_cor);


    }

    public void gotoHome(View view) {
        startActivity(new Intent(this , AboutMeAddActivity.class));

    }

    public void reload(View view) {
        getthis_loaction();
    }



    void getthis_loaction() {


       if (maps.turnGPSOn(this)) {

           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this,
                       new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                       123);
           }
           mMap.setMyLocationEnabled(true);
           mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

               @Override
               public void onMyLocationChange(Location arg0) {
                   LatLng sydney = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                   latitude = arg0.getLatitude();
                   longitude = arg0.getLongitude();

                   mMap.addMarker(new MarkerOptions().position(sydney).title("It's Me!"));
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                   getStringLocation(latitude, longitude);
               }
           });
       }
    }




    void getStringLocation(Double latitude, Double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            HashMap<String, String> r = maps.getAddress(this, latitude, longitude);
            text_cor.setText(latitude + " : " + longitude);
            text_data.setText(r.get("address"));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getthis_loaction();
    }


}
