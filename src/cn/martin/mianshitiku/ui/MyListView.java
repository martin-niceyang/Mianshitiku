package cn.martin.mianshitiku.ui;

import cn.martin.mianshitiku.activity.QuestionDetailActivity;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView{

	private Activity activity;
	
	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expendSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expendSpec);
	}
	
	public void setActivity(Activity ac){
		this.activity = ac;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		System.out.println("xxxxx onSizeChanged xxxxxx");
		
		if(activity instanceof QuestionDetailActivity){
			System.out.println(" make a call back ");
			QuestionDetailActivity ac = (QuestionDetailActivity) activity;
			ac.callback();
		}else{
			System.out.println("activity is not instanceof QuestionActivity");
		}
	}
	
	
}
