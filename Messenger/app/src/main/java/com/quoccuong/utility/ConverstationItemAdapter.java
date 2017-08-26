package com.quoccuong.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.messenger.R;
import com.quoccuong.model.Converstation;
import com.quoccuong.model.MyMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sev_user on 7/14/2015.
 */
public class ConverstationItemAdapter extends BaseAdapter {
    private ArrayList<Converstation> mListConversations;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ConverstationItemAdapter(Context context,
                                    ArrayList<Converstation> listConversations) {
        this.mListConversations = listConversations;
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return mListConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView personIcon;
        TextView phoneNumber;
        TextView time;
        TextView msg;
        TextView tvNotification;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        Converstation converstation = null;
        try {
            converstation = mListConversations.get(position);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

        if (converstation == null || parent == null) {
            return null;
        }

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.converstation_item,
                    null);

            viewHolder = new ViewHolder();
            viewHolder.tvNotification = (TextView) convertView
                    .findViewById(R.id.converstaton_item_tv_notification);
            viewHolder.personIcon = (ImageView) convertView
                    .findViewById(R.id.converstaton_item_personicon);
            viewHolder.phoneNumber = (TextView) convertView
                    .findViewById(R.id.converstaton_item_phonenumber);
            viewHolder.msg = (TextView) convertView
                    .findViewById(R.id.converstaton_item_msg);
            viewHolder.time = (TextView) convertView
                    .findViewById(R.id.converstaton_item_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int notifi = 0;
        for (int l = 0; l < converstation.getListMessages().size(); l++) {
            if (converstation.getListMessages().get(l).getReadState() == 0) {
                notifi++;
            }
        }

        if (notifi > 0) {
            viewHolder.tvNotification.setText("" + notifi);
            viewHolder.tvNotification
                    .setBackgroundResource(R.drawable.boderbutton);
            viewHolder.personIcon.setImageResource(R.drawable.new_message);
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.tvNotification.setText("");
            viewHolder.tvNotification.setBackgroundResource(R.drawable.empty);
            viewHolder.personIcon.setImageResource(R.drawable.read_message);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        viewHolder.phoneNumber.setText(converstation.getnName());

        MyMessage myMessage = null;

        try {
            myMessage = converstation.getListMessages().get(
                    converstation.getListMessages().size() - 1);
            viewHolder.msg.setText(myMessage.getmMsg());
            DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

            viewHolder.time.setText(df.format(myMessage.getmTime()));
        } catch (Exception e) {
            viewHolder.msg.setText("");
            viewHolder.time.setText("");
        }

        final String pho = converstation.getmPhoneNumber();
        final String name = converstation.getnName();
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext,
                        ConverstationActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("phoneNumber", pho);
                bundle.putString("name", name);

                intent.putExtra("bundle", bundle);

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
