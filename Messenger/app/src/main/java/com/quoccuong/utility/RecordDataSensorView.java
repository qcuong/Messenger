package com.quoccuong.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.quoccuong.data.Data;
import com.quoccuong.model.PointOfTime;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/24/2015.
 */
public class RecordDataSensorView extends View {
    private Context mContext;
    private ArrayList<PointOfTime> mData = new ArrayList<>();
    private Paint paint;
    private Paint paint_g;
    private Paint paint_x;
    private Paint paint_y;
    private Paint paint_z;

    private int width;
    private int height;

    int acc_Hight = 400;
    int left = 0;
    int right;
    int acc_k = 15;

    int gyr_Hight = 1150;
    int gyr_k = 20;

    public RecordDataSensorView(Context context) {
        super(context);
        this.mContext = context;
        setBackgroundColor(Color.TRANSPARENT);
        init();
    }

    //
    public RecordDataSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setBackgroundColor(Color.TRANSPARENT);
        init();
    }

    public RecordDataSensorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setBackgroundColor(Color.TRANSPARENT);
        init();
    }

    public RecordDataSensorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        setBackgroundColor(Color.TRANSPARENT);
        init();
    }


    public void setData(ArrayList<PointOfTime> data) {
        this.mData = data;
    }

    public void init() {

//        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
//        width = displayMetrics.widthPixels;
//        height = displayMetrics.heightPixels;

        width = getWidth();
        height = getHeight();


        paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Data.typeface);

        paint_g = new Paint();
        paint_g.setStrokeWidth(2);
        paint_g.setColor(Color.GRAY);

        paint_x = new Paint();
        paint_x.setStrokeWidth(2);
        paint_x.setColor(Color.RED);

        paint_y = new Paint();
        paint_y.setStrokeWidth(2);
        paint_y.setColor(Color.BLACK);

        paint_z = new Paint();
        paint_z.setStrokeWidth(2);
        paint_z.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        acc_Hight = height / 4;
        gyr_Hight = 3 * acc_Hight;

        right = width;

        canvas.drawLine(left, acc_Hight, width, acc_Hight, paint_g);

        canvas.drawLine(left, gyr_Hight, width, gyr_Hight, paint_g);

        int i = 0;
        if (mData.size() > (right - left) / 2) {
            i = mData.size() - (right - left) / 2;
        }

        for (int j = 0; i < mData.size() - 1; i++, j = j + 2) {
            PointOfTime dataPoint = mData.get(i);
            PointOfTime dataNextPoint = mData.get(i + 1);
            canvas.drawLine(left + j, (acc_Hight - dataPoint.getAccX() * acc_k), left + j + 2, (acc_Hight - dataNextPoint.getAccX() * acc_k), paint_x);
            canvas.drawLine(left + j, (acc_Hight - dataPoint.getAccY() * acc_k), left + j + 2, (acc_Hight - dataNextPoint.getAccY() * acc_k), paint_y);
            canvas.drawLine(left + j, (acc_Hight - dataPoint.getAccZ() * acc_k), left + j + 2, (acc_Hight - dataNextPoint.getAccZ() * acc_k), paint_z);

            canvas.drawLine(left + j, (gyr_Hight - dataPoint.getGyrX() * gyr_k), left + j + 2, (gyr_Hight - dataNextPoint.getGyrX() * gyr_k), paint_x);
            canvas.drawLine(left + j, (gyr_Hight - dataPoint.getGyrY() * gyr_k), left + j + 2, (gyr_Hight - dataNextPoint.getGyrY() * gyr_k), paint_y);
            canvas.drawLine(left + j, (gyr_Hight - dataPoint.getGyrZ() * gyr_k), left + j + 2, (gyr_Hight - dataNextPoint.getGyrZ() * gyr_k), paint_z);
        }
    }

    public void upDateView() {
        this.postInvalidate();
    }

    public void setAccK(int accK) {
        this.acc_k = accK;
    }

    public void setGyrK(int gyrK) {
        this.gyr_k = gyrK;
    }
}
