package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fm_QuestionBank extends Fragment {

	private final String TAG = Fm_QuestionBank.class.toString();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fm_question_bank, container, false);
	}

}
