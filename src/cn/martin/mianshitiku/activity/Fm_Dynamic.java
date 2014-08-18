package cn.martin.mianshitiku.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;

import cn.martin.mianshitiku.R;
import cn.martin.mianshitiku.bean.Advertise;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Advanceable;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fm_Dynamic extends Fragment {

	private final String TAG = Fm_Dynamic.class.toString();
	private TextView mTitleView;

	private List<Fragment> mAdvertiseList;
	private AdFragmentAdapter mAdFragmentAdapter;
	private ViewPager mViewPager;

	private ListView mListView;
	private MyAdapter mAdapter;

	private SimpleDateFormat mSFD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			Locale.US);

	private int mAdvertiseCount;
	private static final int MSG_ADVERTISE_COUNT = 100;
	private static final int MSG_ADVERTIS_LIST = 101;

	/**
	 * 先查询到广告的数量，再去查询广告
	 */
	private Handler mHander = new Handler() {
		public synchronized void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_ADVERTISE_COUNT:// 广告的数目
				getAdvertiseList();
				break;
			case MSG_ADVERTIS_LIST:
				if (mAdvertiseList != null && mAdvertiseList.size() == mAdvertiseCount) {
					mAdFragmentAdapter = new AdFragmentAdapter(getActivity()
							.getSupportFragmentManager());
					mAdFragmentAdapter.setFragments(mAdvertiseList);
					mViewPager.setAdapter(mAdFragmentAdapter);
				}
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fm_dynamic, container, false);
		mTitleView = (TextView) view.findViewById(R.id.title);
		mTitleView.setText(R.string.dynamic);
		mListView = (ListView) view.findViewById(R.id.list_view);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 查询广告
		getAdvertiseCount();
		// 查询面试题列表
		getQuestionList();

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(getActivity(),
						QuestionDetailActivity.class);
				AVObject obj = (AVObject) mListView.getAdapter().getItem(
						position);
				intent.putExtra("QUESTION_ID", obj.getObjectId());
				getActivity().startActivity(intent);
			}
		});

	}

	/**
	 * 获取广告的数目（获取到了之后通过handle发送消息，然后查询广告列表）
	 * 
	 * @return
	 */
	private void getAdvertiseCount() {
		// 查询广告
		AVQuery<AVObject> query_advertise = new AVQuery<AVObject>(
				"Advertise");
		query_advertise.orderByAscending("order");
		query_advertise.countInBackground(new CountCallback() {
			@Override
			public void done(int count, AVException e) {
				if (e == null) {
					mAdvertiseCount = count;
					mHander.sendEmptyMessage(MSG_ADVERTISE_COUNT);
				} else {
					System.out.println("query advertise count faild");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取广告列表
	 * 
	 * @param query_advertise
	 */
	private void getAdvertiseList() {
		AVQuery<AVObject> query_advertise = new AVQuery<AVObject>(
				"Advertise");
		query_advertise.orderByAscending("order");
		query_advertise.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if (e == null) {
					mAdvertiseList = new ArrayList<Fragment>();
					for (AVObject obj : list) {
						final AdFragment fragment = new AdFragment();
						fragment.setTitle(obj.getString("title"));
						fragment.setUrl(obj.getString("contentUrl"));
						AVFile file = obj.getAVFile("image");
						file.getDataInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] data, AVException e) {
								if (e == null) {
									fragment.setImage(BitmapFactory
											.decodeByteArray(data, 0,
													data.length));
									mAdvertiseList.add(fragment);
									mHander.sendEmptyMessage(MSG_ADVERTIS_LIST);
								} else {
									System.out
											.println("[ERROR] get ad image failed");
									e.printStackTrace();
								}
							}
						});
					}
				} else {
					System.out.println("xxxx [ERROR] query advertise failed");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 查询动态显示的面试题列表
	 */
	private void getQuestionList() {
		// 查询动态显示题目
		AVQuery<AVObject> query = new AVQuery<AVObject>("Question");
		query.include("poster");
		query.include("category");
		query.orderByDescending("updatedAt");
		// query.whereEqualTo("needVerify", false);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {
				if (e == null) {
					if (list != null) {
						mAdapter = new MyAdapter(getActivity());
						mAdapter.setData(list);
						mListView.setAdapter(mAdapter);
					}
				} else {
					System.out.println("xxxx [ERROR] query failed");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 处理Page的切换逻辑
	 */
	private void setCurrentItem() {
		if (mAdvertiseList == null || mAdvertiseList.size() <= 0)
			return;
		int index = mViewPager.getCurrentItem();
		if (index == mAdvertiseList.size() - 1) {
			index = 0;
		} else {
			index++;
		}
		mViewPager.setCurrentItem(index, true);
	}

	private Timer mTimer;

	/**
	 * 开启定时任务
	 */
	private void startTask() {
		// TODO Auto-generated method stub
		mTimer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setCurrentItem();
			}
		};
		mTimer.schedule(task, 0, 3 * 1000);// 这里设置自动切换的时间，单位是毫秒
	}

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		mTimer.cancel();
	}

	public void onResume() {
		super.onResume();
		setCurrentItem();
		startTask();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTask();
	}

	/**
	 * 自定义Adapter
	 * 
	 * @author zhangyang
	 * 
	 */
	private class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<AVObject> list;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void setData(List<AVObject> _list) {
			list = _list;
		}

		@Override
		public int getCount() {
			if (list == null)
				return 0;
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder _holder = null;
			if (view == null) {
				view = inflater.inflate(R.layout.dynamic_list_item, null);
				_holder = new ViewHolder();
				_holder.image = (ImageView) view.findViewById(R.id.image);
				_holder.name = (TextView) view.findViewById(R.id.name);
				_holder.title = (TextView) view.findViewById(R.id.title);
				_holder.time = (TextView) view.findViewById(R.id.time);
				_holder.category = (TextView) view.findViewById(R.id.category);
				view.setTag(_holder);
			} else {
				_holder = (ViewHolder) view.getTag();
			}

			final ViewHolder holder = _holder;

			// init，防止view复用
			holder.name.setText("");
			holder.image.setImageResource(R.drawable.avatar_default);
			holder.title.setText("");
			holder.category.setText("");
			holder.time.setText("");

			AVObject obj = (AVObject) getItem(position);

			AVObject user = obj.getAVObject("poster");
			if (user != null) {
				holder.name.setText(user.getString("username"));
				AVFile image = user.getAVFile("avatar");
				image.getDataInBackground(new GetDataCallback() {
					@Override
					public void done(byte[] data, AVException arg1) {
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						holder.image.setImageBitmap(bitmap);
					}
				});
			}
			holder.title.setText(obj.getString("title"));
			AVObject category = obj.getAVObject("category");
			holder.category.setText(category.getString("title"));
			holder.time.setText(mSFD.format(obj.getCreatedAt()));

			return view;
		}

		class ViewHolder {
			ImageView image;// 头像
			TextView name;// 名字
			TextView time;// 出题时间
			TextView title;// 题目标题
			TextView category;// 题目所属
		}

	}

}
