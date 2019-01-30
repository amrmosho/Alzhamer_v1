package com.example.alzhamer_v1.libs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.alzhamer_v1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class Map implements OnMapReadyCallback, View.OnClickListener {

    Context context;

public Map(Context mycontext){
    context=mycontext;

}
    public Map(Context mycontext ,TextView txtview){
        this.context=mycontext;
        this.textview=txtview;
    }

    Boolean UPDATE_LOCTION = true;
    TextView text_data ,txt_map_address;

    public double latitude = 0;
    public double longitude = 0;

     TextView textview;

    Dialog dialog;
   public   Button  getLoctions_Dialog( ){



         dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_map);
        dialog.show();




        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(context);

        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
         mMapView.getMapAsync(this);

        Button button = dialog.findViewById(R.id.get_loction);

        text_data = dialog.findViewById(R.id.map_data);
       txt_map_address = dialog.findViewById(R.id.txt_map_address);
       button.setOnClickListener(this);




       return  button;


    }
    GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {



                if (latitude!=0 &&  longitude!=0 ) {
                    LatLng sydney = new LatLng(latitude,  longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney).title("HERE"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                    getStringLocation(latitude, longitude);


                } else {

                    getloction();


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


         // markerOptions.title(latLng.latitude + " : " + latLng.longitude);


                  mMap.clear();

                  mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    mMap.addMarker(markerOptions);





                    if (latLng.latitude != 0) {

                        getStringLocation(latLng.latitude, latLng.longitude);
                        UPDATE_LOCTION = false;

                    }
                }
            });
        } catch (NullPointerException e) {
        }

    }









    void getStringLocation(Double latitude, Double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {

            this.latitude =latitude;
            this. longitude =longitude;

            HashMap<String, String> r = maps.getAddress(context, latitude, longitude);


           text_data.setText(latitude + " : " + longitude );
           txt_map_address.setText(r.get("address"));


        }
    }



    void getloction() {


        if (maps.turnGPSOn(context)) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
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
                        mMap.addMarker(new MarkerOptions().position(sydney).title("It's Me!"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                        getStringLocation(arg0.getLatitude(),  arg0.getLongitude());

                    }


                }
            });

        }
    }


    @Override
    public void onClick(View v) {


        if (textview != null){

            textview.setText(latitude + " , " +longitude);
            dialog.dismiss();

        }
    }
}
