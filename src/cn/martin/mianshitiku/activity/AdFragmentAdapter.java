package cn.martin.mianshitiku.activity;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public AdFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setFragments(List<Fragment> list){
		fragments = list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(fragments == null) return 0;
		return fragments.size();
	}

}
