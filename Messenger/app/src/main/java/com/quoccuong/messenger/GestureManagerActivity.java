package com.quoccuong.messenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.quoccuong.data.Data;
import com.quoccuong.utility.GestureItemAdapter;
import com.quoccuong.utility.SaveGestureData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class GestureManagerActivity extends Activity implements View.OnClickListener{

    private GestureManagerActivity mContext;
    private ListView lvListGesture;
    private GestureItemAdapter gestureItemAdapter;
    private ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_manager);
        mContext = this;

        gestureItemAdapter = new GestureItemAdapter(this);
        lvListGesture = (ListView) findViewById(R.id.activity_gesture_manager_lv_lisgusture);
        lvListGesture.setAdapter(gestureItemAdapter);

        imgAdd = (ImageView) findViewById(R.id.activity_gesture_manager_imv_newsms);
        imgAdd.setOnClickListener(this);
        registerForContextMenu(lvListGesture);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestureItemAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gesture_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            case R.id.menu_gesture_add:
                Bundle bundle = new Bundle();
                bundle.putString("msg", "add");
                bundle.putInt("position", position);

                Intent intent = new Intent(mContext, CreateNewGestueActivity.class);
                intent.putExtra("msg", bundle);

                startActivity(intent);
                return true;
            case R.id.menu_gesture_delete:
                Log.i("quoccuong", "delete clicked");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Data.data.remove(position);
                                gestureItemAdapter.notifyDataSetChanged();
                                try {
                                    File file = new File(mContext.getFilesDir(), "gesture.data");
                                    ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(file));
                                    objectInputStream.writeObject(Data.data);
                                    objectInputStream.flush();
                                    objectInputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        mContext);
                builder.setMessage("Do you delete gesture?")
                        .setPositiveButton("Yes",
                                dialogClickListener)
                        .setNegativeButton("No",
                                dialogClickListener).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_gesture_manager_imv_newsms){
            Bundle bundle = new Bundle();
            bundle.putString("msg", "create");

            Intent intent = new Intent(mContext, CreateNewGestueActivity.class);
            intent.putExtra("msg", bundle);

            startActivity(intent);
            return;
        }
    }
}
