package com.quoccuong.utility;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quoccuong.messenger.ConverstationActivity;
import com.quoccuong.messenger.R;
import com.quoccuong.model.MyMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageItemAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MyMessage> listMessages;
	private LayoutInflater inflater;

	public MessageItemAdapter(Context mContext,
			ArrayList<MyMessage> listMessages) {
		super();
		this.mContext = mContext;
		this.listMessages = listMessages;
		inflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMessages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class ViewHolder {
		TextView tvTime;
		TextView tvMsg;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		final MyMessage myMessage = listMessages.get(position);

		// if (convertView == null) {

		if (myMessage.ismInbox()) {
			convertView = inflater.inflate(R.layout.message_item_left, null);
		} else {
			convertView = inflater.inflate(R.layout.message_item_right, null);

			TextView error = (TextView) convertView
					.findViewById(R.id.message_item_error);
			if (myMessage.getErrorCode() != 0) {
				error.setBackgroundResource(R.drawable.boder_error);
				error.setText("!");
			} else {
				error.setBackgroundResource(R.drawable.empty);
				error.setText("");
			}
		}

		viewHolder.tvMsg = (TextView) convertView
				.findViewById(R.id.message_item_msg);
		viewHolder.tvTime = (TextView) convertView
				.findViewById(R.id.message_item_time);
		convertView.setTag(viewHolder);

		DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

		viewHolder.tvTime.setText(df.format(myMessage.getmTime()));

		viewHolder.tvMsg.setText(myMessage.getmMsg());

		convertView.setOnClickListener(null);
		viewHolder.tvMsg.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (myMessage.getErrorCode() == 0) {
					return false;
				} else {
					Bundle bundle = new Bundle();
					bundle.putString("msg", "message resend");
					bundle.putLong("idsms", myMessage.getId());

					Message msg = Message.obtain();
					msg.setData(bundle);
					ConverstationActivity.handler.sendMessage(msg);
					return false;
				}
			}
		});

		return convertView;
		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }

	}

}
