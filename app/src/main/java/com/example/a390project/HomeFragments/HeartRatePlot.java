package com.example.a390project.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class HeartRatePlot extends DialogFragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LineChart chart;
    List<Entry> entries;
    float time = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gas_plot, container, false);
        // Inflate the layout for this fragment

        Log.v("Home_page_running","this is running:  "+1);
        chart = rootView.findViewById(R.id.chart1);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        entries = new ArrayList<>();

        Button closeButton = rootView.findViewById(R.id.closeButton);
        Log.v("Home_page_running","this is running:  "+1);
        getdata();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });
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
                    if (strings.length == 19) {
                        String year= strings[0];
                        String month= strings[1];
                        String date= strings[2];
                        String hr= strings[3];
                        String minute= strings[4];
                        String second= strings[5];
                        String MQ135 = strings[6].equals("N")? "0":strings[6];
                        MQ135 = strings[6].equals("CL")? "0":strings[6];
                        String MQ135Battery = strings[7].equals("N")? "0":strings[7];
                        MQ135Battery = strings[7].equals("CL")? "0":strings[7];
                        String MQ135PlugIn = strings[8].equals("N")? "0":strings[8];
                        MQ135PlugIn = strings[8].equals("CL")? "0":strings[8];
                        String Flame = strings[9].equals("N")? "0000":strings[9];
                        Flame = strings[9].equals("CL")? "0000":strings[9];
                        String FlameBattery = strings[10].equals("N")? "0":strings[10];
                        FlameBattery = strings[10].equals("CL")? "0":strings[10];
                        String FlamePlugIn = strings[11].equals("N")? "0":strings[11];
                        FlamePlugIn = strings[11].equals("CL")? "0":strings[11];
                        String HeartRate = (strings[12].equals("N")|strings[12].equals("CL"))? "0":strings[12];
                        String HeartRateBattery = strings[13].equals("N")? "0":strings[13];
                        HeartRateBattery = strings[13].equals("CL")? "0":strings[13];
                        String HeartRatePlugIn = strings[14].equals("N")? "0":strings[14];
                        HeartRatePlugIn = strings[14].equals("CL")? "0":strings[14];
                        String Mode = strings[15].equals("V")? "Void":strings[15];
                        String latitudeString = strings[16];
                        String longitudeString = strings[17];
                        String Counter = strings[18].equals("N")? "0":strings[18];
                        chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value of the y-axis to 0
                        chart.getAxisLeft().setAxisMaximum(200f); // Set the maximum value of the y-axis to 1000
                        Log.v("Home_page_running","this is running:  "+1);
                        float floatValue = Float.parseFloat(HeartRate);
                        if (entries.size() >= 15) {
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