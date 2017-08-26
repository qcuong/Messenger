package com.quoccuong.utility;

import android.content.Context;
import android.util.Log;

import com.quoccuong.data.Data;
import com.quoccuong.model.MyPair;
import com.quoccuong.model.PointOfTime;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by sev_user on 7/29/2015.
 */
public class LoadGestureData extends Thread {
    private Context mContext;

    public LoadGestureData(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        try {
            File file = new File(mContext.getFilesDir(), "gesture.data");
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Data.data = (ArrayList<MyPair<String, ArrayList<ArrayList<PointOfTime>>>>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e){
            Data.data = new ArrayList<>();
            Log.e("quoccuong", "load data " + e.toString());
        }

    }
}
