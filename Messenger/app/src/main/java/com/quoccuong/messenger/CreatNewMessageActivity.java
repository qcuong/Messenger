package com.quoccuong.messenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.quoccuong.data.Data;
import com.quoccuong.utility.ContactItemAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class CreatNewMessageActivity extends Activity {
    private MultiAutoCompleteTextView enterContact;
    private ContactItemAdapter contactAdapter;
    private EditText edText;
    private ImageView imvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_message);

        enterContact = (MultiAutoCompleteTextView) findViewById(R.id.activity_creat_message_entercontact);
        edText = (EditText) findViewById(R.id.activity_creat_message_edit_txt);
        imvSend = (ImageView) findViewById(R.id.activity_creat_message_imgv_send);

        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterContact.getText().toString();
                String text = edText.getText().toString();

                if (name == null || name.isEmpty() || text == null || text.isEmpty()) {
                    return;
                }

                ArrayList<String> phones = new ArrayList<String>();
                StringTokenizer st = new StringTokenizer(name);

                // printing next token
                while (st.hasMoreTokens()) {
                    String t = st.nextToken(",");
                    t = t.trim();
                    boolean check = true;
                    for (int i = 0; i < Data.listContacts.size(); i++) {
                        if (t.compareToIgnoreCase(Data.listContacts.get(i).getName()) == 0) {
                            phones.add(Data.listContacts.get(i).getPhoneNumber());
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        phones.add(t);
                    }
                }

                for (int i = 0; i < phones.size(); i++) {
                    sendSMS(phones.get(i), text);
                }
                finish();
            }
        });

        contactAdapter = new ContactItemAdapter(this, R.layout.contact_item);

        enterContact.setAdapter(contactAdapter);
        enterContact.setThreshold(1);
        enterContact.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    private void sendSMS(String phoneNumber, String txt) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        phoneNumber = phoneNumber.trim();
        if (phoneNumber.startsWith("+84")) {
            phoneNumber = phoneNumber.substring(3);
            phoneNumber = "0" + phoneNumber;
        }
        phoneNumber = phoneNumber.replaceAll("\\s", "");

        for (int i = 0; i < phoneNumber.length(); i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9'){
                return;
            }
        }

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        SmsManager sm = SmsManager.getDefault();
        ArrayList<String> parts = sm.divideMessage(txt);
        int numParts = parts.size();

        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

        for (int i = 0; i < numParts; i++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }

        try {
            sm.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents,
                    deliveryIntents);
        } catch (Exception e){
            Log.e("quoccuong", e.toString());
        }
    }



}
