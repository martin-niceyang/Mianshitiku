package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fm_Me extends Fragment{
	
	private final String TAG = Fm_Me.class.toString();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "xxxxx____onCreateView_____xxxxxxxx");
		return inflater.inflate(R.layout.fm_me, container, false);
	}

	
}
