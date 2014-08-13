package cn.martin.mianshitiku.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import cn.martin.mianshitiku.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Fm_Dynamic extends Fragment {

	private final String TAG = Fm_Dynamic.class.toString();
	
	private List<AVObject> mList;
	private ListView mListView;
	private MyAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fm_dynamic, container, false) ;
			mListView = (ListView) view.findViewById(R.id.list_view);
		return view;
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		AVQuery<AVObject> query = new AVQuery<AVObject>("Question");
		query.include("poster");
		query.include("category");
		query.orderByDescending("updatedAt");
//		query.whereEqualTo("needVerify", false);
		query.findInBackground(new FindCallback<AVObject>() {
			
			@Override
			public void done(List<AVObject> list, AVException e) {
				System.out.println("-----------------------------");
				if(e == null){
					if(list != null){
						System.out.println("[DEBUG] list size____:" + list.size());
						mList = list;
						
						mAdapter = new MyAdapter(getActivity());
						mListView.setAdapter(mAdapter);
					}
				}else{
					System.out.println("xxxx [ERROR] query failed");
					e.printStackTrace();
				}
			}
		});
	}
	
	private class MyAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		
		public MyAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if(mList == null) return 0;
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if(view == null){
				view = inflater.inflate(R.layout.dynamic_list_item, null);
				holder = new ViewHolder();
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.name = (TextView) view.findViewById(R.id.name);
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.category = (TextView) view.findViewById(R.id.category);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			
			AVObject obj = (AVObject) getItem(position);
			
			AVObject user = obj.getAVObject("poster");
			if(user != null){
				holder.name.setText(user.getString("username"));
			}
			holder.title.setText(obj.getString("title"));
			AVObject category = obj.getAVObject("category");
			holder.category.setText(category.getString("title"));
			try {
				Date date =  obj.getDate("createdAt");
				SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
						Locale.US);
				System.out.println("date is null ??? " + (date == null));
				if(date != null){
					String time = sfd.format(date);
					System.out.println("time___:" + time);
					holder.time.setText(time);
				}
			} catch (Exception e) {
				System.out.println("date to String error");
				e.printStackTrace();
			}
			
			return view;
		}
		
		class ViewHolder{
			ImageView image;//头像
			TextView name;//名字
			TextView time;//出题时间
			TextView title;//题目标题
			TextView category;//题目所属
		}
		
	}
	
}
