package com.example.random_number_inbetween;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RoundedCounterActivity extends AppCompatActivity {
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_counter);

        messageTextView = findViewById(R.id.messageTextView);

        String message = getIntent().getStringExtra("message");
        int newCounter = getIntent().getIntExtra("counter", 0);
        updateMessageTextView(message+"\nYour New Counter = "+newCounter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(RoundedCounterActivity.this, MainActivity.class); // Specify MainActivity as the target component
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("newCounter", newCounter);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void updateMessageTextView(String message) {
        messageTextView.setText(message);
    }
}

