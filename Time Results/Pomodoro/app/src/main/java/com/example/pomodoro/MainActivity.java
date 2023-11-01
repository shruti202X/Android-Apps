package com.example.pomodoro;

//To Do :
//1. Deal with backButton, basically quit / end button functionality
//2. Instead of Stats have like View TimeLine Option
//3. Create Resume Page, which takes you to all Plans
//4. optional - Add first and last Session id's in Plan Database for faster access

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends AppCompatActivity {

    //Initialize Views
    private TextView type;
    private ProgressBar progressBar;
    private TextView time;
    private Button button_start;
    private Button button_memo;
    private Button button_skip;
    private Button button_quit;

    private CountDownTimer countDownTimer;
    private boolean workType;
    private boolean isTimerRunning;
    private long remainingAtSkipped;
    private long timeRemainingMillis;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int sessionNumber;
    private static final int POMODORO_SEC = 25 * 60; // 25 minutes
    private static final int BREAK_SEC = 5 * 60; // 5 minutes
    private static final long SEC = 1000; // 1 minute
    private static final long POMODORO_DURATION = POMODORO_SEC * SEC;
    private static final long BREAK_DURATION = BREAK_SEC * SEC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        type = findViewById(R.id.textView_type);
        progressBar = findViewById(R.id.progressBar);
        time = findViewById(R.id.textView_time);
        button_start = findViewById(R.id.button_start);
        button_memo = findViewById(R.id.button_memo);
        button_skip = findViewById(R.id.button_skip);
        button_quit = findViewById(R.id.button_quit);

        workType = true;
        isTimerRunning = false;
        remainingAtSkipped = 0;
        timeRemainingMillis = POMODORO_DURATION;
        progressBar.setMax(POMODORO_SEC);
        sessionNumber = 1;

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if(isTimerRunning) startTimer();
                        }
                    }
                }
        );

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        button_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                intent.putExtra("SESSION_NUMBER", sessionNumber);
                intent.putExtra("WORK_TYPE", workType);
                if(workType){
                    intent.putExtra("SESSION_POMODORO_TIME", POMODORO_DURATION - timeRemainingMillis);
                    intent.putExtra("SESSION_BREAK_TIME", 0);
                }else{
                    intent.putExtra("SESSION_POMODORO_TIME", POMODORO_DURATION - remainingAtSkipped);
                    intent.putExtra("SESSION_BREAK_TIME", BREAK_DURATION - timeRemainingMillis);
                }
                if(isTimerRunning) countDownTimer.cancel();
                activityResultLauncher.launch(intent);
            }
        });

        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainingAtSkipped = timeRemainingMillis;
                nextTimer();
            }
        });

        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                if(isTimerRunning) countDownTimer.cancel();
                activityResultLauncher.launch(intent);
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Timer finished, handle completion
                // For simplicity, we'll just reset the timer
                nextTimer();
            }
        };

        countDownTimer.start();
        button_start.setText("Pause");
        isTimerRunning = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        button_start.setText("Start");
        isTimerRunning = false;
    }

    private void nextTimer() {
        if(countDownTimer!=null) countDownTimer.cancel();
        if(workType){
            timeRemainingMillis = BREAK_DURATION;
            type.setText("Break Time");
            progressBar.setMax(BREAK_SEC);
        }else{
            timeRemainingMillis = POMODORO_DURATION;
            type.setText("Work Time");
            remainingAtSkipped = 0;
            sessionNumber++;
            progressBar.setMax(POMODORO_SEC);
        }
        workType = !workType;
        updateTimer();
        startTimer();
    }

    private void updateTimer() {
        int totalSeconds = (int) (timeRemainingMillis / 1000);
        int minutes = (int) (totalSeconds) / 60;
        int seconds = (int) (totalSeconds) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        time.setText(timeLeftFormatted);
        //progressBar.setProgress(totalSeconds, true);
        progressBar.setProgress(progressBar.getMax() - totalSeconds, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
