package com.limxing.beijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * �̳���ViewGroup
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
	
		// ���´���
		return false;// android.widget.TextView@4053b850 �����view��������

		
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {// ֱ��return������true����false �Ļ�
//		return super.onTouchEvent(ev);				// �����ܻ���
//		//return false;
//	}
	/**
	 * onInterceptTouchEvent(ev)ֵΪtrueʱ����View�Ĵ����¼��ͻᱻ�жϣ�
	 * dispatchTouchEvent�����ͷ�����true����������ViewGroup��touch�¼�
	 * 
	 * 
	 * * 1/����������������ڣ�����returnʲô ȫ�����ܻ��� onInterceptTouchEvent false onTouchEvent
	 * false
	 * 
	 * onInterceptTouchEvent false onTouchEvent true
	 * 
	 * onInterceptTouchEvent true onTouchEvent false slidingMenu������Ӧ
	 * 
	 * onInterceptTouchEvent true onTouchEvent true
	 * 
	 * 
	 * 2/���ֻ��onInterceptTouchEvent ��д�ˣ�����ķ����޷�ִ��
	 * 
	 * return true; �����ܹ����������治�ܻ��� return false; �����ܹ����������治�ܻ��� return
	 * super.onInterceptTouchEvent(ev); ȫ��Ļ���Ի���
	 * 
	 * 
	 * 3/���ֻ��onTouchEvent ��д�ˣ���������������޷�ִ�� return true; ���²��ܻ��� return false;
	 * ���²��ܻ��� return super.onTouchEvent(ev) ȫ���ɻ���
	 * 
	 * 
	 * 4/��� onInterceptTouchEvent false onTouchEvent return
	 * super.onTouchEvent(ev) ������Ի�
	 * 
	 * onInterceptTouchEvent true onTouchEvent return super.onTouchEvent(ev)
	 * ������Ի�
	 * 
	 * 
	 * 5/��� onInterceptTouchEvent super.onInterceptTouchEvent(ev) ȫ���ɻ���
	 * onTouchEvent super.onTouchEvent(ev)
	 * 
	 * 
	 * 
	 * 6/��� onInterceptTouchEvent super.onInterceptTouchEvent(ev) ȫ�����ɻ���
	 * //Ĭ����false���Խ��¼����ݵ����ڲ� onTouchEvent return true
	 * 
	 * onInterceptTouchEvent super.onInterceptTouchEvent(ev) ȫ�����ɻ��� onTouchEvent
	 * return false
	 * 
	 * 
	 * 
	 * ��������ĳ���ؼ������Ȼ�ȥ���øÿؼ����ڲ��ֵ�dispatchTouchEvent������
	 * Ȼ���ڲ��ֵ�dispatchTouchEvent�������ҵ����������Ӧ�ؼ�����ȥ���øÿؼ���dispatchTouchEvent������
	 * ������ǵ����MyLayout�еİ�ť
	 * ������ȥ����MyLayout��dispatchTouchEvent������������ᷢ��MyLayout�в�û�����������
	 * �Ǿ��ٵ����ĸ���LinearLayout����һ�ң�����Ҳû�������������ֻ�ü�������LinearLayout�ĸ���ViewGroup��
	 * ��������ViewGroup�п����������������ť��dispatchTouchEvent����������������õ�
	 * 
	 * disallowIntercept��ָ�Ƿ���õ��¼����صĹ��ܣ�Ĭ����false��
	 * Ҳ����ͨ������requestDisallowInterceptTouchEvent���������ֵ�����޸ġ�
	 * 
	 * �ڶ���ֵ��ʲô�أ���Ȼ���Ƕ�onInterceptTouchEvent�����ķ���ֵȡ����
	 * Ҳ����˵���������onInterceptTouchEvent�����з���false���ͻ��õڶ���ֵΪtrue��
	 * �Ӷ����뵽�����жϵ��ڲ������������onInterceptTouchEvent�����з���true
	 * ���ͻ��õڶ���ֵΪfalse���Ӷ���������������ж�
	 * 
	 * ͨ��һ��forѭ���������˵�ǰViewGroup�µ�����child��View��Ȼ���жϵ�ǰ������View�ǲ������ڵ����View��
	 * ����ǵĻ��ͻ���뵽�������жϵ��ڲ��� Ȼ����forѭ���ڲ������˸�child��View��dispatchTouchEvent
	 * 
	 * ���һ���ؼ��ǿɵ���ģ���ô����ÿؼ�ʱ��dispatchTouchEvent�ķ���ֵ�ض���true
	 * ������forѭ���ͻ�������ֻҪ��һ��child��view�������������¼�����ֱ�Ӳ������±����� ��û�����ģ���Ȼһ��һ��ķ��ظ��ϲ��view
	 * 
	 * ��������ǵ���Ĳ��ǰ�ť�����ǿհ������أ����������һ��������forѭ���ڷ���true�ˣ����ǻ����ִ�к���Ĵ��롣�����Ǽ������󿴣��ڵ�44�У�
	 * ���target����null
	 * ���ͻ���뵽�������ж��ڲ�������һ�������target������null����˻��ڵ�50�е���super.dispatchTouchEvent
	 * (ev)�����������õ������أ���Ȼ��View�е�dispatchTouchEvent�����ˣ���ΪViewGroup�ĸ������View��
	 * ֮��Ĵ����߼��ֺ�ǰ����˵����һ������
	 * ��Ҳ���MyLayout��ע���onTouch������õ�ִ�С�֮��Ĵ�����һ����������߲������ˣ�����Ҳ�Ͳ��ټ������·���
	 * 
	 * 
	 * 1. Android�е�touch��click�¼��ַ����ȴ��ݵ�ViewGroup������ViewGroup���ݵ�View�ġ�
	 * 
	 * 2. ��ViewGroup�п���ͨ��onInterceptTouchEvent�������¼����ݽ������أ�
	 * onInterceptTouchEvent��������true���������¼���������View����
	 * ������false�������¼��������أ�Ĭ�Ϸ���false��
	 * 
	 * 3. ��View����������ݵ��¼����ѵ���ViewGroup�н��޷����յ��κ��¼���
	 * 
	 * 4. ��view�����ûӴ���¼����ĵ���ViewGroup��һ�ε�
	 */
}
