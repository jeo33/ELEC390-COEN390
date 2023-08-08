package com.example.a390project.HomeFragments;

import android.graphics.Color;
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


public class FlamePlot extends DialogFragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LineChart chart;
    List<Entry> entries1;
    List<Entry> entries2;
    List<Entry> entries3;
    List<Entry> entries4;
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
        entries1 = new ArrayList<>();
        entries2 = new ArrayList<>();
        entries3 = new ArrayList<>();
        entries4 = new ArrayList<>();

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
                        String HeartRate = strings[12].equals("N")? "0":strings[12];
                        HeartRate = strings[12].equals("CL")? "0":strings[12];
                        String HeartRateBattery = strings[13].equals("N")? "0":strings[13];
                        HeartRateBattery = strings[13].equals("CL")? "0":strings[13];
                        String HeartRatePlugIn = strings[14].equals("N")? "0":strings[14];
                        HeartRatePlugIn = strings[14].equals("CL")? "0":strings[14];
                        String Mode = strings[15].equals("V")? "Void":strings[15];
                        String latitudeString = strings[16];
                        String longitudeString = strings[17];
                        String Counter = strings[18].equals("N")? "0":strings[18];
                        chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value of the y-axis to 0
                        chart.getAxisLeft().setAxisMaximum(2.0f); // Set the maximum value of the y-axis to 1000
                        Log.v("Home_page_running","this is running:  "+1);
                        float floatValue1 = (Flame.charAt(0)=='1')?0:1;
                        float floatValue2 = (Flame.charAt(1)=='1')?0:1;
                        float floatValue3 = (Flame.charAt(2)=='1')?0:1;
                        float floatValue4 = (Flame.charAt(3)=='1')?0:1;
                        if (entries1.size() >= 15) {
                            entries1.remove(0); // Remove the oldest entry if the list size exceeds 10
                        }
                        if (entries2.size() >= 15) {
                            entries2.remove(0); // Remove the oldest entry if the list size exceeds 10
                        }
                        if (entries3.size() >= 15) {
                            entries3.remove(0); // Remove the oldest entry if the list size exceeds 10
                        }
                        if (entries4.size() >= 15) {
                            entries4.remove(0); // Remove the oldest entry if the list size exceeds 10
                        }
                        // Add the new data entry to the list
                        entries1.add(new Entry(time, floatValue1));
                        entries2.add(new Entry(time, floatValue2));
                        entries3.add(new Entry(time, floatValue3));
                        entries4.add(new Entry(time, floatValue4));

                        time++;
                        LineDataSet flameDataSet = new LineDataSet(entries1, "Front");
                        flameDataSet.setColor(Color.RED); // Set color for the data points
                        flameDataSet.setDrawValues(false); // Disable drawing values on data points
                        flameDataSet.setLineWidth(2f); // Set line width
                        flameDataSet.setCircleColor(Color.RED); // Set circle color for data points
                        flameDataSet.setDrawCircleHole(false); // Disable circle hole

                        LineDataSet mq135DataSet = new LineDataSet(entries2, "Back");
                        mq135DataSet.setColor(Color.BLUE);
                        mq135DataSet.setDrawValues(false);
                        mq135DataSet.setLineWidth(2f);
                        mq135DataSet.setCircleColor(Color.BLUE);
                        mq135DataSet.setDrawCircleHole(false);

                        LineDataSet mq135BatteryDataSet = new LineDataSet(entries3, "Left");
                        mq135BatteryDataSet.setColor(Color.GREEN);
                        mq135BatteryDataSet.setDrawValues(false);
                        mq135BatteryDataSet.setLineWidth(2f);
                        mq135BatteryDataSet.setCircleColor(Color.GREEN);
                        mq135BatteryDataSet.setDrawCircleHole(false);

                        LineDataSet flameBatteryDataSet = new LineDataSet(entries4, "Right");
                        flameBatteryDataSet.setColor(Color.YELLOW);
                        flameBatteryDataSet.setDrawValues(false);
                        flameBatteryDataSet.setLineWidth(2f);
                        flameBatteryDataSet.setCircleColor(Color.YELLOW);
                        flameBatteryDataSet.setDrawCircleHole(false);

// Create a LineData object and add data sets to it
                        LineData lineData = new LineData(flameDataSet, mq135DataSet, mq135BatteryDataSet, flameBatteryDataSet);

// Set data to the chart
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