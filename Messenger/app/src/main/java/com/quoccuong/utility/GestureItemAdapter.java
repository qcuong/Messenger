package com.quoccuong.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.R;

/**
 * Created by sev_user on 7/29/2015.
 */
public class GestureItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater ;

    public GestureItemAdapter(Context context) {
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Data.data.size();
    }

    @Override
    public Object getItem(int position) {
        return Data.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.gesture_item, null);
        TextView tvGesture = (TextView) convertView.findViewById(R.id.gesture_item_tv_gesture);
        TextView tvSize = (TextView) convertView.findViewById(R.id.gesture_item_tv_size);

        tvGesture.setText(Data.data.get(position).first);
        tvSize.setText("Number of sample : " + Data.data.get(position).second.size() + "_" + Data.data.get(position).second.get(0).size());
        return convertView;
    }
}
