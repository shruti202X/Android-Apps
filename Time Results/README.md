# TIme Results

Time's Result : What are the results from time
OR
Time Results : Analyze results with the dimension of time

---

## Accuracy of `CountDownTImer`

The `CountDownTimer` class in Android provides a convenient way to implement a countdown timer functionality. However, it's important to note that the accuracy of the `CountDownTimer` class is dependent on the underlying system timer.

The accuracy of the `CountDownTimer` class can vary depending on several factors, including the device's hardware, system load, and other running processes. In general, it provides reasonable accuracy for most purposes, such as displaying a countdown in a timer app or triggering an action after a certain duration.

The `CountDownTimer` class uses the `Handler` mechanism to update the countdown at regular intervals. It's worth noting that the actual countdown interval may not be exactly precise due to system overhead and delays. The class tries to compensate for any discrepancies and adjusts the countdown to ensure the elapsed time matches the expected duration as closely as possible.

If you require more precise timing, such as for high-performance applications or critical operations, you may need to consider using alternative mechanisms, such as the `Timer` class or custom implementations using threads and handlers. These alternatives offer more control over the timing but require more careful management and synchronization to ensure accuracy.

In summary, the `CountDownTimer` class is generally accurate enough for most common use cases, but for applications that require precise timing, it's important to consider the limitations and explore alternative approaches if necessary.

---

## Example 1 : Simeple TImer

**Java File (TimerActivity.java):**
```java
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private TextView countdownText;
    private Button startButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownText = findViewById(R.id.countdown_text);
        startButton = findViewById(R.id.start_button);
        resetButton = findViewById(R.id.reset_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("Start");
                startButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();

        timerRunning = true;
        startButton.setText("Pause");
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Start");
        resetButton.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        timeLeftInMillis = 60000; // Change this to your desired timer duration
        updateCountdownText();
        startButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countdownText.setText(timeLeftFormatted);
    }
}
```

**XML Layout File (activity_timer.xml):**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".TimerActivity">

    <TextView
        android:id="@+id/countdown_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="36sp"
        android:text="00:00" />

    <Button
        android:id="@+id/start_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Start" />

    <Button
        android:id="@+id/reset_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Reset"
        android:visibility="invisible" />

</LinearLayout

>
```

**Manifest File (AndroidManifest.xml):**
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.timerapp">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".TimerActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
In this example, the timer is set to one minute (60000 milliseconds) but you can adjust the `timeLeftInMillis` variable to your desired timer duration.

This code sets up a timer with start, pause, and reset functionality. The countdown is displayed in a `TextView` and updates every second using the `CountDownTimer` class.

Remember to add the necessary permissions and dependencies in your app's `AndroidManifest.xml` and `build.gradle` files, respectively.

Make sure to replace the package name (`com.example.timerapp`) with your own package name in the Manifest file if needed.

---

## Example 2 : Added circular timer

To achieve the circular ring-like structure that fills up as the countdown timer progresses, you can use a `ProgressBar` with a custom drawable background. Here's an updated version of the Java file, XML layout file, and manifest file to incorporate the circular progress ring:

Java File (`TimerActivity.java`):
```java
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    private TextView countdownText;
    private Button startButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis;

    private ProgressBar progressBar;
    private int progressMax = 60000; // Change this to your desired timer duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownText = findViewById(R.id.countdown_text);
        startButton = findViewById(R.id.start_button);
        resetButton = findViewById(R.id.reset_button);
        progressBar = findViewById(R.id.progress_bar);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setText("Start");
                startButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                progressBar.setProgress(progressMax);
            }
        }.start();

        timerRunning = true;
        startButton.setText("Pause");
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setText("Start");
        resetButton.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        }

        timeLeftInMillis = progressMax;
        updateCountdownText();
        updateProgressBar();
        startButton.setText("Start");
        startButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        countdownText.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        int progress = (int) ((progressMax - timeLeftInMillis) / 1000);
        progressBar.setProgress(progress);
    }
}
```

XML Layout File (`activity_timer.xml`):
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    tools:context=".TimerActivity">

    <ProgressBar
        android:id="@+id/progress_bar"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:max="60

"
        android:progress="0"
        android:progressDrawable="@drawable/circular_progress_bar"
        android:indeterminate="false"
        android:layout_centerInParent="true"/>

    <TextView
        android:id="@+id/countdown_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/progress_bar"
        android:textSize="24sp"
        android:text="00:00"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="16dp"/>

    <Button
        android:id="@+id/start_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/countdown_text"
        android:text="Start"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="16dp"/>

    <Button
        android:id="@+id/reset_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/start_button"
        android:text="Reset"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="16dp"
        android:visibility="invisible"/>

</RelativeLayout>
```

Drawable Resource File (`circular_progress_bar.xml`):
Create a new XML file called `circular_progress_bar.xml` in the `res/drawable` directory with the following content:
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="100dp" />
    <stroke
        android:width="8dp"
        android:color="@color/progressBarStrokeColor" />
    <solid android:color="@color/progressBarFillColor" />
</shape>
```

Colors Resource File (`colors.xml`):
Add the following colors to your `res/values/colors.xml` file:
```xml
<resources>
    <color name="progressBarStrokeColor">#CCCCCC</color>
    <color name="progressBarFillColor">#FF4081</color>
</resources>
```

Manifest File (`AndroidManifest.xml`):
Ensure that the `TimerActivity` is declared in your `AndroidManifest.xml` file:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.timerapp">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".TimerActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Make sure to replace `com.example.timerapp` with your actual package name.

These updates will add a circular progress bar that fills up as the countdown timer progresses. The progress bar will have a stroke color defined in `progressBarStrokeColor` and a fill color defined in `progressBarFillColor`. Adjust the colors as per your preference in the `colors.xml` file.

Note: Don't forget to include the necessary drawables and colors in your project's resource files.

---


