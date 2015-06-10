package com.limxing.beijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 继承自ViewGroup
 * 
 * @author Administrator
 * 
 */
public class MyViewPager extends LazyViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	
		// 向下传递
		return false;// android.widget.TextView@4053b850 这个在view的子类中

		
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {// 直接return不管是true还是false 的话
//		return super.onTouchEvent(ev);				// 都不能滑动
//		//return false;
//	}
	/**
	 * onInterceptTouchEvent(ev)值为true时，子View的触摸事件就会被中断，
	 * dispatchTouchEvent方法就返回了true。后续处理ViewGroup的touch事件
	 * 
	 * 
	 * * 1/如果两个方法都存在，不管return什么 全屏不能滑动 onInterceptTouchEvent false onTouchEvent
	 * false
	 * 
	 * onInterceptTouchEvent false onTouchEvent true
	 * 
	 * onInterceptTouchEvent true onTouchEvent false slidingMenu正常响应
	 * 
	 * onInterceptTouchEvent true onTouchEvent true
	 * 
	 * 
	 * 2/如果只有onInterceptTouchEvent 重写了，父类的方法无法执行
	 * 
	 * return true; 上面能够滑动，下面不能滑动 return false; 上面能够滑动，下面不能滑动 return
	 * super.onInterceptTouchEvent(ev); 全屏幕可以滑动
	 * 
	 * 
	 * 3/如果只有onTouchEvent 重写了，父类中这个方法无法执行 return true; 上下不能滑动 return false;
	 * 上下不能滑动 return super.onTouchEvent(ev) 全屏可滑动
	 * 
	 * 
	 * 4/如果 onInterceptTouchEvent false onTouchEvent return
	 * super.onTouchEvent(ev) 上面可以滑
	 * 
	 * onInterceptTouchEvent true onTouchEvent return super.onTouchEvent(ev)
	 * 上面可以滑
	 * 
	 * 
	 * 5/如果 onInterceptTouchEvent super.onInterceptTouchEvent(ev) 全屏可滑动
	 * onTouchEvent super.onTouchEvent(ev)
	 * 
	 * 
	 * 
	 * 6/如果 onInterceptTouchEvent super.onInterceptTouchEvent(ev) 全屏不可滑动
	 * //默认是false所以将事件传递到了内部 onTouchEvent return true
	 * 
	 * onInterceptTouchEvent super.onInterceptTouchEvent(ev) 全屏不可滑动 onTouchEvent
	 * return false
	 * 
	 * 
	 * 
	 * 当你点击了某个控件，首先会去调用该控件所在布局的dispatchTouchEvent方法，
	 * 然后在布局的dispatchTouchEvent方法中找到被点击的相应控件，再去调用该控件的dispatchTouchEvent方法。
	 * 如果我们点击了MyLayout中的按钮
	 * ，会先去调用MyLayout的dispatchTouchEvent方法，可是你会发现MyLayout中并没有这个方法。
	 * 那就再到它的父类LinearLayout中找一找，发现也没有这个方法。那只好继续再找LinearLayout的父类ViewGroup，
	 * 你终于在ViewGroup中看到了这个方法，按钮的dispatchTouchEvent方法就是在这里调用的
	 * 
	 * disallowIntercept是指是否禁用掉事件拦截的功能，默认是false，
	 * 也可以通过调用requestDisallowInterceptTouchEvent方法对这个值进行修改。
	 * 
	 * 第二个值是什么呢？竟然就是对onInterceptTouchEvent方法的返回值取反！
	 * 也就是说如果我们在onInterceptTouchEvent方法中返回false，就会让第二个值为true，
	 * 从而进入到条件判断的内部，如果我们在onInterceptTouchEvent方法中返回true
	 * ，就会让第二个值为false，从而跳出了这个条件判断
	 * 
	 * 通过一个for循环，遍历了当前ViewGroup下的所有child子View，然后判断当前遍历的View是不是正在点击的View，
	 * 如果是的话就会进入到该条件判断的内部， 然后在for循环内部调用了该child子View的dispatchTouchEvent
	 * 
	 * 如果一个控件是可点击的，那么点击该控件时，dispatchTouchEvent的返回值必定是true
	 * ，所以for循环就会跳出，只要有一个child子view消耗了这个点击事件，就直接不再往下遍历了 若没有消耗，自然一层一层的返回给上层的view
	 * 
	 * 那如果我们点击的不是按钮，而是空白区域呢？这种情况就一定不会在for循环内返回true了，而是会继续执行后面的代码。那我们继续往后看，在第44行，
	 * 如果target等于null
	 * ，就会进入到该条件判断内部，这里一般情况下target都会是null，因此会在第50行调用super.dispatchTouchEvent
	 * (ev)。这句代码会调用到哪里呢？当然是View中的dispatchTouchEvent方法了，因为ViewGroup的父类就是View。
	 * 之后的处理逻辑又和前面所说的是一样的了
	 * ，也因此MyLayout中注册的onTouch方法会得到执行。之后的代码在一般情况下是走不到的了，我们也就不再继续往下分析
	 * 
	 * 
	 * 1. Android中的touch和click事件分发是先传递到ViewGroup，再由ViewGroup传递到View的。
	 * 
	 * 2. 在ViewGroup中可以通过onInterceptTouchEvent方法对事件传递进行拦截，
	 * onInterceptTouchEvent方法返回true代表不允许事件继续向子View传递
	 * ，返回false代表不对事件进行拦截，默认返回false。
	 * 
	 * 3. 子View中如果将传递的事件消费掉，ViewGroup中将无法接收到任何事件。
	 * 
	 * 4. 子view中如果没哟将事件消耗掉，ViewGroup将一次的
	 */
}
