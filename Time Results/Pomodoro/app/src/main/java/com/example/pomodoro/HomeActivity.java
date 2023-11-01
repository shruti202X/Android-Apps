package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button button_new;
    private Button button_resume;
    private Button button_stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button_new = findViewById(R.id.button_new);
        button_resume = findViewById(R.id.button_resume);
        button_stats = findViewById(R.id.button_stats);

        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch Planning Page
                Intent intent = new Intent(HomeActivity.this, PlanActivity.class);
                startActivity(intent);
            }
        });

        button_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch choose page
            }
        });

        button_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch stats page
                Intent intent = new Intent(HomeActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });
    }
}