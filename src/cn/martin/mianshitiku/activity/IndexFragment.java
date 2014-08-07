package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class IndexFragment extends Fragment{

	private ImageView mBtnMenu;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.index, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mBtnMenu = (ImageView) getActivity().findViewById(R.id.menu_btn);
		mBtnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Activity ac = getActivity();
				if(ac instanceof MainActivity){
					MainActivity main = (MainActivity) ac;
					main.call_back_menu();
				}
			}
		});
	}
}
