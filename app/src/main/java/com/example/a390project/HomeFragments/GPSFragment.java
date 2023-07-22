package com.example.a390project.HomeFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.a390project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSFragment extends Fragment {

    private EditText longitudeEditText;
    private EditText altitudeEditText;
    private GoogleMap myMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_g_p_s, container, false);

        longitudeEditText = view.findViewById(R.id.longitudeEditText);
        altitudeEditText = view.findViewById(R.id.altitudeEditText);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                myMap = googleMap;
            }
        });

        Button applyButton = view.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocation();
            }
        });

        return view;
    }

    private void displayLocation() {
        String longitudeStr = longitudeEditText.getText().toString();
        String altitudeStr = altitudeEditText.getText().toString();

        double longitude = Double.parseDouble(longitudeStr);
        double altitude = Double.parseDouble(altitudeStr);

        LatLng latLng = new LatLng(longitude,altitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + "KG" + latLng.longitude);

        myMap.clear();
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        myMap.addMarker(markerOptions);
    }
}
