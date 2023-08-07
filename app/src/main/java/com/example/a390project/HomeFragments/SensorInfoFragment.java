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
    private TextView sensor1BatteryValueTextView;
    private TextView sensor2BatteryValueTextView;
    private TextView sensor3BatteryValueTextView;
    private TextView sensor4BatteryValueTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_info, container, false);
        MQ135ValueTextView = rootView.findViewById(R.id.sensor1Value);
        FlameValueTextView = rootView.findViewById(R.id.sensor2Value);
        HeartRateValueTextView = rootView.findViewById(R.id.sensor3Value);
        sensor4ValueTextView = rootView.findViewById(R.id.sensor4Value);
        sensor1BatteryValueTextView=rootView.findViewById(R.id.sensor1BatteryValue);
        sensor2BatteryValueTextView=rootView.findViewById(R.id.sensor2BatteryValue);
        sensor3BatteryValueTextView=rootView.findViewById(R.id.sensor3BatteryValue);
        sensor4BatteryValueTextView=rootView.findViewById(R.id.sensor4BatteryValue);
        //above are the edittext for each sensors
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        getdata();

        return rootView;
    }

    public void getdata() {
        DatabaseReference currentReadingRef = databaseReference.child("message").child("current_reading");
        final String[] PreviousfirstString = new String[1];
        final String[] PrevioussecondString = new String[1];
        final String[] PreviousthirdString = new String[1];
        currentReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);

                String[] strings = value.split("/");
                if (strings.length == 7) {
                    String MQ135 = strings[0].equals("CL")? "Disconnected":strings[0];
                    String MQ135Battery = strings[1].equals("CL")? "Disconnected":strings[1]+"%";
                    String Flame = strings[2].equals("CL")? "Disconnected":strings[2];
                    String FlameBattery = strings[3].equals("CL")? "Disconnected":strings[3]+"%";
                    String HeartRate ;

                    switch (strings[4])
                    {
                        case "CL":
                            HeartRate="Disconnected";
                            break;
                        default:HeartRate=strings[3].equals("N")? "Disconnected":strings[4]+"%";
                        break;
                    }

                    String HeartRateBattery = strings[5].equals("CL")? "Disconnected":strings[5]+"%";
                    MQ135ValueTextView.setText(MQ135);
                    FlameValueTextView.setText(Flame);
                    HeartRateValueTextView.setText(HeartRate);
                    sensor4ValueTextView.setText(value);
                    sensor1BatteryValueTextView.setText(MQ135Battery);
                    sensor2BatteryValueTextView.setText(FlameBattery);
                    sensor3BatteryValueTextView.setText(HeartRateBattery);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
