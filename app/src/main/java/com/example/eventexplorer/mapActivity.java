package com.example.eventexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Get last known location if permission is granted
            getLocation();
        }
    }

    // Callback for permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    // Get the last known location of the device
    private void getLocation() {
        try {
            gMap.setMyLocationEnabled(true); // Enable showing the device's location on the map
            gMap.getUiSettings().setMyLocationButtonEnabled(true); // Enable the "My Location" button

            // Get the last known location
            Location lastLocation = gMap.getMyLocation();
            if (lastLocation != null) {
                LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
