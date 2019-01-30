package com.example.alzhamer_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alzhamer_v1.libs.maps;
import com.example.alzhamer_v1.libs.options;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class GetLoctionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String status = "", from = "";

    double longitude = 0;
    TextView text_data;
    double latitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_loction);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_map_white_24dp);

        getSupportActionBar().setDisplayUseLogoEnabled(true);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

        text_data = findViewById(R.id.text_data);

        Button button_select = findViewById(R.id.button11);


        status = getIntent().getStringExtra("status");
        if (getIntent().hasExtra("status")) {
            status = getIntent().getStringExtra("status");
            if (status.equalsIgnoreCase("show")) {

                button_select.setVisibility(View.GONE);

            }
        }
        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }
    }


    Location sydney;


    Boolean UPDATE_LOCTION = true;

    void getloction() {


        if (maps.turnGPSOn(this)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {


                    if (UPDATE_LOCTION) {

                        LatLng sydney = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                        latitude = arg0.getLatitude();
                        longitude = arg0.getLongitude();

                        mMap.addMarker(new MarkerOptions().position(sydney).title("It's Me!"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                        getStringLocation(latitude, longitude);

                    }


                }
            });

        }
    }


    void getStringLocation(Double latitude, Double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            HashMap<String, String> r = maps.getAddress(this, latitude, longitude);
            text_data.setText(latitude + " : " + longitude + "\n" + r.get("address"));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {


            if (status.equals("creat")) {
                getloction();
            } else {


                if ((!options.location.get("lng").equals("") && !options.location.get("lat").equals(""))) {
                    LatLng sydney = new LatLng(Double.parseDouble(options.location.get("lat")), Double.parseDouble(options.location.get("lng")));

                    latitude = Double.parseDouble(options.location.get("lat"));
                    longitude = Double.parseDouble(options.location.get("lng"));
                    mMap.addMarker(new MarkerOptions().position(sydney).title("HERE"));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                    getStringLocation(latitude, longitude);


                } else {

                    getloction();


                }
            }

        } catch (NullPointerException e) {
            getloction();

        }


        try {


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    latitude  = latLng.latitude;
                     longitude = latLng.longitude;

                    mMap.clear();

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.addMarker(markerOptions);


                    if (latitude != 0) {

                        getStringLocation(latitude, longitude);
                        UPDATE_LOCTION = false;

                    }
                }
            });
        } catch (NullPointerException e) {
        }

    }


    public void update_ll(View view) {
        HashMap<String, String> r = maps.getAddress(this, latitude, longitude);

        options.location.put("lat", latitude + "");
        options.location.put("lng", longitude + "");
        options.location.put("address", r.get("address"));


        if (from.equalsIgnoreCase("addContact")) {

            startActivity(new Intent(this, AddContectActivity.class));
        } else if (from.equalsIgnoreCase("aboutme")) {

            startActivity(new Intent(this, AboutMeAddActivity.class));

        } else {


            onBackPressed();
        }


    }


    public void cancel_bck(View view) {
        onBackPressed();

    }
}
