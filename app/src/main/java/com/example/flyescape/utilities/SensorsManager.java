package com.example.flyescape.utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.flyescape.interfaces.SensorsCallBack;

public class SensorsManager {

    private SensorManager sensorManager;

    private Sensor sensor;

    private SensorEventListener sensorEventListener;

    private SensorsCallBack sensorsCallBack;

    private long moveTimestamp = 0;

    private long changeSpeedTimestamp = 0;



    public SensorsManager(Context context, SensorsCallBack sensorsCallBack){

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorsCallBack = sensorsCallBack;
        initSensorEventListener();
    }

    private void initSensorEventListener(){
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //not needed
            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - moveTimestamp > 300) {
            moveTimestamp = System.currentTimeMillis();
            if (x > 3.0) {
                if(sensorsCallBack != null)
                    sensorsCallBack.moveFly(-1);
            } else if (x < -3.0) {
                if(sensorsCallBack != null)
                    sensorsCallBack.moveFly(1);
            }
        }
        if (System.currentTimeMillis() - changeSpeedTimestamp > 600) {
            changeSpeedTimestamp = System.currentTimeMillis();
            if (y > 3.0) {
                if(sensorsCallBack != null)
                    sensorsCallBack.changeGameSpeed(-1);
            } else if (y < -3.0)
                if(sensorsCallBack != null)
                    sensorsCallBack.changeGameSpeed(1);
        }
    }

    public void stop(){
        sensorManager.unregisterListener(sensorEventListener,sensor);
    }

    public void start(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
}
