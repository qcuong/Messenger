package com.quoccuong.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sev_user on 7/14/2015.
 */
public class MyMessage implements Serializable {
	private long id;
	private String mMsg;
	private Date mTime;
	private boolean mInbox;
	private int readState;
	private int errorCode;

	public MyMessage() {
		readState = 1;
	}

	public MyMessage(String mMsg, Date mTime, boolean mInbox, int readState,
			int errorCode) {
		super();
		this.mMsg = mMsg;
		this.mTime = mTime;
		this.mInbox = mInbox;
		this.readState = readState;
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getmMsg() {
		return mMsg;
	}

	public void setmMsg(String mMsg) {
		this.mMsg = mMsg;
	}

	public Date getmTime() {
		return mTime;
	}

	public void setmTime(Date mTime) {
		this.mTime = mTime;
	}

	public boolean ismInbox() {
		return mInbox;
	}

	public void setmInbox(boolean inbox) {
		this.mInbox = inbox;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

}
