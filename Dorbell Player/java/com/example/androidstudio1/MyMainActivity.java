package com.example.androidstudio1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MyMainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private Button startButton, pauseButton, stopButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_main);

        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        stopButton = findViewById(R.id.stop_button);
        progressBar = findViewById(R.id.progress_bar);

        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        acquireMediaPlayer();
        initializeUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void acquireMediaPlayer() {
        if (mediaPlayer == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer = MediaPlayer.create(MyMainActivity.this, R.raw.sample_audio);
                    mediaPlayer.setOnErrorListener(MyMainActivity.this);

                    if (mediaPlayer == null) {
                        showToast("Error loading audio file.");
                        finish(); // Close the activity if MediaPlayer creation fails
                    }
                }
            });
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    try {
                        mediaPlayer.start();
                        startUpdatingDuration();
                    } catch (IllegalStateException e) {
                        showToast("Unable to start media playback.");
                    }
                }
                break;
            case R.id.pause_button:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop_button:
                if (mediaPlayer != null) {
                    try {
                        while (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                    } finally {
                        initializeUI();
                        try {
                            mediaPlayer.prepareAsync();
                        } catch (IllegalStateException e) {
                            releaseMediaPlayer();
                            acquireMediaPlayer();
                        }
                    }
                }
                break;
        }
    }

    private void initializeUI() {
        final TextView durationTextView = findViewById(R.id.duration_text_view);
        final int duration = (mediaPlayer != null) ? mediaPlayer.getDuration() : 0;
        progressBar.setProgress(0);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                durationTextView.setText("Duration left: " + duration + " milliseconds");
            }
        });
    }

    private void startUpdatingDuration() {
        final int duration = (mediaPlayer != null) ? mediaPlayer.getDuration() : 0;
        final TextView durationTextView = findViewById(R.id.duration_text_view);

        if(mediaPlayer == null) return;

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int remainingTime = duration;

            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int progress = (int)(((double)currentPosition * 100.0) / (double)duration);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                            remainingTime = duration - currentPosition;
                            if (remainingTime < 1000) {
                                remainingTime = 0;
                                progressBar.setProgress(100);
                            }
                            durationTextView.setText("Duration left: " + remainingTime + " milliseconds");
                        }
                    });

                    handler.postDelayed(this, 1000); // Update every second
                }
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        showToast("MediaPlayer error occurred. What: " + what + ", Extra: " + extra);
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}