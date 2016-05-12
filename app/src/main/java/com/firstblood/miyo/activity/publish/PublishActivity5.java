package com.firstblood.miyo.activity.publish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Token;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.netservices.HouseServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;
import com.firstblood.miyo.view.gridview.UnScrollGridView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PublishActivity5 extends AppCompatActivity {

	private static final int REQUEST_IMAGE = 1;
	@InjectView(R.id.publish5_gv)
	UnScrollGridView mPublish5Gv;
	@InjectView(R.id.publish5_title_et)
	EditText mPublish5TitleEt;
	@InjectView(R.id.publish5_description_et)
	EditText mPublish5DescriptionEt;
	@InjectView(R.id.publish5_delete_tv)
	TextView mPublish5DeleteTv;
	@InjectView(R.id.publish5_publish_tv)
	TextView mPublish5PublishTv;
	@InjectView(R.id.publish5_root_ll)
	LinearLayout rootLl;
	int finishNumber = 0;
	private ArrayList<String> paths;
	private ArrayList<String> imgKeys;
	private MyAdapter adapter;
	private int mGridWidth;
	private int mGridHeight;
	private KProgressHUD pd;
	private RxBus bus;
	private HashMap<String, Object> dataMap;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finishNumber++;
			pd.setProgress(finishNumber);
			if (finishNumber == paths.size()) {
				pd.dismiss();
				publishInfo();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish5);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish5));
		bus = RxBus.getInstance();
		dataMap = (HashMap<String, Object>) getIntent().getSerializableExtra("dataMap");
		paths = new ArrayList<>();
		imgKeys = new ArrayList<>();
		adapter = new MyAdapter();
		mPublish5Gv.setAdapter(adapter);
		mPublish5Gv.setOnItemClickListener((parent, view, position, id) -> {
			if (position == paths.size()) {
				imgSelect();
			}
		});
		mPublish5Gv.setOnItemLongClickListener((parent, view, position, id) -> {
			if (position != paths.size()) {
				deleteImg(position);
				return true;
			}
			return false;
		});
		initWidth();
	}

	private void imgSelect() {
		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
		// default select images (support array list)
		intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, paths);
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	private void deleteImg(int position) {
		new AlertDialog.Builder(this)
				.setTitle("删除图片？")
				.setNegativeButton("删除", (dialog, which) -> {
					paths.remove(position);
					adapter.notifyDataSetChanged();
					resizeGvHeight();
				})
				.setPositiveButton("取消", (dialog, which) -> dialog.dismiss())
				.show();
	}

	private void initWidth() {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			wm.getDefaultDisplay().getSize(size);
			width = size.x;
		} else {
			width = wm.getDefaultDisplay().getWidth();
		}
		mGridWidth = width / 4;
		mGridHeight = mGridWidth * 4 / 5;
	}

	@OnClick({R.id.publish5_delete_tv, R.id.publish5_publish_tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.publish5_delete_tv:
				break;
			case R.id.publish5_publish_tv:
				publishHouse();
				break;
		}
	}

	private void publishHouse() {
		getToken();
	}

	private void publishInfo() {
		dataMap.put("title", mPublish5TitleEt.getText().toString());
		dataMap.put("description", mPublish5DescriptionEt.getText().toString());
		JSONArray jsonArray = new JSONArray();
		for (String imgKey : imgKeys) {
			jsonArray.put(imgKey);
		}
		dataMap.put("image", jsonArray.toString());
		dataMap.put("userid", SpUtils.getInstance().getUserId());

		HouseServices services = HttpMethods.getInstance().getClassInstance(HouseServices.class);
		services.publishHouse(dataMap)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, o -> {
					AlertMessageUtil.showAlert(PublishActivity5.this, "发布成功");
					bus.send(new publishSucceed());
					finish();
				}));
	}

	private void getToken() {
		CommonServices services = HttpMethods.getInstance().getClassInstance(CommonServices.class);
		services.getToken()
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, this::uploadImg));
	}

	private void uploadImg(Token token) {
		pd = new KProgressHUD(this);
		pd.setStyle(KProgressHUD.Style.BAR_DETERMINATE)
				.setMaxProgress(paths.size())
				.setLabel("上传中...")
				.setCancellable(false)
				.setProgress(0);
		pd.show();
		for (int i = 0; i < paths.size(); i++) {
			UploadManager uploadManager = new UploadManager();
			String imgKey = System.currentTimeMillis() + i + "";
			imgKeys.add(imgKey);
			uploadManager.put(new File(paths.get(i)), imgKey, token.getToken(), (key, info, response) -> {
				handler.obtainMessage().sendToTarget();
			}, null);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				// Get the result list of select image paths
				List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				paths.clear();
				paths.addAll(path);
				adapter.notifyDataSetChanged();
				resizeGvHeight();
			}
		}
	}

	private void resizeGvHeight() {
		int n = getRealSize();
		int m = n / 3 == 3 ? 3 : (n / 3 + 1);//m+1=行数
		int o = 0;//间隔数
		if (n <= 3) o = 0;
		if (n > 3 && n <= 6) o = 1;
		if (n > 6) o = 2;
		int total = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20 + 5 * o, getResources().getDisplayMetrics()) + mGridHeight * m;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, total);
		mPublish5Gv.setLayoutParams(lp);
		rootLl.invalidate();
	}

	private int getRealSize() {
		if (paths.size() == 9) {
			return 9;
		} else {
			return paths.size() + 1;
		}
	}

	public static class publishSucceed {
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getRealSize();
		}

		@Override
		public String getItem(int position) {
			return paths.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = View.inflate(parent.getContext(), R.layout.item_publish5_img, null);
				vh.iv = (ImageView) convertView.findViewById(R.id.item_publish5_iv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if (position == paths.size()) {
				Picasso.with(parent.getContext())
						.load(R.drawable.icon_img_add)
						.resize(mGridWidth, mGridHeight)
						.into(vh.iv);
			} else {
				File file = new File(getItem(position));
				if (file.exists()) {
					Picasso.with(parent.getContext())
							.load(file)
							.resize(mGridWidth, mGridHeight)
							.centerCrop()
							.into(vh.iv);

				}

			}
			return convertView;
		}

		class ViewHolder {
			ImageView iv;
		}
	}
}
