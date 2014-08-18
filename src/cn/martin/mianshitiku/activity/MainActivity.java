package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TabHost;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private TabHost mTabHost;

	private RadioButton mBtnDynamic;
	private RadioButton mBtnQuestionBank;
	private RadioButton mBtnSet;

	private final String TAB_DYNAMIC = "tab1";
	private final String TAB_QUESTION_BANK = "tab2";
	private final String TAB_ME = "tab3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabHost.addTab(mTabHost.newTabSpec(TAB_DYNAMIC).setIndicator(TAB_DYNAMIC).setContent(R.id.tab_1));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_QUESTION_BANK).setIndicator(TAB_QUESTION_BANK).setContent(R.id.tab_2));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_ME).setIndicator(TAB_ME).setContent(R.id.tab_3));
		
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		mBtnDynamic = (RadioButton) findViewById(R.id.btn_dynamic);
		mBtnQuestionBank = (RadioButton) findViewById(R.id.btn_question_bank);
		mBtnSet = (RadioButton) findViewById(R.id.btn_set);

		mBtnDynamic.setOnClickListener(this);
		mBtnQuestionBank.setOnClickListener(this);
		mBtnSet.setOnClickListener(this);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dynamic:
			mTabHost.setCurrentTabByTag(TAB_DYNAMIC);
			break;
		case R.id.btn_question_bank:
			mTabHost.setCurrentTabByTag(TAB_QUESTION_BANK);
			break;
		case R.id.btn_set:
			mTabHost.setCurrentTabByTag(TAB_ME);
			break;
		}
	}

}
