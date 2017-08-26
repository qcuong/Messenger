package com.quoccuong.data;

import android.app.Activity;
import android.graphics.Typeface;

import com.quoccuong.model.Contact;
import com.quoccuong.model.Converstation;
import com.quoccuong.model.MyMessage;
import com.quoccuong.model.MyPair;
import com.quoccuong.model.PointOfTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by sev_user on 7/14/2015.
 */
public class Data {
    public static ArrayList<Converstation> listConverstations = new ArrayList<Converstation>();
    public static ArrayList<Contact> listContacts = new ArrayList<Contact>();
    public static boolean isLoadMessageRunning = false;
    public static boolean isAddNewMessageRunning = false;
    public static Activity mContext = null;

    public static ArrayList<MyPair<String, ArrayList<ArrayList<PointOfTime>>>> data = null;

    public static Typeface typeface = null;

    public static void sortSMS() {
        for (int i = 0; i < Data.listConverstations.size(); i++) {
            Collections.sort(Data.listConverstations.get(i).getListMessages(),
                    new MessageComparator());
        }

        Collections
                .sort(Data.listConverstations, new ConverstationComparator());

    }


    static class MessageComparator implements Comparator<MyMessage> {

        @Override
        public int compare(MyMessage lhs, MyMessage rhs) {
            // TODO Auto-generated method stub
            return lhs.getmTime().compareTo(rhs.getmTime());
        }
    }

    static class ConverstationComparator implements Comparator<Converstation> {

        @Override
        public int compare(Converstation lhs, Converstation rhs) {
            // TODO Auto-generated method stub
            Date lhstime = lhs.getListMessages()
                    .get(lhs.getListMessages().size() - 1).getmTime();
            Date rhstime = rhs.getListMessages()
                    .get(rhs.getListMessages().size() - 1).getmTime();
            return rhstime.compareTo(lhstime);
        }
    }
}
