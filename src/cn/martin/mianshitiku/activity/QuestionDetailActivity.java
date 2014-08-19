package cn.martin.mianshitiku.activity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;

import cn.martin.mianshitiku.R;
import cn.martin.mianshitiku.ui.MyListView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class QuestionDetailActivity extends Activity implements OnClickListener {

	private ImageView mBackView;
	private ImageView mAvatarView;

	private TextView mNameView;
	private TextView mTimeView;
	private TextView mTitleView;
	private TextView mAnswerView;
	private MyListView mListView;
	private EditText mCommentView;
	private TextView mSubmitView;
	private TextView mCommentTitleView;

	private MyAdapter mAdapter;
	private AVObject mQuestionBean;

	private SimpleDateFormat mSFD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			Locale.US);

	private static final int MSG_GET_COMMENT = 100;
	/**
	 * 获取完面试题后，然后再获取面试题的评论
	 */
	private Handler mHandle = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case MSG_GET_COMMENT://开始获取评论
				if(mQuestionBean != null){
					getQuestionComment(mQuestionBean);
				}
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_detail);

		findViews();

		init();
	}

	/**
	 * 获取控件，设置监听
	 */
	private void findViews() {
		((TextView) findViewById(R.id.title)).setText(R.string.question_detail);
		mBackView = (ImageView) findViewById(R.id.back);
		mBackView.setVisibility(View.VISIBLE);
		mAvatarView = (ImageView) findViewById(R.id.avatar);
		mNameView = (TextView) findViewById(R.id.name);
		mTimeView = (TextView) findViewById(R.id.time);
		mTitleView = (TextView) findViewById(R.id.question_title);
		mAnswerView = (TextView) findViewById(R.id.answer);
		mListView = (MyListView) findViewById(R.id.list_view);
		mCommentView = (EditText) findViewById(R.id.edt_comment);
		mSubmitView = (TextView) findViewById(R.id.submit);
		mCommentTitleView = (TextView) findViewById(R.id.comment_title);
		mListView.setActivity(this);

		mBackView.setOnClickListener(this);
		mSubmitView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.submit:// 提交评论
			
			break;
		default:
			break;
		}
	}

	/**
	 * 提交评论(这个也等用户系统完成后再来实现)
	 */
	private void submitComment(){
		
	}
	
	/**
	 * 初始化数据
	 */
	private void init() {
		String id = getIntent().getStringExtra("QUESTION_ID");
		getQuestionInfo(id);
	}

	/**
	 * 获取面试题的内容
	 * 
	 * @param id
	 */
	private void getQuestionInfo(String id) {
		AVQuery<AVObject> query_obj = new AVQuery<AVObject>("Question");
		query_obj.include("poster");
		query_obj.getInBackground(id, new GetCallback<AVObject>() {
			@Override
			public void done(AVObject obj, AVException e) {
				if (e == null) {
					mQuestionBean = obj;
					AVObject user = obj.getAVObject("poster");
					if (user != null) {
						mNameView.setText(user.getString("username"));
						AVFile avatar = user.getAVFile("avatar");
						avatar.getDataInBackground(new GetDataCallback() {
							@Override
							public void done(byte[] data, AVException e) {
								if (e == null) {
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(data, 0,
													data.length);
									mAvatarView.setImageBitmap(bitmap);
								} else {
									System.out
											.println(" init get avatar failde");
									e.printStackTrace();
								}
							}
						});
					}
					mTimeView.setText(mSFD.format(obj.getCreatedAt()));
					mTitleView.setText(obj.getString("title"));
					mAnswerView.setText(obj.getString("answer"));

					mHandle.sendEmptyMessage(MSG_GET_COMMENT);//发送消息开始获取面试题的评论
					
				} else {
					System.out.println("get obj failde");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 查询问题的评论
	 * @param obj
	 */
	private void getQuestionComment(AVObject obj) {
		// 查询评论
		AVQuery<AVObject> query_comment = new AVQuery<AVObject>("Comment");
		query_comment.whereEqualTo("question", obj);
		query_comment.include("user");
		query_comment.orderByDescending("createdAt");
		query_comment.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if (e == null) {
					if (list != null && list.size() > 0) {
						mCommentTitleView.setVisibility(View.VISIBLE);
						mAdapter = new MyAdapter(QuestionDetailActivity.this);
						mAdapter.setData(list);
						mListView.setAdapter(mAdapter);
					}
				} else {
					System.out.println("query question comment failed ");
				}
			}
		});
	}

	/**
	 * 将显示置顶
	 */
	public void callback() {
		((ScrollView) findViewById(R.id.scroll_view)).scrollTo(10, 10);
		System.out.println("scroll to top xxxxxxxxxx");
	}

	/**
	 * 自定义评论适配器
	 * 
	 * @author zhangyang
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<AVObject> list;

		private MyAdapter(Context context) {
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
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				view = inflater.inflate(R.layout.question_comment_list_item,
						null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) view.findViewById(R.id.avatar);
				holder.name = (TextView) view.findViewById(R.id.name);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.comment = (TextView) view.findViewById(R.id.comment);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final ViewHolder _holder = holder;

			// 初始化，防止view的复用
			_holder.avatar.setImageResource(R.drawable.avatar_default);
			_holder.name.setText("");
			_holder.time.setText("");
			_holder.comment.setText("");

			AVObject obj = (AVObject) getItem(position);
			AVObject user = obj.getAVObject("user");
			System.out.println("--------------------------------");
			if (user != null) {
				_holder.name.setText(user.getString("username"));
				AVFile avatar = user.getAVFile("avatar");
				avatar.getDataInBackground(new GetDataCallback() {
					@Override
					public void done(byte[] data, AVException e) {
						if (e == null) {
							if (data != null) {
								try {
									Bitmap bitmap = BitmapFactory
											.decodeByteArray(data, 0,
													data.length);
									System.out
											.println("position "
													+ position
													+ " set avatar ___ bitmap is null ???? "
													+ (bitmap == null));
									_holder.avatar.setImageBitmap(bitmap);
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						} else {
							System.out.println("get user avatar failed");
							e.printStackTrace();
						}
					}
				});
			} else {
				System.out.println("user is null which coment is ::"
						+ obj.getString("content"));
			}
			_holder.time.setText(mSFD.format(obj.getCreatedAt()));
			_holder.comment.setText(obj.getString("content"));

			return view;
		}

		class ViewHolder {
			ImageView avatar;
			TextView name;
			TextView time;
			TextView comment;
		}

	}

}
