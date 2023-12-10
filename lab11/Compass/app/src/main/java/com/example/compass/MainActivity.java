package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private SensorManager  sensorManager;
    private TextView accelerationTextView, maxAccelerationTextView;
    private float currentAcceleration = 0, maxAcceleration = 0;
    private ImageView compassImage;
    private Sensor magnetometer, accelerometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false, lastMagnetometerSet = false;
    private float[] rotationMatrix = new float[9], orientation = new float[3];
    private final double calibration = SensorManager.STANDARD_GRAVITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accelerationTextView = (TextView) findViewById(R.id.acceleration);
        maxAccelerationTextView = (TextView) findViewById(R.id.maxAcceleration);

        compassImage = (ImageView) findViewById(R.id.compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(sensorEventListener,  magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener,  accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Timer updateTimer = new Timer("gForceUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGui();
            }

        },0,100);

    }
    @Override
    protected void onPause(){
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void updateGui() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String currentG = currentAcceleration/SensorManager.STANDARD_GRAVITY
                        + "Gs";
                accelerationTextView.setText(currentG);
                accelerationTextView.invalidate();
                String maxG = maxAcceleration/SensorManager.STANDARD_GRAVITY + "Gs";
                maxAccelerationTextView.setText(maxG);
                maxAccelerationTextView.invalidate();
            }
        });
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
        @Override
        public void onSensorChanged(SensorEvent event) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            double a = Math.round(Math.sqrt(Math.pow(x,2)+
                    Math.pow(y, 2)+
                    Math.pow(z, 2)));
            currentAcceleration = Math.abs((float) (a - calibration));
            if(currentAcceleration > maxAcceleration)
                maxAcceleration = currentAcceleration;
            if (event.sensor == magnetometer){
                System.arraycopy(event.values, 0 ,lastMagnetometer, 0 ,event.values.length);
                lastMagnetometerSet = true;
            } else if (event.sensor == accelerometer){
                System.arraycopy(event.values, 0 ,lastAccelerometer, 0 ,event.values.length);
                lastAccelerometerSet = true;
            }

            if(lastAccelerometerSet && lastMagnetometerSet) {
                SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
                SensorManager.getOrientation(rotationMatrix, orientation);

                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) +360) % 360;

                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
                Matrix matrix = new Matrix();
                matrix.postRotate(-azimuthInDegrees);
                Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0 ,0, originalBitmap.getWidth(),
                        originalBitmap.getHeight(), matrix, true);
                compassImage.setImageBitmap(rotatedBitmap);
            }

        }

    };
}