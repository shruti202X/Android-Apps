package com.example.random_number_inbetween;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "counter_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE_ROUND_TO_TEN = 1;
    private static final int REQUEST_CODE_NEXT_IN_HUNDRED = 2;

    private int counter = 0;
    private TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        counterTextView = findViewById(R.id.counterTextView);
        updateCounterTextView();

        Button button = findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCounter();
                displayNotificationWithActions();
            }
        });
    }

    private void increaseCounter() {
        counter++;
        updateCounterTextView();
    }

    private void updateCounterTextView() {
        counterTextView.setText(String.valueOf(counter));
    }

    private void displayNotificationWithActions() {
        Intent roundToTenIntent = new Intent(this, RoundedCounterActivity.class);
        roundToTenIntent.putExtra("counter", roundToNearestTen(counter));
        roundToTenIntent.putExtra("message", "Thank you for rounding to nearest 10");
        PendingIntent roundToTenPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_ROUND_TO_TEN, roundToTenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextInHundredIntent = new Intent(this, RoundedCounterActivity.class);
        nextInHundredIntent.putExtra("counter", getNextInHundred(counter));
        nextInHundredIntent.putExtra("message", "Thank you for rounding to next 100");
        PendingIntent nextInHundredPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NEXT_IN_HUNDRED, nextInHundredIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notificationContent = "Counter: " + counter;
        String roundToTenActionLabel = "Round to nearest 10";
        String nextInHundredActionLabel = "Next in hundred";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.break_notif)
                .setContentTitle("Notification")
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(0, roundToTenActionLabel, roundToTenPendingIntent)
                .addAction(0, nextInHundredActionLabel, nextInHundredPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private int roundToNearestTen(int number) {
        return Math.round(number / 10.0f) * 10;
    }

    private int getNextInHundred(int number) {
        return ((number / 100) + 1) * 100;
    }

    private void createNotificationChannel() {
        // Create the notification channel for devices running Android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Counter Notifications";
            String description = "Notification channel for counter app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (!notificationManager.areNotificationsEnabled()) {
                // Request notification permission
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==REQUEST_CODE_ROUND_TO_TEN || requestCode==REQUEST_CODE_NEXT_IN_HUNDRED) && resultCode == RESULT_OK) {
            int newCounter = data.getIntExtra("newCounter", 0);
            counter = newCounter;
            updateCounterTextView();
        }
    }
}
