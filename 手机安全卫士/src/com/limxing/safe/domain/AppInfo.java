package com.limxing.safe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String apppack;

	public String getApppack() {
		return apppack;
	}

	public void setApppack(String apppack) {
		this.apppack = apppack;
	}

	private String apppath;

	public String getApppath() {
		return apppath;
	}

	public void setApppath(String apppath) {
		this.apppath = apppath;
	}

	private String appname;
	private Drawable appico;
	private long appsize;
	private boolean inphone;
	private boolean isuser;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public Drawable getAppico() {
		return appico;
	}

	public void setAppico(Drawable icon) {
		this.appico = icon;
	}

	public long getAppsize() {
		return appsize;
	}

	public void setAppsize(long appsize) {
		this.appsize = appsize;
	}

	public boolean isInphone() {
		return inphone;
	}

	public void setInphone(boolean inphone) {
		this.inphone = inphone;
	}

	public boolean isIsuser() {
		return isuser;
	}

	public void setIsuser(boolean isuser) {
		this.isuser = isuser;
	}

	@Override
	public String toString() {
		return "Soft [appname=" + appname + ", appsize=" + appsize
				+ ", inphone=" + inphone + ", isuser=" + isuser + "]";
	}
}
