package com.example.a390project.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.R;
import com.example.a390project.view.DisplayActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorInfoFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private TextView MQ135ValueTextView;
    private TextView FlameValueTextView;
    private TextView HeartRateValueTextView;
    private TextView sensor4ValueTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_info, container, false);
        MQ135ValueTextView = rootView.findViewById(R.id.sensor1Value);
        FlameValueTextView = rootView.findViewById(R.id.sensor2Value);
        HeartRateValueTextView = rootView.findViewById(R.id.sensor3Value);
        sensor4ValueTextView = rootView.findViewById(R.id.sensor4Value);
        //above are the edittext for each sensors
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("message");
        getdata();

        return rootView;
    }

    public void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                MQ135ValueTextView.setText(value);
                FlameValueTextView.setText(value);
                HeartRateValueTextView.setText(value);
                sensor4ValueTextView.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
