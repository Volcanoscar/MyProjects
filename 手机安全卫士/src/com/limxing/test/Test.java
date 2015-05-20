package com.limxing.test;

import java.util.Random;

import android.content.Context;
import android.test.AndroidTestCase;

import com.limxing.safe.db.dao.BlackNumberDao;

public class Test extends AndroidTestCase {

	private Context context;
	public void setUp() throws Exception{
		context=getContext();
		super.setUp();
	}
	/**
	 * 测试添加
	 */
	public void testAdd() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(context);
		Random random=new Random(8979);
		for(long i=1;i<30;i++){
			long number=135000001+i;
			dao.add(number+"", String.valueOf(random.nextInt(3)+1));
		}
	}
	/**
	 * 删除所有数据
	 */
	public void testDelete()throws Exception{
		BlackNumberDao dao=new BlackNumberDao(context);
		boolean b=dao.deleteAll();
		System.out.println(b);
	}
	/**
	 * 删除某一条记录
	 * 
	 */
	public void delete() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(context);
		boolean b=dao.delete("123");
	}
	/**
	 * 修改某一条记录
	 */
	public void update() throws Exception{
		BlackNumberDao dao=new BlackNumberDao(context);
		boolean b=dao.changeMode("110", "2");
	}
	

}
