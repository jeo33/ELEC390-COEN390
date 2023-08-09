package com.example.a390project.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a390project.HomeFragments.GPSFragment;
import com.example.a390project.HomeFragments.HomePage;
import com.example.a390project.HomeFragments.ProfileFragment;
import com.example.a390project.HomeFragments.SensorInfoFragment;
import com.example.a390project.HomeFragments.TimerFragment;
import com.example.a390project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayActivity extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;
    // variable for Text view.
    private TextView retrieveTV;
    BottomNavigationView bottomNavigationView;

    HomePage homePage=new HomePage();
    GPSFragment gpsFragment=new GPSFragment();
    ProfileFragment profileFragment=new ProfileFragment();
    SensorInfoFragment sensorInfoFragment=new SensorInfoFragment();
    TimerFragment timerFragment=new TimerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homePage).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home_page) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homePage).commit();
                } else if (item.getItemId() == R.id.Data_page) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,sensorInfoFragment).commit();
                } else if (item.getItemId() == R.id.Location_page) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,gpsFragment).commit();
                } else if (item.getItemId() == R.id.Profile_page) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profileFragment).commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,timerFragment).commit();
                }
                return true;
            }
        });



/*

        Log.v("displayact","1");
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        Log.v("displayact","2");
        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("message");

        Log.v("displayact","3");
        // initializing our object class variable.
        retrieveTV = findViewById(R.id.idTVRetrieveData);

        // calling method
        // for getting data.
        getdata();
        Log.v("displayact","4");
 */




    }

   /*
    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = snapshot.getValue(String.class);

                Log.v("displayact","5");
                // after getting the value we are setting
                // our value to our text view in below line.
                retrieveTV.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(DisplayActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */



}
