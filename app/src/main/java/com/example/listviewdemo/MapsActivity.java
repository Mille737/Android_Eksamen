package com.example.listviewdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
    }
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(MapsActivity.this);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle));
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .alpha(0.9f);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMapClickListener(point -> {
            MarkerOptions newMarker = new MarkerOptions()
                    .position(new LatLng(point.latitude, point.longitude))
                    .title("New Marker")
                    .visible(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .alpha(0.9f);
            googleMap.addMarker(newMarker);
        });
        
        LatLng pizza1 = new LatLng(55.69108,12.5300476);
        LatLng pizza2 = new LatLng(55.7049511,12.5332827);
        LatLng pizza3 = new LatLng(55.687996,12.5627984);
        googleMap.addMarker(new MarkerOptions()
                .position(pizza1)
                .title("Da Cavelino")
                .snippet("Lækker pizza")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .alpha(0.9f)
        );
        googleMap.addMarker(new MarkerOptions()
                .position(pizza2)
                .title("Tribeca")
                .snippet("Lækker pizza og Øl")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .alpha(0.9f)
        );
        googleMap.addMarker(new MarkerOptions()
                .position(pizza3)
                .title("Frankies")
                .snippet("Super lækker pizza")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .alpha(0.9f)
        );

        googleMap.setOnMarkerClickListener(marker -> {

            Intent i = new Intent(Intent.ACTION_VIEW);
            String url;

            if (marker.getPosition().equals(pizza1)) {

                System.out.println("TEST123");
                url = "https://www.dacavallino.dk/en/";
                i.setData(Uri.parse(url));
                startActivity(i);

            } else if (marker.getPosition().equals(pizza2)) {

                System.out.println("TEST PIZZA2");
                url = "https://www.tribecanv.dk";
                i.setData(Uri.parse(url));
                startActivity(i);

            } else if (marker.getPosition().equals(pizza3)) {

                System.out.println("Pizza3");
                url = "https://frankiespizza.dk";
                i.setData(Uri.parse(url));
                startActivity(i);

            }
            return true;
        });
    }
}