package com.example.alzhamer_v1.libs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.example.alzhamer_v1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by empcl on 01 - 09 - 2017.
 */

public class maps {

    /**
     *
     * @param c
     * @param latitude
     * @param longitude
     * @return [address,city,state,country,postalCode,knownName]
     */
    public static HashMap<String, String> getAddress(Context c, double latitude, double longitude) {
        HashMap<String, String> r = new HashMap();

        try {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(c, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            r.put("address", addresses.get(0).getAddressLine(0));
            r.put("city", addresses.get(0).getLocality());
            r.put("state", addresses.get(0).getAdminArea());
            r.put("country", addresses.get(0).getCountryName());
            r.put("postalCode", addresses.get(0).getPostalCode());
            r.put("knownName", addresses.get(0).getFeatureName());


        } catch (IOException e) {
            e.printStackTrace();

    } catch (Exception e) {
            e.printStackTrace();
        }
        return r;


    }


    public static Marker getloction(GoogleMap mMap, double latitude , double longitude ){

        LatLng sydney = new LatLng(latitude, longitude);

        Marker m =  mMap.addMarker(new MarkerOptions().position(sydney).title("It's Me!"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
return m ;
    }




    public static Location getthis_loaction(Context c, final GoogleMap mMap) {


        final Location[] l = new Location[1];

        if (maps.turnGPSOn(c)) {

            if (ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) c,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        123);
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    LatLng    sydney = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                 /*   latitude = arg0.getLatitude();
                    longitude = arg0.getLongitude();*/

                 l[0] =arg0;
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Im Here!"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                }
            });

        }

        return l[0];
    }


    public static boolean turnGPSOn(final Context context) {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }


        return (gps_enabled || network_enabled);

    }


}
