package com.example.listviewdemo.handler;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.listviewdemo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapHandler {

    private GoogleMap mMap;
    private Activity activity;
    private Marker marker1;

    public MapHandler(GoogleMap mMap,Activity c) {
        this.mMap = mMap;
        activity = c;
        handleOnMapReady();
    }

    public void addMarker(MarkerOptions markerOptions){
        activity.runOnUiThread(()->{
            mMap.addMarker(markerOptions);
        });
    }

    private void handleOnMapReady(){
        mMap.setOnMapLongClickListener(latLng -> {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.car1);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap);
            LatLng loc = new LatLng(latLng.latitude, latLng.longitude);
            Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
            String a = "";
            try {
                List<Address> addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1);
                a = addresses.get(0).toString();
                Log.i("PlaceInfo", a);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(loc).title(a));
            //mMap.addMarker(new MarkerOptions().position(loc).title("Marker in Sydney").icon(bitmapDescriptor));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.equals(marker1)){
                    Log.w("Clicked", "Test");
                    System.out.println("Test1234567");
                    return true;
                }
                return false;
            }
        });

    }

}

