package com.quoccuong.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract.PhoneLookup;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.messenger.MainActivity;
import com.quoccuong.model.Converstation;
import com.quoccuong.model.MyMessage;

class AddNewMessage extends Thread {

    private HashMap<String, ArrayList<MyMessage>> mListAllNewMessages;

    public AddNewMessage(HashMap<String, ArrayList<MyMessage>> listAllNewMessages) {
        super();
        this.mListAllNewMessages = listAllNewMessages;
    }

    @Override
    public void run() {
        Data.isAddNewMessageRunning = true;
        // TODO Auto-generated method stub
        if (mListAllNewMessages != null) {
            for (String key : mListAllNewMessages.keySet()) {
                boolean check = true;
                for (int i = 0; i < Data.listConverstations.size(); i++) {
                    if (Data.listConverstations.get(i).getmPhoneNumber()
                            .compareTo(key) == 0) {
                        Data.listConverstations.get(i).getListMessages().addAll(mListAllNewMessages.get(key));
                        check = false;
                        break;
                    }
                }

                if (check) {
                    String name = key;

                    for (int t = 0; t < Data.listContacts.size(); t++) {
                        if (Data.listContacts.get(t).getPhoneNumber()
                                .compareTo(key) == 0) {
                            name = Data.listContacts.get(t).getName();
                            break;
                        }
                    }

                    ArrayList<MyMessage> mylistMsgs = new ArrayList<MyMessage>();
                    mylistMsgs.addAll(mListAllNewMessages.get(key));
                    Converstation converstation = new Converstation(key, name,
                            mylistMsgs);
                    converstation.setnName(name);
                    converstation.setmPhoneNumber(key);
                    Data.listConverstations.add(converstation);
                }
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
        Data.isAddNewMessageRunning = false;
    }
}