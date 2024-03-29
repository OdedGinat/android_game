package com.example.tears_dont_fall.Activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.tears_dont_fall.Activities.Interfaces.MovementCallback;

public class SpeedManager {

    // sensor vars
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private MovementCallback movementCallback;

    // vars
    private long timestemp = 0;

    public SpeedManager(Context context, MovementCallback movementCallback) {
        this.movementCallback = movementCallback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                calculateStep(x);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    private void calculateStep(float x) {
        if(System.currentTimeMillis() - timestemp > 200){
            timestemp = System.currentTimeMillis();
            if(x > 3.0){
                if(movementCallback != null)
                    movementCallback.playerMovement(-1);
            }
            if(x < -3.0){
                if(movementCallback != null)
                    movementCallback.playerMovement(1);
            }
        }
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }

}
