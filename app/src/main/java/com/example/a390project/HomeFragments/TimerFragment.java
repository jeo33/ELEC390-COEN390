package com.example.a390project.HomeFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a390project.R;
import com.example.a390project.view.RegisterActivity;
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

public class TimerFragment extends Fragment {
    int thresholdValue=3002; ; // Set your threshold value here
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int Reading;
    TextView timer1,timer2,lastRecord;
    int counter=0;
    SharedPreferences sharedPreferences;
    LineChart chart;
    List<Entry> entries;
    float time = 0;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        chart = rootView.findViewById(R.id.chart1);
        entries = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        lastRecord=rootView.findViewById(R.id.lastTime);
        timer1=rootView.findViewById(R.id.timer1);
        timer2=rootView.findViewById(R.id.timer2);
        Button setThresholdBtn = rootView.findViewById(R.id.setThresholdBtn);
        Button resetThresholdBtn = rootView.findViewById(R.id.resetThresholdBtn);
        EditText thresholdEditText = rootView.findViewById(R.id.thresholdEditText);
        setThresholdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = thresholdEditText.getText().toString();
                if (!input.isEmpty()) {
                    thresholdValue = Integer.parseInt(input);
                }
            }
        });

        resetThresholdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastRecord.setText("Last record:"+counter+"s");
                counter=0;
                thresholdValue=9999;
            }
        });

        getdata_inTimer();

        return rootView;
    }


    public void getdata_inTimer() {
        DatabaseReference currentReadingRef = databaseReference.child("message").child("current_reading");
        Log.v("SENSORiNFO","RUNNING INSIDE");
        currentReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                String[] strings = value.split("/");
                if (strings.length == 16) {
                    String year= strings[0];
                    String month= strings[1];
                    String date= strings[2];
                    String hr= strings[3];
                    String minute= strings[4];
                    String second= strings[5];
                    String MQ135 = (strings[6].equals("N")|strings[6].equals("CL"))? "0":strings[6];
                    String MQ135Battery = (strings[7].equals("N")|strings[7].equals("CL"))? "0":strings[7];
                    String MQ135PlugIn =(strings[8].equals("N")|strings[8].equals("CL"))? "0":strings[8];
                    String Flame = (strings[9].equals("N")|strings[9].equals("CL"))? "0000":strings[9];
                    String FlameBattery = (strings[10].equals("N")|strings[10].equals("CL"))? "0":strings[10];
                    String FlamePlugIn = (strings[11].equals("N")|strings[11].equals("CL"))? "0":strings[11];
                    String HeartRate = (strings[12].equals("N")|strings[12].equals("CL"))? "0":strings[12];
                    String HeartRateBattery = (strings[13].equals("N")|strings[13].equals("CL"))? "0":strings[13];
                    String HeartRatePlugIn = (strings[14].equals("N")|strings[14].equals("CL"))? "0":strings[14];
                    String Counter = strings[15].equals("N")? "0":strings[15];
                    timer1.setText(year+"/"+month+"/"+date+"/"+hr+":"+minute+":"+second);
                    Reading=Integer.parseInt(MQ135);
                    if(thresholdValue< Reading)counter=counter+1;
                    //timer2.setText("threshold:  "+thresholdValue+"  reading  "+Reading+"counter: "+counter);
                    timer2.setText("Time(s): "+counter);
                    chart.getAxisLeft().setAxisMinimum(0f); // Set the minimum value of the y-axis to 0
                    chart.getAxisLeft().setAxisMaximum(1000f); // Set the maximum value of the y-axis to 1000
                    float floatValue = Float.parseFloat(MQ135);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

