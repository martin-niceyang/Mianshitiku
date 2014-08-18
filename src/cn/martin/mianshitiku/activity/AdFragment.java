package cn.martin.mianshitiku.activity;

import cn.martin.mianshitiku.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdFragment extends Fragment{

	private Bitmap bitmap;
	private String title;
	private String url;
	
	private ImageView imageView;
	private TextView titleView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.advertise, container, false);
		imageView = (ImageView) view.findViewById(R.id.image);
		titleView = (TextView) view.findViewById(R.id.advertis_title);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		imageView.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		titleView.setText(title);
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					Uri uri = Uri.parse(url);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					getActivity().startActivity(it);
				} catch (Exception e) {
					System.out.println("网址打开错误");
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public void setImage(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
}
