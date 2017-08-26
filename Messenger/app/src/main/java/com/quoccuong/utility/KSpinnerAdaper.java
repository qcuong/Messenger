package com.quoccuong.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quoccuong.data.Data;
import com.quoccuong.messenger.R;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/29/2015.
 */
public class KSpinnerAdaper extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> listK = new ArrayList<>();
    private LayoutInflater inflater;

    public KSpinnerAdaper(Context context, ArrayList<Integer> listK) {
        this.listK = listK;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listK.size();
    }

    @Override
    public Object getItem(int position) {
        return listK.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.k_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setTypeface(Data.typeface);
        tv.setText("" + listK.get(position));
        return convertView;
    }
}
