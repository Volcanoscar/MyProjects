package com.limxing.beijing.bean;

import java.util.List;
/*
 * 作者：Limxing 时间： 2015-6-11 上午11:22:13
 * 
 * 描述：新闻中心的JavaBean
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
