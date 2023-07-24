package com.example.a390project.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.a390project.R;

public class LoadingActivity extends AppCompatActivity {

    private static final int LOADING_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Create a handler to delay the start of the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here (e.g., MainActivity2)
                Intent intent = new Intent(LoadingActivity.this, Login_activity.class);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        }, LOADING_DURATION);
    }
}