package com.limxing.safe.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private String packname;
	private long memsize;
	private Drawable icon;
	private String appname;
	private boolean usertask;
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public boolean isUsertask() {
		return usertask;
	}
	public void setUsertask(boolean usertask) {
		this.usertask = usertask;
	}
	

}
