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
import android.widget.Toast;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.Json;
import com.example.alzhamer_v1.libs.maps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;

public class SosActivity   extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double longitude = 0;
    TextView text_data, text_cor;
   double latitude = 0;

    Double Home_lat=0.0;
    Double Home_lng=0.0;

    public void gotoHome(View view) {
        startActivity(new Intent(this , HomeActivity.class));

    }

    public void reload(View view) {
        getthis_loaction();
    }






    Marker thisMarker ;





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
                    longitude =arg0.getLongitude();
                            latitude =arg0.getLatitude();

                            if (thisMarker!=null){
                                thisMarker.remove();

                            }

                    thisMarker =  maps.getloction(mMap, latitude,longitude);
                    getStringLocation(  latitude,longitude);

                    updateDis();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_map_white_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);



        text_data = findViewById(R.id.text_data);
        text_cor = findViewById(R.id.text_cor);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }






float defdis =1000;
    float dis =1000;


    void updateDis(){


    Location l2 = new Location("");
    l2.setLatitude(Home_lat);
    l2.setLongitude(Home_lng);

    Location l1 = new Location("");
    l1.setLatitude( latitude);
    l1.setLongitude(longitude);


  float di =  l2.distanceTo(l1);




if (di>defdis && di>dis ){

       dis =di+30;


       text_data.setTextColor(R.color.colorError);
       Json j = new Json();
       j.sendNotificatio(this, R.drawable.ic_error_white_24dp, "error", " you very far from Home  ("+di+")");



}else{

    text_data.setTextColor(R.color.colorNormal);


}

    text_data.setText(""+di +" meters ");


}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<HashMap<String, String>> alldata= CacheManager.getData(this, "aboutme");
        if (!alldata.isEmpty()) {
            String[] s=    alldata.get(0).get("location").split(",");

            Home_lat = Double.parseDouble(s[0]);
            Home_lng = Double.parseDouble(s[1]);

            maps.getloction(mMap,Home_lat,Home_lng);

        }else{
            Toast.makeText(this,"Pleas add Your Location",Toast.LENGTH_LONG);



            startActivity(new Intent(this , AboutMeAddActivity.class));



        }




        getthis_loaction();
    }
}
