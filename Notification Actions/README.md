# Android Notification Guide

This guide provides an overview of the necessary steps to display notifications in an Android application using the `android.app.NotificationManager`, `android.app.NotificationChannel`, `androidx.core.app.NotificationCompat`, and `androidx.core.app.NotificationManagerCompat` classes.

***

## Prerequisites

- Android Studio: The official integrated development environment (IDE) for Android app development.
- Android device or emulator running Android 8.0 (API level 26) or above.

***

## Steps to Display a Notification

1. Create a `NotificationChannel`:
   - Use the `android.app.NotificationChannel` class to define a channel for the notification.
   - Set properties such as the channel ID, name, description, and importance level.
   - Call `createNotificationChannel()` on the `android.app.NotificationManager` instance to create the channel.

2. Build a notification using `NotificationCompat.Builder`:
   - Use the `androidx.core.app.NotificationCompat.Builder` class to build the notification.
   - Set properties like the small icon, content title, and content text.
   - Add additional properties such as large icon, content intent, priority, and actions.

3. Create an instance of `NotificationManagerCompat`:
   - Retrieve an instance of `NotificationManagerCompat` using `NotificationManagerCompat.from(context)`.
   - This ensures compatibility with older versions of Android through the support library.

4. Display the notification:
   - Call `notify(notificationId, notificationBuilder.build())` on the `NotificationManagerCompat` instance to display the notification.
   - Provide a unique `notificationId` to identify the notification.

***

## Example Code

For a complete example, refer to the [NotificationExample.java](NotificationExample.java) file in this repository. It demonstrates the step-by-step process of creating a notification, including creating a notification channel, building the notification, and displaying it.

***

## Some Resources

