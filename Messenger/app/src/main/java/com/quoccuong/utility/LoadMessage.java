package com.quoccuong.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.messenger.MainActivity;
import com.quoccuong.model.Contact;
import com.quoccuong.model.Converstation;
import com.quoccuong.model.MyMessage;

import java.util.ArrayList;
import java.util.Date;

public class LoadMessage extends Thread {
    private Activity mContext;

    public LoadMessage() {
        this.mContext = Data.mContext;
    }

    @Override
    public void run() {
        if (mContext == null) {
            return;
        }
        Data.isLoadMessageRunning = true;

        if (Data.listContacts.isEmpty()) {
            Data.listContacts.clear();

            // TODO Auto-generated method stub
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor people = mContext.getContentResolver().query(uri, projection,
                    null, null, null);

            int indexName = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (people == null || people.getCount() <= 0) {
                return;
            }
            if (!people.moveToFirst()) {
                return;
            }
            do {
                String name = people.getString(indexName);
                String phone = people.getString(indexNumber);

                phone = phone.trim();
                if (phone.startsWith("+84")) {
                    phone = phone.substring(3);
                    phone = "0" + phone;
                }
                phone = phone.replaceAll("\\s", "");

                Data.listContacts.add(new Contact(name, phone));

            } while (people.moveToNext());
        }


        Uri messageUri = Uri.parse("content://sms/");
        ContentResolver cr = mContext.getContentResolver();

        Cursor c = cr.query(messageUri, null, null, null, null);
        mContext.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            Data.listConverstations.clear();
            for (int i = 0; i < totalSMS; i++) {
                MyMessage message = new MyMessage();

                message.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
                String phone = c.getString(c.getColumnIndexOrThrow("address"));
                String type = c.getString(c.getColumnIndexOrThrow("type"));
                String time = c.getString(c.getColumnIndexOrThrow("date"));

                if (phone != null) {
                    message.setmMsg(c.getString(c.getColumnIndexOrThrow("body")));
                    message.setErrorCode(c.getInt(c
                            .getColumnIndexOrThrow("error_code")));
                    try {
                        message.setmTime(new Date(Long.parseLong(time)));
                    } catch (Exception e) {
                        // TODO: handle exception
                        message.setmTime(new Date());
                    }

                    if (type.compareTo("1") == 0) {
                        message.setmInbox(true);
                        message.setReadState(c.getInt(c.getColumnIndex("read")));
                    } else {
                        message.setmInbox(false);
                        message.setReadState(1);
                    }

                    phone = phone.trim();
                    if (phone.startsWith("+84")) {
                        phone = phone.substring(3);
                        phone = "0" + phone;
                    }
                    phone = phone.replaceAll("\\s", "");

                    boolean check = true;
                    for (int j = 0; j < Data.listConverstations.size(); j++) {
                        if (Data.listConverstations.get(j).getmPhoneNumber()
                                .compareTo(phone) == 0) {
                            Data.listConverstations.get(j).getListMessages().add(message);
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        String name = phone;
                        for (int t = 0; t < Data.listContacts.size(); t++) {
                            if (Data.listContacts.get(t).getPhoneNumber()
                                    .compareTo(phone) == 0) {
                                name = Data.listContacts.get(t).getName();
                                break;
                            }
                        }
                        ArrayList<MyMessage> mylistMsgs = new ArrayList<MyMessage>();
                        mylistMsgs.add(message);
                        Converstation converstation = new Converstation(phone, name,
                                mylistMsgs);
                        Data.listConverstations.add(converstation);
                    }
                }

                c.moveToNext();
            }
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
        Data.isLoadMessageRunning = false;
    }

}
