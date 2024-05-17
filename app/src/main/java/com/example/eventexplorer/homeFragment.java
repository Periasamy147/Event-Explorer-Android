package com.example.eventexplorer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.OnMapReadyCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class homeFragment extends Fragment implements OnMapReadyCallback {

    private TextView event1TextView, event2TextView, event3TextView, event4TextView, event5TextView;
    private GoogleMap gMap;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize event TextViews
        event1TextView = rootView.findViewById(R.id.event1);
        event2TextView = rootView.findViewById(R.id.event2);
        event3TextView = rootView.findViewById(R.id.event3);
        event4TextView = rootView.findViewById(R.id.event4);
        event5TextView = rootView.findViewById(R.id.event5);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        // Set click listeners for event TextViews
        event1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event1TextView.getText().toString());
            }
        });

        event2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event2TextView.getText().toString());
            }
        });

        event3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event3TextView.getText().toString());
            }
        });

        event4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event4TextView.getText().toString());
            }
        });

        event5TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToEventActivity(event5TextView.getText().toString());
            }
        });

        // Set click listener for mapsButton

        return rootView;
    }

    private void redirectToEventActivity(String eventName) {
        Intent intent = new Intent(getActivity(), eventActivity.class);
        intent.putExtra("eventName", eventName); // Pass event name to EventActivity
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
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
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//                gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
