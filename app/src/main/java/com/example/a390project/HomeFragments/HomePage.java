package com.example.a390project.HomeFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    private boolean onDataChangeEnabled = false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView textSensorReading1,textName1,GPSMODE,latitude,longitude,textName2,textName3,textName4;
    ImageView top,bot,left,right,middle;;
    ImageButton centerIcon;
    Button Location,Location2,Location3,Location4;
    String Readings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
Log.v("HOME_RUNNING_TEST","RUNNING");

        LinearLayout firstIncludedLayout = rootView.findViewById(R.id.included_layout_1);
        LinearLayout secondIncludedLayout = rootView.findViewById(R.id.included_layout_2);
        LinearLayout thirdIncludedLayout = rootView.findViewById(R.id.included_layout_3);
        LinearLayout forthIncludedLayout = rootView.findViewById(R.id.included_layout_4);

        textName1 = firstIncludedLayout.findViewById(R.id.text_name);
        textName2 = secondIncludedLayout.findViewById(R.id.text_name);
        textName3 = thirdIncludedLayout.findViewById(R.id.text_name);
        textName4 = forthIncludedLayout.findViewById(R.id.text_name);
        centerIcon=firstIncludedLayout.findViewById(R.id.image_center_icon);
        textName1 = firstIncludedLayout.findViewById(R.id.text_name);
        textSensorReading1 = firstIncludedLayout.findViewById(R.id.text_sensor_reading);
        top= firstIncludedLayout.findViewById(R.id.image_top_icon);
        bot= firstIncludedLayout.findViewById(R.id.image_bottom_icon);
        left= firstIncludedLayout.findViewById(R.id.image_left_icon);
        right= firstIncludedLayout.findViewById(R.id.image_right_icon);
        Location=(Button) firstIncludedLayout.findViewById(R.id.Location);
        Location2=(Button) secondIncludedLayout.findViewById(R.id.Location);
        Location3=(Button) thirdIncludedLayout.findViewById(R.id.Location);
        Location4=(Button) forthIncludedLayout.findViewById(R.id.Location);
        GPSMODE= firstIncludedLayout.findViewById(R.id.GpsMode);
        latitude= firstIncludedLayout.findViewById(R.id.latitude);
        longitude= firstIncludedLayout.findViewById(R.id.longitude);
        Log.v("HOME_RUNNING_TEST","RUNNING");
        textName1.setText("Paul Stanley");
        textName2.setText("Gene Simmons");
        textName3.setText("Eric Singer");
        textName4.setText("Tommy Thayer");
        centerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlotPopupAll();
            }
        });

        getdata();

        Log.v("HOME_RUNNING_TEST","RUNNING");
        Location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putFloat("floatArg2Key", Float.parseFloat("45.424721")); // Replace with your float value
                args.putFloat("floatArg1Key", Float.parseFloat("-75.695000")); // Replace with your float value

                // Create OtherFragment and set arguments
                GPSwithLocationInput gpsPage=new GPSwithLocationInput();
                gpsPage.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, gpsPage) // Replace with your fragment container ID
                        .addToBackStack(null) // Add transaction to the back stack
                        .commit();
            }
        });

        Location3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putFloat("floatArg2Key", Float.parseFloat("43.653226")); // Replace with your float value
                args.putFloat("floatArg1Key", Float.parseFloat("-79.3831843")); // Replace with your float value

                // Create OtherFragment and set arguments
                GPSwithLocationInput gpsPage=new GPSwithLocationInput();
                gpsPage.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, gpsPage) // Replace with your fragment container ID
                        .addToBackStack(null) // Add transaction to the back stack
                        .commit();
            }
        });

        Location4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putFloat("floatArg2Key", Float.parseFloat("49.246292")); // Replace with your float value
                args.putFloat("floatArg1Key", Float.parseFloat("-123.116226")); // Replace with your float value

                // Create OtherFragment and set arguments
                GPSwithLocationInput gpsPage=new GPSwithLocationInput();
                gpsPage.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, gpsPage) // Replace with your fragment container ID
                        .addToBackStack(null) // Add transaction to the back stack
                        .commit();
            }
        });
        return rootView;
    }

    private void showPlotPopupAll() {
        // Create and show the PlotFragment as a popup dialog
        AllRecords plotFragment = new AllRecords();
        plotFragment.show(getChildFragmentManager(), "my_dialog");
    }

    public void getdata() {
        Log.v("HOME_RUNNING_TEST","this is running:  "+1);
        DatabaseReference currentReadingRef = databaseReference.child("message").child("current_reading");

        Log.v("HOME_RUNNING_TEST","this is running:  "+1);
        currentReadingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Log.v("HOME_RUNNING_TEST","this is running:  "+1);
                    String value = snapshot.getValue(String.class);
                    if (value != null) {
                        // Parse value and get time (you need to adjust this based on your data structure)
                        String[] strings = value.split("/");

                        Log.v("HOME_RUNNING_TEST","RUNNING INSIDE");
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
                            String Mode = strings[15].equals("V")? "Last Location":"Current Location";
                            String latitudeString = strings[16];
                            String longitudeString = strings[17];
                            String Counter = strings[18].equals("N")? "0":strings[18];
                            textSensorReading1.setText(year+"/"+month+"/"+date+"/"+hr+":"+minute+":"+second); // Set the new sensor reading for the first sensor.
                            GPSMODE.setText(Mode);
                            latitude.setText(latitudeString);
                            longitude.setText(longitudeString);
                            Location.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle args = new Bundle();
                                    args.putFloat("floatArg1Key", Float.parseFloat(longitudeString)); // Replace with your float value
                                    args.putFloat("floatArg2Key", Float.parseFloat(latitudeString)); // Replace with your float value

                                    // Create OtherFragment and set arguments
                                    GPSwithLocationInput gpsPage=new GPSwithLocationInput();
                                    gpsPage.setArguments(args);

                                    requireActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.frameLayout, gpsPage) // Replace with your fragment container ID
                                            .addToBackStack(null) // Add transaction to the back stack
                                            .commit();
                                }
                            });
                            if(Flame.charAt(0)=='1')
                            {
                                if(Integer.parseInt(Counter)%2==0)top.setImageResource(R.drawable.baseline_local_fire_department_off);
                                else top.setImageResource(R.drawable.baseline_local_fire_department_24);
                            }
                            else top.setImageResource(R.drawable.baseline_local_fire_department_off);

                            if(Flame.charAt(1)=='1')
                            {
                                if(Integer.parseInt(Counter)%2==0)bot.setImageResource(R.drawable.baseline_local_fire_department_off);
                                else bot.setImageResource(R.drawable.baseline_local_fire_department_24);
                            }
                            else bot.setImageResource(R.drawable.baseline_local_fire_department_off);

                            if(Flame.charAt(2)=='1')
                            {
                                if(Integer.parseInt(Counter)%2==0)left.setImageResource(R.drawable.baseline_local_fire_department_off);
                                else left.setImageResource(R.drawable.baseline_local_fire_department_24);
                            }
                            else left.setImageResource(R.drawable.baseline_local_fire_department_off);

                            if(Flame.charAt(3)=='1')
                            {
                                if(Integer.parseInt(Counter)%2==0)right.setImageResource(R.drawable.baseline_local_fire_department_off);
                                else right.setImageResource(R.drawable.baseline_local_fire_department_24);
                            }
                            else right.setImageResource(R.drawable.baseline_local_fire_department_off);


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