- [Android Developers Documentation: Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
- [Android Developers Documentation: NotificationCompat.Builder](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder)
- [Android Developers Documentation: NotificationManagerCompat](https://developer.android.com/reference/androidx/core/app/NotificationManagerCompat)
- [Android Developers Documentation: NotificationChannel](https://developer.android.com/reference/android/app/NotificationChannel)
- [Android Developers Documentation: NotificationManager](https://developer.android.com/reference/android/app/NotificationManager)

***

## Accessing default menu buttons

To access the default system buttons like the back button and home button, you can override specific methods in your activity:
-  ### Back Button:
   To handle the back button press, override the onBackPressed() method in your activity:
   ```
   @Override
   public void onBackPressed() {
       // Add your custom logic here
       super.onBackPressed();
   }
   ```
-  ### Home Button:
   The home button is typically used to navigate to the device's home screen, and its behavior cannot be overridden or intercepted by your app. However, you can detect when your app loses focus or goes to the background by overriding the onPause() method in your activity:
   ```
   @Override
   protected void onPause() {
       // App is going to the background, add your custom logic here
       super.onPause();
   }
   ```
Please note that while you can override onBackPressed() and onPause() methods to handle certain scenarios, it is important to follow Android's design guidelines and not modify the default behavior of system buttons unless absolutely necessary.

***

## Some Functions

## NotificationCompat.Builder.addAction

This method is used to add an action button to a notification created using `NotificationCompat.Builder`. An action button allows the user to perform a specific action when the notification is displayed.

### Parameters

- `icon` (int): Resource ID of a drawable that represents the action.
- `title` (CharSequence): The title text for the action button.
- `intent` (PendingIntent): The `PendingIntent` that should be fired when the action button is clicked.

The `intent` parameter specifies the action to be performed when the action button is clicked. It can start an activity, broadcast a message, or invoke a service.

***

## onActivityResult

`onActivityResult` is a callback method that is invoked when an activity launched using `startActivityForResult` exits and returns a result. It is typically used to receive data from child activities or handle the result of an activity.

### Parameters

- `requestCode` (int): An integer code that identifies the original request. It is used to differentiate between multiple requests started by `startActivityForResult`.
- `resultCode` (int): An integer code that represents the result of the activity. It indicates whether the activity completed successfully or there was an error.
- `data` (Intent): An `Intent` object that contains any additional data returned by the child activity.

Developers can override `onActivityResult` in their activity to handle the result and perform any necessary actions based on the returned data.

Note: `onActivityResult` is deprecated in AndroidX starting from version 1.3.0. The recommended approach is to use the `ActivityResultLauncher` API for activity result handling.

***

## ActivityResultLauncher

The `ActivityResultLauncher` interface is used to launch an activity and handle the result of that activity. It provides a simplified way to handle activity results without the need for overriding the `onActivityResult` method.

### Parameters

- `I` (Input type): The type of the input used to launch the activity.
- `O` (Output type): The type of the output received from the activity result.

The `ActivityResultLauncher` interface has the following methods:

- `launch`: Launches the activity with the specified input and registers a callback to receive the result.
- `unregister`: Unregisters the callback for the activity result.

```
ActivityResultLauncher<Intent> launcher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            // Handle the activity result here
        }
    }
);

// Launch the activity using the launcher
Intent intent = new Intent(this, MyActivity.class);
launcher.launch(intent);
```

In this example, we created an `ActivityResultLauncher` using `ActivityResultContracts.StartActivityForResult`, which is a pre-defined contract for launching an activity and receiving its result. We pass a lambda function as the callback, which will be called when the activity result is available. Inside the callback, we can check the result code and retrieve the data from the Intent passed back from the activity.

The launcher object is used to launch the activity by calling its `launch` method and passing the intent. The result will be received in the callback function.

Remember to unregister the launcher when it's no longer needed to avoid memory leaks by calling unregister.

***

## PendingIntent.getActivity

This method returns a `PendingIntent` object that can be used to start an activity when triggered.

### Parameters

- `context` (Context): The context in which the `PendingIntent` will be created.
- `requestCode` (int): A unique code to identify the `PendingIntent`. It is typically used when handling the result of the `PendingIntent`.
- `intent` (Intent): The intent that specifies the activity to be started.
- `flags` (int): Additional flags to customize the behavior of the `PendingIntent`.

***

## Intent constructor

The `Intent` class represents an operation to be performed, such as starting an activity, broadcasting a message, or invoking a service.

### Parameters

- `context` (Context): The context in which the `Intent` is being created.
- `cls` (Class<?>): The class of the component to which the `Intent` should be delivered. It can be an activity, service, broadcast receiver, or content provider.
- `extras` (Bundle): An optional `Bundle` containing additional data for the `Intent`.

***

## Intent.getIntExtra

This method retrieves an integer value from the extras of an `Intent`.

### Parameters

- `name` (String): The name of the desired extra data.
- `defaultValue` (int): The default value to be returned if the extra data with the given name is not found.

### Example
```
int counter = getIntent().getIntExtra("counter", 0);
```

***

## Intent.getStringExtra

The getStringExtra method is used to retrieve a string value from the extras of an Intent.

### Function Definition
```
public String getStringExtra(String name)
```

### Parameters
- `name` (String): The name of the desired extra data.

### Return Value
`String`: The string value associated with the specified name, or null if no mapping exists for the name.

### Example
```
String message = getIntent().getStringExtra("message");
```

***

## `PendingIntent` VS `startActivityForResult()`

`PendingIntent` can be used as an alternative to `startActivityForResult()` in certain scenarios. Both `PendingIntent` and `startActivityForResult()` serve the purpose of starting an activity and receiving a result back from that activity.

`startActivityForResult()` is typically used within the same application to start another activity and expect a result from it. It provides a way to pass data between activities and handle the result in the calling activity's `onActivityResult()` method.

On the other hand, `PendingIntent` is commonly used for inter-process communication, such as triggering an action or starting an activity from a notification, widget, or system event. It allows you to perform an action or start an activity outside the context of the current activity.

While `PendingIntent` can be used to start an activity, it does not provide a direct mechanism for receiving a result back from that activity. It is more suitable for scenarios where you need to trigger an action or launch an activity without expecting a result.

If you require the ability to receive a result from the launched activity, it is recommended to use `startActivityForResult()`. However, if you only need to trigger an action or start an activity without expecting a result, `PendingIntent` can be a suitable alternative.

***

## Activity Stack

In the context of Android, the "activity stack" refers to a data structure that keeps track of the activities in an application. It is also known as the "back stack" or "task stack."

When an activity is started, it is pushed onto the top of the activity stack, and when the user presses the back button or the activity finishes, it is popped from the stack. The activity stack maintains the order in which activities are opened, allowing the user to navigate back through the application's activity hierarchy.

The activity stack operates based on the principle of Last-In-First-Out (LIFO), meaning the most recently opened activity is the one at the top of the stack. This behavior enables the system to manage the navigation flow and ensure a consistent back button behavior for the user.

The activity stack is managed by the Android system, and developers can interact with it through various methods and APIs provided by the Android framework, such as `startActivity()`, `startActivityForResult()`, and `finish()`.

***

## Behaviour of activity stack on notification action button

1. When the app is running with some activity:
   - When a notification action button is clicked while the app is running and has an activity in the foreground, the `PendingIntent` associated with the action button is triggered.
   - If the `PendingIntent` starts an activity, the new activity is added to the top of the activity stack, and the current activity remains in the stack below it.
   - The user can navigate back to the previous activity by pressing the back button or perform other actions within the newly opened activity.

2. When the app is closed:
   - When a notification action button is clicked and the app is closed (not running in the foreground), the `PendingIntent` associated with the action button is triggered.
   - If the `PendingIntent` starts an activity, the activity is launched as a new task, separate from the app's existing activity stack.
   - The activity launched from the notification action button will be placed on top of the existing task stack or, if no task exists, a new task will be created for the activity.
   - The user can navigate within the newly launched activity or use the back button to return to the previous activity or exit the app entirely, depending on the activity's navigation and back stack configuration.

It's important to note that the behavior may vary depending on how the `PendingIntent` is configured and the launch mode of the activities involved. Developers can customize the behavior by specifying different launch modes, flags, or task affinity for the activities and `PendingIntent` configurations.

***

## setResult Function

The `setResult` function is used to set the result that will be returned to the activity that started the current activity when it finishes. It is typically used in conjunction with `startActivityForResult` to pass data or information back to the calling activity.

The `setResult` function takes two parameters:
1. `resultCode`: This indicates the result of the current activity. It can be one of the predefined result codes such as `RESULT_OK`, `RESULT_CANCELED`, or a custom result code defined by the developer.
2. `data`: An optional `Intent` object that can carry additional data or information to be returned to the calling activity. This can be used to pass data such as integers, strings, or complex objects.

Example: `setResult(RESULT_OK, resultIntent)`. This means that when the current activity finishes, it will return a result code of `RESULT_OK` to the calling activity (lets say, `MainActivity`) along with the `resultIntent` containing the extras.

To handle the result in `MainActivity`, you override the `onActivityResult` method and retrieve the result data using the `requestCode` and `resultCode` parameters.

Here's an example of how `setResult` and `onActivityResult` can be used together:

In the called activity:
```
// Create an intent with the data to be returned
Intent resultIntent = new Intent();
resultIntent.putExtra("newCounter", newCounter); // Pass the updated counter value
// Set the result and finish the activity
setResult(RESULT_OK, resultIntent);
finish();
```

In the calling activity (e.g., MainActivity):
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
        // Retrieve the result data
        int newCounter = data.getIntExtra("newCounter", 0);
        // Handle the result as needed
        // ...
    }
}
```

In the above example, when the called activity finishes and calls `setResult`, the result code `RESULT_OK` and the `resultIntent` with the `newCounter` value are returned to `MainActivity`. In `MainActivity`'s `onActivityResult` method, you can retrieve the `newCounter` value and handle it accordingly.

***

## Handling Instances of MainActivity

If you have multiple instances of the MainActivity and the RoundedCounterActivity, it can lead to unexpected behavior when passing data back and forth between them. To ensure that the result is sent to the correct instance of MainActivity, you can specify the launch mode for the MainActivity in the AndroidManifest.xml file.

In your AndroidManifest.xml, add the `android:launchMode` attribute to the MainActivity declaration and set it to `"singleTop"`. Here's an example:

```
<activity
    android:name=".MainActivity"
    android:launchMode="singleTop"
    ... >
    ...
</activity>
```

By setting the launch mode to `"singleTop"`, it ensures that if an instance of MainActivity already exists at the top of the task stack, a new instance will not be created. Instead, the existing instance will be used, and the result will be delivered to it.

With this change, when you press the back button in RoundedCounterActivity, the result will be sent back to the existing instance of MainActivity.

Note: If you want to bring the existing instance of MainActivity to the foreground when the result is delivered, you can use the `Intent.FLAG_ACTIVITY_SINGLE_TOP` flag when creating the result intent in the RoundedCounterActivity, like this:

```
Intent resultIntent = new Intent(RoundedCounterActivity.this, MainActivity.class);
resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
resultIntent.putExtra("newCounter", newCounter);
setResult(RESULT_OK, resultIntent);
finish();
```

This flag ensures that the existing instance of MainActivity is used and brought to the foreground, if necessary, to receive the result.

***
