package com.limxing.beijing.bean;

import java.util.List;
/*
 * ���ߣ�Limxing ʱ�䣺 2015-6-11 ����11:22:13
 * 
 * �������������ĵ�JavaBean
 */
public class NewCenter {
	public String retcode;
	public List<NewCenterItem> data;
	public List<String> extend;

	public class NewCenterItem {
		public List<Children> children;
		public String id;
		public String title;
		public String type;
		public String url;
		public String urll;
		public String dayurl;
		public String excurl;
		public String weekurl;
	}

	public class Children {
		public String id;
		public String title;
		public String type;
		public String url;
	}

}
