package com.limxing.beijing.bean;

import java.util.List;

public class NewBean {
	public NewBeanItem data;
	public String retcode;
	public class NewBeanItem{
		public String countcommenturl;
		//
		public String more;
		public String title;
		public List<News> news;
		public List<Topic> toptic;
		public List<Topnews> topnews;
	}
	
	public class News{
		public String comment;
		public String commentlist;
		public String commenturl;
		//
		public String listimage;
		//
		public String pubdate;
		//
		public String title;
		public String type;
		public String url;
		public boolean isRead;
		public String id;
	}

	public class Topic{
		public String description;
		public String id;
		public String listimage;
		public String sort;
		public String title;
		public String url;
	}
	public class Topnews{
		public String comment;
		public String commentlist;
		public String commenturl;
		public String id;
		//�ֲ�ͼƬ�����ӵ�ַ
		public String topimage;
		//ʱ��
		public String pubdate;
		//�����б�ı�������
		public String title;
		public String type;
		public String url;
	}
}
