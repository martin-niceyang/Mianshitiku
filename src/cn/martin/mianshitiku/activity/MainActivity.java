package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private FragmentTabHost mTabHost;

	private RadioButton mBtnDynamic;
	private RadioButton mBtnQuestionBank;
	private RadioButton mBtnMe;

	private final String TAB_DYNAMIC = "tab1";
	private final String TAB_QUESTION_BANK = "tab2";
	private final String TAB_ME = "tab3";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec(TAB_DYNAMIC).setIndicator(TAB_DYNAMIC),
				Fm_Dynamic.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_QUESTION_BANK).setIndicator(TAB_QUESTION_BANK),
				Fm_QuestionBank.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_ME).setIndicator(TAB_ME),
				Fm_Me.class, null);
		
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		mBtnDynamic = (RadioButton) findViewById(R.id.btn_dynamic);
		mBtnQuestionBank = (RadioButton) findViewById(R.id.btn_question_bank);
		mBtnMe = (RadioButton) findViewById(R.id.btn_me);

		mBtnDynamic.setOnClickListener(this);
		mBtnQuestionBank.setOnClickListener(this);
		mBtnMe.setOnClickListener(this);
		
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
		case R.id.btn_me:
			mTabHost.setCurrentTabByTag(TAB_ME);
			break;
		}
	}

}
