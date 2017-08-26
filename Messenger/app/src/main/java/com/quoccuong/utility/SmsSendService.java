package com.quoccuong.utility;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.messenger.MainActivity;
import com.quoccuong.model.Converstation;
import com.quoccuong.model.MyMessage;

import java.util.ArrayList;
import java.util.Date;

public class SmsSendService extends Service {
    ContentResolver contentResolver;
    Uri uri = Uri.parse("content://sms/");
    Handler handler;
    public static boolean running = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        running = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(uri, true,
                new MyContentObserver(handler));
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    public class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            if (Data.mContext == null) {
                return;
            }

            Uri messageUri = Uri.parse("content://sms/");
            ContentResolver cr = Data.mContext.getContentResolver();

            Cursor c = cr.query(messageUri, null, null, null, null);
            Data.mContext.startManagingCursor(c);

            if (!c.moveToFirst()) {
                return;
            }

            MyMessage message = new MyMessage();

            message.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
            String phone = c.getString(c.getColumnIndexOrThrow("address"));
            String type = c.getString(c.getColumnIndexOrThrow("type"));
            String time = c.getString(c.getColumnIndexOrThrow("date"));

            if (type.compareTo("1") == 0) {
                return;
            } else {
                message.setmInbox(false);
                message.setReadState(1);
            }

            message.setmMsg(c.getString(c.getColumnIndexOrThrow("body")));
            message.setErrorCode(c.getInt(c.getColumnIndexOrThrow("error_code")));

            try {
                message.setmTime(new Date(Long.parseLong(time)));
            } catch (Exception e) {
                // TODO: handle exception
                message.setmTime(new Date());
            }

            phone = phone.trim();
            if (phone.startsWith("+84")) {
                phone = phone.substring(3);
                phone = "0" + phone;
            }

            phone = phone.replaceAll("\\s", "");

            boolean c1 = true;
            for (int i = 0; i < Data.listConverstations.size(); i++) {
                if (phone.compareToIgnoreCase(Data.listConverstations.get(i).getmPhoneNumber()) == 0) {
                    c1 = false;
                    boolean c2 = true;
                    for (int j = 0; j < Data.listConverstations.get(i).getListMessages().size(); j++) {
                        if (message.getId() == Data.listConverstations.get(i).getListMessages().get(j).getId()) {
                            c2 = false;
                            Data.listConverstations.get(i).getListMessages().set(j, message);
                            break;
                        }
                    }

                    if (c2) {
                        Data.listConverstations.get(i).getListMessages().add(message);
                    }
                }
            }

            if (c1) {
                String name = phone;
                for (int t = 0; t < Data.listContacts.size(); t++) {
                    if (Data.listContacts.get(t).getPhoneNumber()
                            .compareTo(phone) == 0) {
                        name = Data.listContacts.get(t).getName();
                        break;
                    }
                }

                ArrayList<MyMessage> listMyMessages = new ArrayList<>();
                listMyMessages.add(message);

                Data.listConverstations.add(new Converstation(phone, name, listMyMessages));
            }

            Data.sortSMS();

            if (MainActivity.handler != null) {
                Message msg = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putString("msg", "new message");

                msg.setData(bundle);

                MainActivity.handler.sendMessage(msg);
            }

            if (ConverstationActivity.handler != null) {
                Message msg = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putString("msg", "new message");
                msg.setData(bundle);
                ConverstationActivity.handler.sendMessage(msg);
            }
        }
    }
}
