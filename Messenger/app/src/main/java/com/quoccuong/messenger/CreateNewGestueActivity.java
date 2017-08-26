package com.quoccuong.messenger;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.quoccuong.data.Data;
import com.quoccuong.model.MyPair;
import com.quoccuong.model.PointOfTime;
import com.quoccuong.utility.KSpinnerAdaper;
import com.quoccuong.utility.RecordDataSensorView;
import com.quoccuong.utility.SaveGestureData;

import java.util.ArrayList;


public class CreateNewGestueActivity extends Activity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorGyrscope;
    private Context mContext;

    private float acc_x, acc_y, acc_z;
    private float gyr_x, gyr_y, gyr_z;

    double g = 2 * 9.80665;
    double acc_g = g / 15;

    double a = 20;
    double gyr_a = a / 15;
    private Button btnSave;
    private Button btnReset;
    private Button btnCancel;

    private ImageView imageSave;
    private ImageView imageReset;
    private ImageView imageCancel;

    private Spinner spAccK;
    private Spinner spGyrK;

    private TextView tvAcc;
    private TextView tvGyr;

    private EditText edCharactor;
    private boolean isStop = true;
    private boolean isReady = false;

    ArrayList<PointOfTime> data = new ArrayList<>();
    private RecordDataSensorView record;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_gestue);
        mContext = this;

        Bundle bundle = getIntent().getBundleExtra("msg");
        String msg = bundle.getString("msg");

        if (msg.compareTo("add") == 0) {
            position = bundle.getInt("position");
        }

        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);

        sensorGyrscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensorGyrscope, SensorManager.SENSOR_DELAY_FASTEST);


        record = (RecordDataSensorView) findViewById(R.id.activity_create_new_gesture_record);
        record.setData(data);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        imageCancel = (ImageView) findViewById(R.id.imageCancel);
        imageReset = (ImageView) findViewById(R.id.imgReset);
        imageSave = (ImageView) findViewById(R.id.imageSave);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imageCancel.setOnClickListener(this);
        imageReset.setOnClickListener(this);
        imageSave.setOnClickListener(this);

        spAccK = (Spinner) findViewById(R.id.spiner_acc_k);
        spGyrK = (Spinner) findViewById(R.id.spiner_gyr_k);

        final ArrayList<Integer> listK = new ArrayList<>();
        listK.add(5);
        listK.add(10);
        listK.add(15);
        listK.add(20);
        listK.add(25);
        listK.add(30);
        KSpinnerAdaper acc_adapter = new KSpinnerAdaper(mContext, listK);
        KSpinnerAdaper gyr_adapter = new KSpinnerAdaper(mContext, listK);

        spAccK.setAdapter(acc_adapter);
        spGyrK.setAdapter(gyr_adapter);

        spAccK.setSelection(2);
        spGyrK.setSelection(3);

        spAccK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                record.setAccK(listK.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGyrK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                record.setGyrK(listK.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvAcc = (TextView) findViewById(R.id.tv_acc);
        tvGyr = (TextView) findViewById(R.id.tv_gyr);

        tvAcc.setTypeface(Data.typeface);
        tvGyr.setTypeface(Data.typeface);

        edCharactor = (EditText) findViewById(R.id.ed_charactor);
        if (position >= 0) {
            edCharactor.setText(Data.data.get(position).first);
            edCharactor.setEnabled(false);
        }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCancel || v.getId() == R.id.imageCancel) {
            isStop = true;
            isReady = false;
            finish();
        }

        if (v.getId() == R.id.btnReset || v.getId() == R.id.imgReset) {
            if (isReady) {
                isReady = false;
                imageReset.setImageResource(R.drawable.ready);
                imageSave.setImageResource(R.drawable.save2);
                btnReset.setText("READY");
                data.clear();
            } else {
                isReady = true;
                imageReset.setImageResource(R.drawable.reload);
                btnReset.setText("RESET");
                data.clear();
            }
            record.postInvalidate();
            return;
        }

        if (v.getId() == R.id.btnSave || v.getId() == R.id.imageSave) {
            String charactor = edCharactor.getText().toString();
            if (data.isEmpty()){
                return;
            }

            if (position >= 0) {
                Data.data.get(position).second.add(data);
                //finish();
                //return;
            } else {
                boolean c = true;
                for (int i = 0; i < Data.data.size(); i++){
                    if (charactor.equals(Data.data.get(i).first)){
                        position = i;
                        Data.data.get(position).second.add(data);
                        c = false;
                    }
                }

                if (c){
                    ArrayList<ArrayList<PointOfTime>> newData = new ArrayList<>();
                    newData.add(new ArrayList<PointOfTime>(data));
                    MyPair<String, ArrayList<ArrayList<PointOfTime>>> pair = new MyPair<>(charactor, newData);
                    Data.data.add(pair);
                }
            }

            SaveGestureData saveGestureData = new SaveGestureData(mContext);
            saveGestureData.start();

            data.clear();
            isReady = false;
            btnReset.setText("READY");
            imageReset.setImageResource(R.drawable.ready);
            imageSave.setImageResource(R.drawable.save2);
            record.postInvalidate();
            edCharactor.setEnabled(false);

        }
    }

    class RecordDataSensor extends Thread {
        @Override
        public void run() {
            while (!isStop) {
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                }

                record.postInvalidate();
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
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (isReady && isStop) {
                        isStop = false;
                        data.clear();
                        RecordDataSensor recordDataSensor = new RecordDataSensor();
                        recordDataSensor.start();
                        imageSave.setImageResource(R.drawable.save);
                    }
                }
                if (action == KeyEvent.ACTION_UP) {
                    isStop = true;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
