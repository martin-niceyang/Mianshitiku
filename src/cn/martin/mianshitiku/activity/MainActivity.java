package cn.martin.mianshitiku.activity;

import java.util.ArrayList;
import java.util.List;

import cn.martin.mianshitiku.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private LinearLayout mLeftDrawer;
	private ListView mListView;

	private List<Integer> mList;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViews();

		init();
	}

	private void init() {
		Fragment index = new IndexFragment();
		getFragmentManager().beginTransaction()
				.replace(R.id.content_frame, index).commit();

		mList = new ArrayList<Integer>();
		mList.add(R.string.index);
		mList.add(R.string.write_question);
		mList.add(R.string.suggestion);
		mList.add(R.string.about);

		mAdapter = new MyAdapter(this);
		mListView.setAdapter(mAdapter);
	}

	private void findViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mListView = (ListView) findViewById(R.id.list_view);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void call_back_menu() {
		if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
			mDrawerLayout.closeDrawer(mLeftDrawer);
		} else {
			mDrawerLayout.openDrawer(mLeftDrawer);
		}
	}

	/**
	 * define Adapter for leftview's listview
	 * 
	 * @author zhangyang
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (mList == null)
				return 0;
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup arg2) {
			ViewHolder holder;
			if (convertview == null) {
				convertview = inflater.inflate(R.layout.left_view_list_item,
						null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertview.findViewById(R.id.image);
				holder.name = (TextView) convertview.findViewById(R.id.name);
				convertview.setTag(holder);
			} else {
				holder = (ViewHolder) convertview.getTag();
			}

			int name_id = (Integer) getItem(position);
			int image_id = 0;
			switch (name_id) {
			case R.string.index:
				image_id = R.drawable.icon_index;
				break;
			case R.string.write_question:
				image_id = R.drawable.icon_write;
				break;
			case R.string.suggestion:
				image_id = R.drawable.icon_suggest;
				break;
			case R.string.about:
				image_id = R.drawable.icon_about;
				break;
			default:
				break;
			}
			holder.name.setText(name_id);
			holder.image.setImageResource(image_id);
			return convertview;
		}

		class ViewHolder {
			TextView name;
			ImageView image;
		}
	}
}
