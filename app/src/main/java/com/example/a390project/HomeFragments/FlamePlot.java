package com.example.a390project.HomeFragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.a390project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
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

public class FlamePlot extends DialogFragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LineChart chart;
    LineChart chart2;
    LineChart chart3;
    LineChart chart4;

    List<Entry> entries1;
    List<Entry> entries2;
    List<Entry> entries3;
    List<Entry> entries4;
    float time = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flame_plot, container, false);

        chart = rootView.findViewById(R.id.chart1);
        chart2 = rootView.findViewById(R.id.chart2);
        chart3 = rootView.findViewById(R.id.chart3);
        chart4 = rootView.findViewById(R.id.chart4);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Button closeButton = rootView.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });

        DatabaseReference currentReadingRef = databaseReference.child("message");

        currentReadingRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entries1 = new ArrayList<>();
                entries2 = new ArrayList<>();
                entries3 = new ArrayList<>();
                entries4 = new ArrayList<>();

                time = 0; // Reset time for each new set of data points
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    if (item != null) {
                        // Parse value and get time (you need to adjust this based on your data structure)
                        String[] strings = item.split("/");
                        if (strings.length == 18) {
                            String Flame = strings[9].equals("N") ? "0000" : strings[9];
                            Flame = strings[9].equals("CL") ? "0000" : strings[9];

                            // Check for garbage output and skip if necessary
                            if (!Flame.contains("�")) {
                                float floatValue1 = (Flame.charAt(0) == '0') ? 0 : 1;
                                float floatValue2 = (Flame.charAt(1) == '0') ? 0 : 1;
                                float floatValue3 = (Flame.charAt(2) == '0') ? 0 : 1;
                                float floatValue4 = (Flame.charAt(3) == '0') ? 0 : 1;

                                // Add the new data entry to the lists
                                entries1.add(new Entry(time, floatValue1));
                                entries2.add(new Entry(time, floatValue2));
                                entries3.add(new Entry(time, floatValue3));
                                entries4.add(new Entry(time, floatValue4));
                                time++; // Increment time for the next data point
                            } else {
                                Log.d("FlamePlot", "Garbage output detected at time: " + time);
                            }
                        }
                    }
                }

                LineDataSet dataSet1 = new LineDataSet(entries1, "Front");
                dataSet1.setColor(Color.RED); // Set color for the line
                dataSet1.setCircleColor(Color.RED); // Set color for the plot dots

                LineDataSet dataSet2 = new LineDataSet(entries2, "Back");
                dataSet2.setColor(Color.BLUE);
                dataSet2.setCircleColor(Color.BLUE);

                LineDataSet dataSet3 = new LineDataSet(entries3, "Left");
                dataSet3.setColor(Color.GREEN);
                dataSet3.setCircleColor(Color.GREEN);

                LineDataSet dataSet4 = new LineDataSet(entries4, "Right");
                dataSet4.setColor(Color.YELLOW);
                dataSet4.setCircleColor(Color.YELLOW);

                // Add LineDataSet instances to LineData
                LineData lineData1 = new LineData(dataSet1);
                LineData lineData2 = new LineData(dataSet2);
                LineData lineData3 = new LineData(dataSet3);
                LineData lineData4 = new LineData(dataSet4);

                // Set other properties of the LineData if needed
                lineData1.setValueTextSize(12f);
                lineData1.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                lineData2.setValueTextSize(12f);
                lineData2.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                lineData3.setValueTextSize(12f);
                lineData3.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                lineData4.setValueTextSize(12f);
                lineData4.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                // Set labels for x and y axes
                chart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        // Return your formatted time label here based on 'value'
                        return String.valueOf(value);
                    }
                });
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                // Set LineData to the chart
                chart.setData(lineData1);

                // Set other properties of the chart if needed
                Description description = new Description();
                description.setText("Flame Plot");
                chart.setDescription(description);

                // Refresh the chart
                chart.invalidate();

                // Apply the same settings to the other charts
                chart2.setData(lineData2);
                chart2.setDescription(description);
                chart2.invalidate();

                chart3.setData(lineData3);
                chart3.setDescription(description);
                chart3.invalidate();

                chart4.setData(lineData4);
                chart4.setDescription(description);
                chart4.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return rootView;
    }
}
