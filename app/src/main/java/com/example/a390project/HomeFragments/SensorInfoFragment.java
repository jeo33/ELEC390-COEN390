package com.example.a390project.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


    private TextView SensorLabel1;
    private TextView SensorLabel2;
    private TextView SensorLabel3;
    private TextView MQ135ValueTextView;
    private TextView FlameValueTextView;
    private TextView HeartRateValueTextView;
    private TextView sensor1BatteryValueTextView;
    private TextView sensor2BatteryValueTextView;
    private TextView sensor3BatteryValueTextView;
    private TextView sensor1PlugIn;
    private TextView sensor2PlugIn;
    private TextView sensor3PlugIn;
    private ImageView Battery_CONNECTION1,Battery_CONNECTION2,Battery_CONNECTION3;
    private ImageButton sensor1Button,sensor2Button,sensor3Button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_info, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Log.v("SENSORiNFO","RUNNING");
        LinearLayout firstIncludedLayout = rootView.findViewById(R.id.included_layout_1);
        Battery_CONNECTION1 =firstIncludedLayout.findViewById(R.id.Battery);
        SensorLabel1=firstIncludedLayout.findViewById(R.id.sensorLabel1);
        MQ135ValueTextView = firstIncludedLayout.findViewById(R.id.sensor1Value);
        sensor1BatteryValueTextView = firstIncludedLayout.findViewById(R.id.sensor1BatteryValue);
        sensor1PlugIn= firstIncludedLayout.findViewById(R.id.sensor1PlugIn);

        Log.v("SENSORiNFO","RUNNING");
        LinearLayout SecondIncludedLayout = rootView.findViewById(R.id.included_layout_2);
        Battery_CONNECTION2 =SecondIncludedLayout.findViewById(R.id.Battery);
        SensorLabel2=SecondIncludedLayout.findViewById(R.id.sensorLabel1);
        FlameValueTextView = SecondIncludedLayout.findViewById(R.id.sensor1Value);
        sensor2BatteryValueTextView = SecondIncludedLayout.findViewById(R.id.sensor1BatteryValue);
        sensor2PlugIn= SecondIncludedLayout.findViewById(R.id.sensor1PlugIn);

        Log.v("SENSORiNFO","RUNNING");
        LinearLayout ThirdIncludedLayout = rootView.findViewById(R.id.included_layout_3);
        Battery_CONNECTION3 =ThirdIncludedLayout.findViewById(R.id.Battery);
        SensorLabel3=ThirdIncludedLayout.findViewById(R.id.sensorLabel1);
        HeartRateValueTextView = ThirdIncludedLayout.findViewById(R.id.sensor1Value);
        sensor3BatteryValueTextView = ThirdIncludedLayout.findViewById(R.id.sensor1BatteryValue);
        sensor3PlugIn= ThirdIncludedLayout.findViewById(R.id.sensor1PlugIn);
        //above are the edittext for each sensors

        sensor1Button=firstIncludedLayout.findViewById(R.id.detail);
        sensor2Button=SecondIncludedLayout.findViewById(R.id.detail);
        sensor3Button=ThirdIncludedLayout.findViewById(R.id.detail);
        sensor1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlotPopup();
            }
        });


        sensor2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlotPopup2();
            }
        });

        sensor3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlotPopup1();
            }
        });


        SensorLabel1.setText("MQ135");
        SensorLabel2.setText("Flame Sensor");
        SensorLabel3.setText("HeartRate");
        Log.v("SENSORiNFO","RUNNING");
        getdata();

        Log.v("SENSORiNFO","RUNNING,AFTER DATA");
        return rootView;
    }
    private void showPlotPopup() {
        // Create and show the PlotFragment as a popup dialog
        GasPlot plotFragment = new GasPlot();
        plotFragment.show(getChildFragmentManager(), "my_dialog");
    }

    private void showPlotPopup1() {
        // Create and show the PlotFragment as a popup dialog
        HeartRatePlot plotFragment = new HeartRatePlot();
        plotFragment.show(getChildFragmentManager(), "my_dialog");
    }

    private void showPlotPopup2() {
        // Create and show the PlotFragment as a popup dialog
        FlamePlot plotFragment = new FlamePlot();
        plotFragment.show(getChildFragmentManager(), "my_dialog");
    }
    public void getdata() {
        DatabaseReference currentReadingRef = databaseReference.child("message").child("current_reading");

        Log.v("SENSORiNFO","RUNNING INSIDE");
        currentReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                String[] strings = value.split("/");
                if (strings.length == 19) {
                    String year= strings[0];
                    String month= strings[1];
                    String date= strings[2];
                    String hr= strings[3];
                    String minute= strings[4];
                    String second= strings[5];
                    String MQ135 = strings[6].equals("N")? "0":strings[6];
                    MQ135 = (strings[6].equals("CL")|strings[6].equals("w"))? "0":strings[6];
                    String MQ135Battery = strings[7].equals("N")? "0":strings[7];
                    MQ135Battery = strings[7].equals("CL")? "0":strings[7];
                    String MQ135PlugIn = strings[8].equals("N")? "0":strings[8];
                    MQ135PlugIn = strings[8];
                    String Flame = strings[9].equals("N")? "0000":strings[9];
                    Flame = strings[9].equals("CL")? "0000":strings[9];
                    String FlameBattery = strings[10].equals("N")? "0":strings[10];
                    FlameBattery = strings[10].equals("CL")? "0":strings[10];
                    String FlamePlugIn = strings[11].equals("N")? "0":strings[11];
                    FlamePlugIn = strings[11];
                    String HeartRate = (strings[12].equals("N")|strings[12].equals("CL"))? "No Finger Detected":strings[12];
                    String HeartRateBattery = strings[13].equals("N")? "0":strings[13];
                    HeartRateBattery = strings[13].equals("CL")? "0":strings[13];
                    String HeartRatePlugIn = strings[14].equals("N")? "0":strings[14];
                    HeartRatePlugIn = strings[14];
                    String Mode = strings[15].equals("V")? "Void":strings[15];
                    String latitudeString = strings[16];
                    String longitudeString = strings[17];
                    String Counter = strings[18].equals("N")? "0":strings[18];
                    MQ135ValueTextView.setText("Value: "+MQ135);
                    FlameValueTextView.setText("Value(direction): "+Flame);
                    HeartRateValueTextView.setText("Value: "+HeartRate);
                    sensor1BatteryValueTextView.setText("Battery: "+MQ135Battery+"%");
                    sensor2BatteryValueTextView.setText("Battery: "+FlameBattery+"%");
                    sensor3BatteryValueTextView.setText("Battery: "+HeartRateBattery+"%");
                    sensor1PlugIn.setText("PlugIn : "+MQ135PlugIn);
                    sensor2PlugIn.setText("PlugIn : "+FlamePlugIn);
                    sensor3PlugIn.setText("PlugIn : "+HeartRatePlugIn);
                    if(HeartRatePlugIn.equals("CL"))
                    {
                        Battery_CONNECTION3.setImageResource(R.drawable.baseline_signal_wifi_connected_no_internet);
                    } else if (HeartRatePlugIn.equals("1"))
                    {

                        Battery_CONNECTION3.setImageResource(R.drawable.baseline_battery_charging_full_charging);
                    } else {
                        if(Integer.parseInt(HeartRateBattery)<25)
                        {
                            Battery_CONNECTION3.setImageResource(R.drawable.baseline_battery_0_bar_0p);
                        }
                        else if(Integer.parseInt(HeartRateBattery)<75)
                        {

                            Battery_CONNECTION3.setImageResource(R.drawable.baseline_battery_3_bar_50p);
                        }
                        else
                        {

                            Battery_CONNECTION3.setImageResource(R.drawable.baseline_battery_full_24);
                        }
                    }

                    if(FlamePlugIn.equals("CL"))
                    {
                        Battery_CONNECTION2.setImageResource(R.drawable.baseline_signal_wifi_connected_no_internet);
                    } else if (HeartRatePlugIn.equals("1"))
                    {

                        Battery_CONNECTION2.setImageResource(R.drawable.baseline_battery_charging_full_charging);
                    } else {
                        if(Integer.parseInt(FlameBattery)<25)
                        {
                            Battery_CONNECTION2.setImageResource(R.drawable.baseline_battery_0_bar_0p);
                        }
                        else if(Integer.parseInt(FlameBattery)<75)
                        {

                            Battery_CONNECTION2.setImageResource(R.drawable.baseline_battery_3_bar_50p);
                        }
                        else
                        {

                            Battery_CONNECTION2.setImageResource(R.drawable.baseline_battery_full_24);
                        }
                    }

                    if(MQ135PlugIn.equals("CL"))
                    {
                        Battery_CONNECTION1.setImageResource(R.drawable.baseline_signal_wifi_connected_no_internet);
                    } else if (MQ135PlugIn.equals("1"))
                    {

                        Battery_CONNECTION1.setImageResource(R.drawable.baseline_battery_charging_full_charging);
                    } else {
                        if(Integer.parseInt(MQ135Battery)<25)
                        {
                            Battery_CONNECTION1.setImageResource(R.drawable.baseline_battery_0_bar_0p);
                        }
                        else if(Integer.parseInt(MQ135Battery)<75)
                        {

                            Battery_CONNECTION1.setImageResource(R.drawable.baseline_battery_3_bar_50p);
                        }
                        else
                        {

                            Battery_CONNECTION1.setImageResource(R.drawable.baseline_battery_full_24);
                        }
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
