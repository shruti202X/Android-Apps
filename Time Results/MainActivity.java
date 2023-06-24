package com.example.pomodoro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementProgressBar();
            }
        });
    }

    private void incrementProgressBar() {
        int progress = progressBar.getProgress();
        int maxProgress = progressBar.getMax();

        if (progress < maxProgress) {
            progress += 10;
            progressBar.setProgress(progress);
        }
    }
}
