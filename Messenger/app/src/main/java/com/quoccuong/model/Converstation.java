package com.quoccuong.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sev_user on 7/14/2015.
 */
public class Converstation implements Serializable {
	/**
	 * 
	 */
	private String mPhoneNumber;
	private String nName;
	private ArrayList<MyMessage> listMessages = new ArrayList<MyMessage>();
	private boolean readState;

	public Converstation() {
	}

	public Converstation(String phoneNumber, String name, ArrayList<MyMessage> listMessages) {
		super();
		this.mPhoneNumber = phoneNumber;
		this.nName = name;
		this.listMessages = listMessages;
	}

	public String getmPhoneNumber() {
		return mPhoneNumber;
	}

	public void setmPhoneNumber(String mPhoneNumber) {
		this.mPhoneNumber = mPhoneNumber;
	}

	public ArrayList<MyMessage> getListMessages() {
		return listMessages;
	}

	public void setListMessages(ArrayList<MyMessage> listMessages) {
		this.listMessages = listMessages;
	}

	public boolean isReadState() {
		return readState;
	}

	public void setReadState(boolean readState) {
		this.readState = readState;
	}

	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}

}
