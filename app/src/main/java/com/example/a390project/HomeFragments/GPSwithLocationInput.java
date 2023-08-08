package com.example.a390project.HomeFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a390project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSwithLocationInput extends Fragment implements OnMapReadyCallback {

    private GoogleMap myMap;
    private float floatArg1Value;
    private float floatArg2Value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_p_swith_location_input, container, false);

        Bundle args = getArguments();
        if (args != null) {
            floatArg1Value = args.getFloat("floatArg1Key", 0.0f); // Longitude
            floatArg2Value = args.getFloat("floatArg2Key", 0.0f); // Latitude
        }

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng location = new LatLng(floatArg2Value, floatArg1Value); // Latitude, Longitude
        myMap.addMarker(new MarkerOptions().position(location));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15)); // Zoom level: 15
    }
}
