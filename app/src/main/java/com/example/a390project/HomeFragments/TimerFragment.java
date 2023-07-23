package com.example.a390project.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a390project.R;

import java.util.ArrayList;


public class TimerFragment extends Fragment {


    private TextView timerTextView;
    private ImageButton startButton;
    private ImageButton stopButton;
    private Button resetButton;
    private Button recordButton;
    private ListView timerRecordsListView;
    private TextView noRecordsTextView;

    private boolean isTimerRunning = false;
    private long startTime;
    private long elapsedTime;

    private ArrayList<Long> timerRecordsList;
    private ArrayAdapter<String> timerRecordsAdapter;
    private int recordIndex = 1;

    private Handler handler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime;
            updateTimerText(elapsedTime);
            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        timerTextView = rootView.findViewById(R.id.timerTextView);
        startButton = rootView.findViewById(R.id.startButton);
        stopButton = rootView.findViewById(R.id.stopButton);
        resetButton = rootView.findViewById(R.id.resetButton);
        timerRecordsListView = rootView.findViewById(R.id.timerRecordsListView);
        noRecordsTextView = rootView.findViewById(R.id.noRecordsTextView);

        timerRecordsList = new ArrayList<>();
        timerRecordsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        timerRecordsListView.setAdapter(timerRecordsAdapter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        recordButton = rootView.findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTimer();
            }
        });

        return rootView;
    }

    private void recordTimer() {
        timerRecordsList.add(elapsedTime);
        timerRecordsAdapter.add("Record " + recordIndex + ": " + formatElapsedTime(elapsedTime));
        recordIndex++;
        showTimerRecords();
    }

    private void startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            startTime = SystemClock.elapsedRealtime() - elapsedTime;
            handler.post(timerRunnable);
        }
    }

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            handler.removeCallbacks(timerRunnable);
            timerRecordsList.add(elapsedTime);
            timerRecordsAdapter.add("Record " + recordIndex + ": " + formatElapsedTime(elapsedTime));
            recordIndex++;
            showTimerRecords();
        }
    }

    private void resetTimer() {
        stopTimer();
        elapsedTime = 0;
        updateTimerText(elapsedTime);
        timerRecordsAdapter.clear();
        recordIndex = 1;
        showTimerRecords();
    }

    private void updateTimerText(long timeInMilliseconds) {
        int seconds = (int) (timeInMilliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        String timeText = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeText);
    }

    private String formatElapsedTime(long timeInMilliseconds) {
        int seconds = (int) (timeInMilliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void showTimerRecords() {
        if (timerRecordsAdapter.getCount() == 0) {
            timerRecordsListView.setVisibility(View.GONE);
            noRecordsTextView.setVisibility(View.VISIBLE);
        } else {
            timerRecordsListView.setVisibility(View.VISIBLE);
            noRecordsTextView.setVisibility(View.GONE);
        }
    }
}