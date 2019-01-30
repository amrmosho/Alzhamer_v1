package com.example.alzhamer_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alzhamer_v1.libs.CacheManager;
import com.example.alzhamer_v1.libs.Json;
import com.example.alzhamer_v1.libs.maps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    LatLng origin = new LatLng(l1.getLatitude() ,l1.getLongitude() );
    LatLng dest =new LatLng(l2.getLatitude() ,l2.getLongitude() );

    // Getting URL to the Google Directions API
    String url = getDirectionsUrl(origin, dest);

    DownloadTask downloadTask = new DownloadTask();

    // Start downloading json data from Google Directions API
    downloadTask.execute(url);


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



    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                com.journaldev.maproutebetweenmarkers.DirectionsJSONParser parser = new com.journaldev.maproutebetweenmarkers.DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route

            if (points!=null){
            mMap.addPolyline(lineOptions);}
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
