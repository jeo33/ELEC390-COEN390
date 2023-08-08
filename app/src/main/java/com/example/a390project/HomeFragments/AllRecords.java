package com.example.a390project.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.a390project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRecords extends DialogFragment {

    private DatabaseReference databaseReference;
    private ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_records, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        listView = rootView.findViewById(R.id.listView);

        // Query the database based on your index
        DatabaseReference indexRef =  databaseReference.child("message");
        indexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> items = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String item = childSnapshot.getValue(String.class);
                    if (item != null) {
                        // Parse value and get time (you need to adjust this based on your data structure)
                        String[] strings = item.split("/");
                        if (strings.length == 18) {
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
                            String HeartRate = (strings[12].equals("N")|strings[12].equals("CL"))? "No Finger Detected":strings[12];
                            String HeartRateBattery = strings[13].equals("N")? "0":strings[13];
                            HeartRateBattery = strings[13].equals("CL")? "0":strings[13];
                            String HeartRatePlugIn = strings[14].equals("N")? "0":strings[14];
                            HeartRatePlugIn = strings[14].equals("CL")? "0":strings[14];
                            String Mode = strings[15].equals("V")? "Void":strings[15];
                            String latitudeString = strings[16];
                            String longitudeString = strings[17];
                            String new_item=year+"/"+month+"/"+date+"@"+hr+":"+minute+":"+second+"::"+MQ135+"|"+Flame+"|"+HeartRate+"|"+Mode+"|"+latitudeString+"|"+longitudeString;
                            items.add(new_item);
                        }
                    }
                }



                // Create an ArrayAdapter and bind it to the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items
                );
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return rootView;
    }
}
