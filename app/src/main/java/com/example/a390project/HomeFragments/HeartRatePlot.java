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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
        View rootView = inflater.inflate(R.layout.fragment_heart_rateplot, container, false);
        // Inflate the layout for this fragment

        Log.v("Home_page_running","this is running:  "+1);
        chart = rootView.findViewById(R.id.chart1);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        entries = new ArrayList<>();

        Button closeButton = rootView.findViewById(R.id.closeButton);
        Log.v("Home_page_running","this is running:  "+1);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });


        DatabaseReference currentReadingRef = databaseReference.child("message");

        currentReadingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            // ...

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entries = new ArrayList<>();
                time = 0; // Reset time for each new set of data points
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    if (item != null) {
                        // Parse value and get time (you need to adjust this based on your data structure)
                        String[] strings = item.split("/");
                        if (strings.length == 18) {
                            String HeartRate = (strings[12].equals("N")|strings[12].equals("CL"))? "0":strings[12];
                            float floatValue = Float.parseFloat(HeartRate);
                            // Add the new data entry to the list
                            entries.add(new Entry(time, floatValue));
                            time++; // Increment time for the next data point
                        }
                    }
                }
                chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value of the y-axis to 0
                chart.getAxisLeft().setAxisMaximum(1000f); // Set the maximum value of the y-axis to 1000
                LineDataSet dataSet = new LineDataSet(entries, "MQ135 Reading versus Time");
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Adjust line mode if needed
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);

                // Set labels for x and y axes
                chart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Return your formatted time label here based on 'value'
                        return String.valueOf(value);
                    }
                });
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getDescription().setEnabled(false);
                chart.invalidate(); // Refresh the chart
            }

// ...



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });


        return rootView;
    }
}