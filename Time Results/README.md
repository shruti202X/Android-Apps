# TIme Results

**Time's Result** : What are the results from time

OR

**Time Results** : Analyze results with the dimension of time

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

## References

1. [GeeksForGeeks](https://www.geeksforgeeks.org/how-to-implement-circular-progressbar-in-android/)

The `<rotate>` element in Android XML is used to apply rotation transformations to its child elements. It allows you to rotate a shape, drawable, or view by specifying the starting and ending angles of rotation.

Here are the main attributes used with the `<rotate>` element:

- `android:fromDegrees`: Specifies the starting angle of rotation in degrees. This attribute defines the initial position of the child element.
- `android:toDegrees`: Specifies the ending angle of rotation in degrees. This attribute defines the final position of the child element.
- `android:pivotX` and `android:pivotY`: Optional attributes that specify the pivot point for the rotation. These attributes determine the point around which the rotation occurs. By default, the rotation is performed around the center of the child element.

The rotation angle is measured in degrees, where positive values indicate clockwise rotation and negative values indicate counter-clockwise rotation. The rotation is applied in a circular manner, allowing you to create spinning, flipping, or other rotation effects.

Here's an example usage of the `<rotate>` element:

```xml
<rotate
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromDegrees="0"
    android:toDegrees="45">
  
    <!-- Child element goes here -->
  
</rotate>
```

In the above example, the child element will be rotated from 0 degrees to 45 degrees. You can place any drawable or view inside the `<rotate>` element to apply the rotation effect.

Note that the `<rotate>` element can be combined with other transformation elements, such as `<scale>`, `<translate>`, and `<alpha>`, to achieve complex transformations on the child elements.

2. [CircularProgressIndicator](https://developer.android.com/reference/com/google/android/material/progressindicator/CircularProgressIndicator)

To use the Circular Progress Indicator from the Material Components library in your Android timer app, follow these steps:

1. Add the Material Components library to your project by adding the following dependency to your app-level build.gradle file:

```groovy
implementation 'com.google.android.material:material:1.4.0'
```

2. In your layout XML file (e.g., `activity_timer.xml`), add the Circular Progress Indicator as a child view where you want it to appear. You can customize the size, style, and other attributes as needed. Here's an example:

```xml
<com.google.android.material.progressindicator.CircularProgressIndicator
    android:id="@+id/progress_indicator"
    android:layout_width="200dp"
    android:layout_height="200dp"
    android:progress="0"
    app:indicatorSize="medium"
    app:trackColor="#E0E0E0"
    app:indicatorColor="@color/colorPrimary"
    app:indeterminate="false"
    app:trackCornerRadius="100dp"
    app:trackThickness="10dp"
    app:indicatorDirectionCircular="clockwise"
    app:indicatorInset="10dp"
    app:indicatorRotation="0"
    app:progressTint="@color/colorPrimary"
    app:progressTintMode="src_atop"
    app:showAnimationBehavior="auto"
    app:hideAnimationBehavior="auto" />
```

3. In your Java file (e.g., `TimerActivity.java`), reference the Circular Progress Indicator view and update its progress as needed. Here's an example:

```java
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class TimerActivity extends AppCompatActivity {
    private CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Reference the Circular Progress Indicator view
        progressIndicator = findViewById(R.id.progress_indicator);

        // Update the progress value
        int progress = 50; // Set your desired progress value here
        progressIndicator.setProgressCompat(progress, true);
    }
    // ...
}
```

4. Customize the progress value (`progress`) based on your timer logic to update the Circular Progress Indicator accordingly.

Make sure to customize the attributes and styling of the Circular Progress Indicator to fit your desired design and functionality. Refer to the official documentation of the CircularProgressIndicator class for more information on available attributes and customization options.

Remember to import the necessary classes in your Java file and sync your project after adding the Material Components library dependency.

The `setProgressCompat()` method is a convenient method provided by the CircularProgressIndicator class in the Material Components library. It allows you to set the progress value for the Circular Progress Indicator.

The signature of the `setProgressCompat()` method is as follows:

```java
void setProgressCompat(int progress, boolean animated)
```

- `progress`: The desired progress value to set for the Circular Progress Indicator. It represents the current progress of the task or operation, typically measured as a percentage. The valid range of the progress value is between 0 and the maximum progress value, which is determined by the `setMax()` method or the `app:indicatorMaximum` attribute in the XML.
- `animated`: A boolean value indicating whether to animate the progress change or not. If `true`, the progress change will be animated; if `false`, the progress will be updated immediately without animation.

Other similar methods available in the CircularProgressIndicator class include:

- `setProgress(int progress)`: This method sets the progress value without animation. It is equivalent to `setProgressCompat(progress, false)`.
- `setMax(int max)`: This method sets the maximum progress value that the Circular Progress Indicator can reach.
- `getProgress()`: This method retrieves the current progress value of the Circular Progress Indicator.
- `getMax()`: This method retrieves the maximum progress value of the Circular Progress Indicator.

These methods provide control over the progress value and behavior of the Circular Progress Indicator, allowing you to update the progress in real-time and customize its appearance according to your application's requirements.

---

## Placing Button in the middle of circular progress bar

To position a start and stop button in the middle of the progress bar, you can use different implementations depending on your preferred approach. Here are two possible implementations:

1. Overlaying the Buttons on the Progress Bar:
   - In your layout XML file, you can use a RelativeLayout or ConstraintLayout to position the Circular Progress Indicator and the start/stop buttons.
   - Place the Circular Progress Indicator and the buttons within the same parent layout.
   - Adjust the positioning of the elements using appropriate layout constraints or rules. For example, you can align the buttons to the center of the parent layout and adjust the margins to overlay them on the Circular Progress Indicator.
   - Here's an example XML snippet demonstrating this approach:

```xml
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.progressindicator.CircularProgressIndicator
        android:id="@+id/progress_indicator"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true"
        app:indicatorSize="medium"
        app:trackColor="#E0E0E0"
        app:indicatorColor="@color/colorPrimary"
        app:indeterminate="false"
        app:trackCornerRadius="100dp"
        app:trackThickness="10dp" />

    <Button
        android:id="@+id/start_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Start"
        android:layout_centerInParent="true" />

    <Button
        android:id="@+id/stop_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Stop"
        android:layout_centerInParent="true" />

</RelativeLayout>
```

2. Creating a Custom Progress Bar Component:
   - Create a custom compound component that encapsulates both the Circular Progress Indicator and the start/stop buttons.
   - Define a custom XML layout for your compound component, where you can position the Circular Progress Indicator and buttons using appropriate layout constraints.
   - In your custom compound component class, inflate the custom XML layout and set references to the Circular Progress Indicator and buttons.
   - Expose methods in your custom compound component class to update the progress, handle button clicks, and provide any necessary functionality.
   - Here's an example custom compound component implementation:

```java
public class ProgressBarWithButtons extends RelativeLayout {
    private CircularProgressIndicator progressIndicator;
    private Button startButton;
    private Button stopButton;

    public ProgressBarWithButtons(Context context) {
        super(context);
        init();
    }

    public ProgressBarWithButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressBarWithButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.custom_progress_bar_with_buttons, this, true);

        progressIndicator = findViewById(R.id.progress_indicator);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);

        // Set click listeners for the buttons and handle the logic
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle start button click
            }
        });

        stopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle stop button click
            }
        });
    }

    // Add custom methods to update progress, handle button clicks, etc.
}
```

In this approach, you would create a custom XML layout file (`custom_progress_bar`) for the compound component and define the Circular Progress Indicator, start/stop buttons, and their positioning within the layout.

Choose the approach that suits your requirements and implement the necessary layout and logic to achieve the desired outcome.

---
