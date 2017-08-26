package com.quoccuong.utility;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.model.PointOfTime;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/29/2015.
 */
public class RecordSensorData extends Thread implements SensorEventListener {
    private ArrayList<PointOfTime> data;
    private Context mContext;


    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorGyrscope;
    private float acc_x, acc_y, acc_z;
    private float gyr_x, gyr_y, gyr_z;

    private double g = 2 * 9.80665;
    private double acc_g = g / 15;

    private double a = 20;
    private double gyr_a = a / 15;
    private boolean isStop = false;

    public RecordSensorData(Context context, ArrayList<PointOfTime> data) {
        this.mContext = context;
        this.data = data;

        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);

        sensorGyrscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensorGyrscope, SensorManager.SENSOR_DELAY_FASTEST);
        isStop = false;
        this.data.clear();
    }

    @Override
    public void run() {

        while (!isStop) {
            if (ConverstationActivity.handler != null) {
                Message msg = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putString("msg", "postInvalidate");
                msg.setData(bundle);
                ConverstationActivity.handler.sendMessage(msg);
            }
            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }

            PointOfTime dataPoint = new PointOfTime();
            if (acc_x > g) {
                dataPoint.setAccX(16);
            } else if (acc_x < -g) {
                dataPoint.setAccX(-16);
            } else {
                dataPoint.setAccX((int) Math.round((acc_x / acc_g)));
            }


            if (acc_y > g) {
                dataPoint.setAccY(16);
            } else if (acc_y < -g) {
                dataPoint.setAccY(-16);
            } else {
                dataPoint.setAccY((int) Math.round((acc_y / acc_g)));
            }

            if (acc_z > g) {
                dataPoint.setAccZ(16);
            } else if (acc_z < -g) {
                dataPoint.setAccZ(-16);
            } else {
                dataPoint.setAccZ((int) Math.round((acc_z / acc_g)));
            }

            if (gyr_x > a) {
                dataPoint.setGyrX(16);
            } else if (gyr_x < -a) {
                dataPoint.setGyrX(-16);
            } else {
                dataPoint.setGyrX((int) Math.round((gyr_x / gyr_a)));
            }

            if (gyr_y > a) {
                dataPoint.setGyrY(16);
            } else if (gyr_y < -a) {
                dataPoint.setGyrY(-16);
            } else {
                dataPoint.setGyrY((int) Math.round((gyr_y / gyr_a)));
            }

            if (gyr_z > a) {
                dataPoint.setGyrZ(16);
            } else if (gyr_z < -a) {
                dataPoint.setGyrZ(-16);
            } else {
                dataPoint.setGyrZ((int) Math.round((gyr_z / gyr_a)));
            }

            data.add(dataPoint);

            int maxx = -17;
            int minx = 17;

            for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                if (data.get(i).getAccX() > maxx) {
                    maxx = data.get(i).getAccX();
                }

                if (data.get(i).getAccX() < minx) {
                    minx = data.get(i).getAccX();
                }
            }

            if (maxx - minx == 1) {
                for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                    data.get(i).setAccX(minx);
                }
            }

            maxx = -17;
            minx = 17;

            for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                if (data.get(i).getAccY() > maxx) {
                    maxx = data.get(i).getAccY();
                }

                if (data.get(i).getAccY() < minx) {
                    minx = data.get(i).getAccY();
                }
            }

            if (maxx - minx == 1) {
                for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                    data.get(i).setAccY(minx);
                }
            }

            maxx = -17;
            minx = 17;

            for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                if (data.get(i).getAccZ() > maxx) {
                    maxx = data.get(i).getAccZ();
                }

                if (data.get(i).getAccZ() < minx) {
                    minx = data.get(i).getAccZ();
                }
            }

            if (maxx - minx == 1) {
                for (int i = data.size() - 1, j = 30; i >= 0 && j > 0; i--, j--) {
                    data.get(i).setAccZ(minx);
                }
            }
        }
        isStop = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acc_x = event.values[0];
            acc_y = event.values[1];
            acc_z = event.values[2];
        }
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyr_x = event.values[0];
            gyr_y = event.values[1];
            gyr_z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
