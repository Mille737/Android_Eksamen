package com.example.listviewdemo;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.listviewdemo.handler.MapHandler;
import com.example.listviewdemo.handler.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapHandler mapHandler;
    private LocationManager locationManager; // holder styr på start og stop
    private LocationListener locationListener; // det objekt som får location events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        checkPermissionsAndRequestUpdates();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void checkPermissionsAndRequestUpdates() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){ // hvis vi ikke har permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){ // hvis vi har permission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            }
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapHandler = new MapHandler(googleMap, this);

        LatLng pizza1 = new LatLng(55.69108,12.5300476);
        LatLng pizza2 = new LatLng(55.7049511,12.5332827);
        LatLng pizza3 = new LatLng(55.687996,12.5627984);
        googleMap.addMarker(new MarkerOptions()
                .position(pizza1)
                .title("Da Cavelino")
                .snippet("Lækker pizza")
                );
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pizza1));

        googleMap.addMarker(new MarkerOptions()
                .position(pizza2)
                .title("Tribeca")
                .snippet("Lækker pizza og Øl"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pizza2));

        googleMap.addMarker(new MarkerOptions()
                .position(pizza3)
                .title("Frankies")
                .snippet("Super lækker pizza"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pizza3));

        googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                String url;

                if (marker.getPosition().equals(pizza1)){

                    System.out.println("TEST123");
                    url = "https://www.dacavallino.dk/en/";
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
                else if (marker.getPosition().equals(pizza2)){

                    System.out.println("TEST PIZZA2");
                    url = "https://www.tribecanv.dk";
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
                else if (marker.getPosition().equals(pizza3)){

                    System.out.println("Pizza3");
                    url = "https://frankiespizza.dk";
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }

                return true;
            }
        });
    }
}