package com.cbuu.finalproject;

import android.R.string;

public class Note {

	private int id;
	private long time;
	private String content;

	private double longitude;
	private double latitude;

	public Note(String content, long time, double longitude, double latitude) {
		this.content = content;
		this.time = time;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Note(){}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
