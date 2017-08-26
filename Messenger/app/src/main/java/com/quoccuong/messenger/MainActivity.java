package com.quoccuong.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.quoccuong.data.Data;
import com.quoccuong.model.Converstation;
import com.quoccuong.utility.ConverstationItemAdapter;
import com.quoccuong.utility.LoadGestureData;
import com.quoccuong.utility.LoadMessage;
import com.quoccuong.utility.SmsSendService;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView mlvMessage;
    private ConverstationItemAdapter mConverstationItemAdapter;
    public static Handler handler;
    private Context mContext;
    private ImageView imvNewSms;
    private ArrayList<Converstation> mListConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data.mContext = this;

        // http://www.iconarchive.com/show/100-flat-icons-by-graphicloads.html
        // https://www.google.com.vn/#q=how+to+creat+a+project+android+wear
        // http://stackoverflow.com/questions/3616386/creating-a-sms-application-in-android

        mContext = this;

        try {
            if (Data.typeface == null) {
                Data.typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/quoccuongfont.ttf");
            }

            if (Data.data == null) {
                LoadGestureData loadGestureData = new LoadGestureData(mContext);
                loadGestureData.start();
            }
        } catch (Exception e) {
            Log.e("qwerty", "error " + e.toString());
        }
        mListConversations = new ArrayList<>();

        mConverstationItemAdapter = new ConverstationItemAdapter(mContext,
                mListConversations);
        mlvMessage = (ListView) findViewById(R.id.activity_main_listview);
        mlvMessage.setAdapter(mConverstationItemAdapter);

        if (!SmsSendService.running) {
            Intent intent = new Intent(mContext, SmsSendService.class);
            startService(intent);
        }

        if (!Data.isLoadMessageRunning && !Data.isAddNewMessageRunning) {
            LoadMessage loadMessage = new LoadMessage();
            loadMessage.start();
        }

        imvNewSms = (ImageView) findViewById(R.id.activity_main_imv_newsms);
        imvNewSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreatNewMessageActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                Bundle bundle = msg.getData();

                String ms = bundle.getString("msg");

                if (ms.compareTo("new message") == 0) {
                    Log.e("quoccuong", "handler  mainactivity");
                    mListConversations.clear();
                    mListConversations.addAll(Data.listConverstations);
                    mConverstationItemAdapter.notifyDataSetChanged();
                }
            }
        };

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        try {
            mConverstationItemAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(mContext, GestureManagerActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
