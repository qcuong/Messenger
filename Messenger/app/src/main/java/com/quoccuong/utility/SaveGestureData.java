package com.quoccuong.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.quoccuong.data.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by sev_user on 7/29/2015.
 */
public class SaveGestureData extends Thread {
    private Context mContext;

    public SaveGestureData(Context context) {
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            File file = new File(mContext.getFilesDir(), "gesture.data");
            ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectInputStream.writeObject(Data.data);
            objectInputStream.flush();
            objectInputStream.close();
        } catch (Exception e) {
            Log.e("quoccuong", "save data " + e.toString());
        }
//
//        try {
//            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
//            File file = new File(sdcard + "/gesture.data");
//            Log.e("quoccuong", "save data " + file.getPath());
//            ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(file));
//            objectInputStream.writeObject(Data.data);
//            objectInputStream.flush();
//            objectInputStream.close();
//        } catch (Exception e) {
//            Log.e("quoccuong", "save data " + e.toString());
//        }
    }
}
