package com.example.a390project.HomeFragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.a390project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LineChart chart;
    List<Entry> entries;
    float time = 0;
    int xIndex = 0; // To keep track of the x-axis index
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);

        Log.v("Home_page_running","this is running:  "+1);
        chart = rootView.findViewById(R.id.chart);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        entries = new ArrayList<>();

        Log.v("Home_page_running","this is running:  "+1);
        getdata();

        return rootView;
    }
    public void getdata() {
        DatabaseReference currentReadingRef = databaseReference.child("message").child("current_reading");
        currentReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.v("Home_page_running","this is running:  "+1);
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    // Parse value and get time (you need to adjust this based on your data structure)
                    String[] strings = value.split("/");
                    if (strings.length == 7) {
                        String MQ135 = strings[0].equals("N")? "0":strings[0];
                        String MQ135Battery = strings[1].equals("N")? "0":strings[1];
                        String Flame = strings[2].equals("N")? "0":strings[2];
                        String FlameBattery = strings[3].equals("N")? "0":strings[3];
                        String HeartRate = strings[4].equals("N")? "0":strings[4];
                        String HeartRateBattery = strings[5].equals("N")? "0":strings[5];
                        chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value of the y-axis to 0
                        chart.getAxisLeft().setAxisMaximum(1000f); // Set the maximum value of the y-axis to 1000
                        Log.v("Home_page_running","this is running:  "+1);
                        float floatValue = Float.parseFloat(MQ135);
                        if (entries.size() >= 100) {
                            entries.remove(0); // Remove the oldest entry if the list size exceeds 10
                        }
                        // Add the new data entry to the list
                        entries.add(new Entry(time, floatValue));

                        time++;
                        // Create a dataset and update the chart
                        LineDataSet dataSet = new LineDataSet(entries, "Value versus Time");
                        LineData lineData = new LineData(dataSet);
                        chart.setData(lineData);
                        chart.invalidate(); // Refresh the chart
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
