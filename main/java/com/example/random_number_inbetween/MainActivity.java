package com.example.random_number_inbetween;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;
import android.media.MediaPlayer;

    public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener {

        Button button;
    TextView result;
    EditText rangeStart, rangeEnd;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        result = (TextView) findViewById(R.id.result);
        rangeStart = (EditText) findViewById(R.id.rangeStart);
        rangeEnd = (EditText) findViewById(R.id.rangeEnd);

        //Using setOnFocusChangeListener performs the validation when the field loses focus, which can be helpful for validating the entire input as a whole before taking any action. It prevents constant validation on every keystroke and allows the user to complete their input before the validation occurs.
        rangeEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int rangeEndValue = Integer.parseInt(rangeEnd.getText().toString());
                    int rangeStartValue = Integer.parseInt(rangeStart.getText().toString());

                    if (rangeEndValue < rangeStartValue) {
                        rangeEnd.getBackground().setTint(Color.RED);
                    } else {
                        rangeEnd.getBackground().setTint(Color.GREEN);
                    }
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.chime);
                mediaPlayer.setOnErrorListener(MainActivity.this);

                if (mediaPlayer == null) {
                    Toast.makeText(MainActivity.this, "Error loading audio file.", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity if MediaPlayer creation fails
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.start();

                int rangeEndValue = Integer.parseInt(rangeEnd.getText().toString());
                int rangeStartValue = Integer.parseInt(rangeStart.getText().toString());

                if (rangeEndValue < rangeStartValue) {
                    Toast.makeText(MainActivity.this, "Please enter a larger Ending Value", Toast.LENGTH_SHORT).show();
                } else {
                    int randomNumber = (int) Math.floor(Math.random() * (rangeEndValue - rangeStartValue + 1) + rangeStartValue);
                    result.setText(String.valueOf(randomNumber));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(this, "MediaPlayer error occurred. What: " + i + ", Extra: " + i1, Toast.LENGTH_SHORT).show();
        return false;
    }
}