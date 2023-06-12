import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ExmpleMainActivityCamera2 extends AppCompatActivity {

    private static final String TAG = "CameraExample";
    private static final int CAMERA_REQUEST_CODE = 1001;
    private CameraDevice cameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            // Permission already granted, start camera setup
            openCamera();
        }
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Assuming first camera
            cameraManager.openCamera(cameraId, cameraCallback, null);
          
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback cameraCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            // Camera is successfully opened, you can start using it
            Log.d(TAG, "Camera opened");
            // Create a capture session and configure it
            createCaptureSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
            cameraDevice = null;
            Log.d(TAG, "Camera disconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
            Log.e(TAG, "Camera error: " + error);
        }
    };
  
    private void createCaptureSession() {
        SurfaceView cameraPreviewSurfaceView = findViewById(R.id.cameraPreviewSurface);
        SurfaceHolder surfaceHolder = cameraPreviewSurfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    // Create a capture session with the camera device
                    List<Surface> outputSurfaces = new ArrayList<>();
                    outputSurfaces.add(holder.getSurface());
                    cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            // Capture session is successfully configured
                            // You can start capturing or streaming images
                            Log.d(TAG, "Capture session configured");
                            // Start capturing or streaming images here
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            // Failed to configure capture session
                            Log.e(TAG, "Failed to configure capture session");
                        }
                    }, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                // Surface size or format changed
                // Handle the changes if needed
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                // Surface destroyed
                // Clean up resources if needed
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
}  
  /*
  In activity_main.xml:
  
  <FrameLayout
      android:id="@+id/cameraPreviewContainer"
      android:layout_width="match_parent"
      android:layout_height="match_parent">
      <SurfaceView
          android:id="@+id/cameraPreviewSurface"
          android:layout_width="match_parent"
          android:layout_height="match_parent" />
  </FrameLayout>

  
  */
