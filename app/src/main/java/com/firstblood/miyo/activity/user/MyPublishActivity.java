package com.firstblood.miyo.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.cs.networklibrary.util.PropertiesUtil;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.MyPublish;
import com.firstblood.miyo.module.MyPublishModule;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyPublishActivity extends AppCompatActivity {

	private static final int TYPE_REFRESH = 1;
	private static final int TYPE_LOAD_MORE = 2;
	private final int mCount = 15;
	@InjectView(R.id.my_publish_xrv)
	XRecyclerView mMyPublishXrv;
	@InjectView(R.id.my_publish_no_data_rl)
	RelativeLayout mMyPublishNoDataRl;
	private MyRecyclerAdapter adapter;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_publish);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setBack().setTitle("我的发布");
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mMyPublishXrv.setLayoutManager(layoutManager);
		mMyPublishXrv.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				requestRefresh();
			}

			@Override
			public void onLoadMore() {
				requestLoadMore();
			}
		});
		adapter = new MyRecyclerAdapter();
		mMyPublishXrv.setAdapter(adapter);

		mMyPublishXrv.setRefreshing(true);
	}

	private void requestRefresh() {
		adapter.clearData();
		index = 0;
		requestPublishList(TYPE_REFRESH);
	}

	private void requestLoadMore() {
		index += mCount;
		requestPublishList(TYPE_LOAD_MORE);
	}

	private void requestPublishList(int type) {
		UserServices services = HttpMethods.getInstance().getClassInstance(UserServices.class);
		services.getMyPublishList(SpUtils.getInstance().getUserId(), index, mCount)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<MyPublishModule>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						if (e instanceof SocketTimeoutException) {
							Toast.makeText(MyPublishActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
						} else if (e instanceof ConnectException) {
							Toast.makeText(MyPublishActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(MyPublishActivity.this, "错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						if (type == TYPE_LOAD_MORE) {
							mMyPublishXrv.loadMoreComplete();
						} else if (type == TYPE_REFRESH) {
							mMyPublishXrv.refreshComplete();
						}
					}

					@Override
					public void onNext(MyPublishModule myPublishModule) {
						adapter.addData(myPublishModule.getData());
						adapter.notifyDataSetChanged();
						if (type == TYPE_LOAD_MORE) {
							mMyPublishXrv.loadMoreComplete();
							if (myPublishModule.getData().isEmpty()) {
								AlertMessageUtil.showAlert(MyPublishActivity.this, "没有更多了");
								mMyPublishXrv.setLoadingMoreEnabled(false);
							}
						} else if (type == TYPE_REFRESH) {
							mMyPublishXrv.refreshComplete();
							if (myPublishModule.getData().isEmpty()) {
								mMyPublishNoDataRl.setVisibility(View.VISIBLE);
							} else {
								mMyPublishNoDataRl.setVisibility(View.GONE);
								mMyPublishXrv.setLoadingMoreEnabled(true);
							}
						}
					}
				});
	}

	private class MyRecyclerAdapter extends RecyclerView.Adapter {

		private List<MyPublish> mMessages;

		public MyRecyclerAdapter() {
			mMessages = new ArrayList<>();
		}

		public void clearData() {
			this.mMessages.clear();
		}

		public void addData(List<MyPublish> messages) {
			if (!messages.isEmpty()) {
				this.mMessages.addAll(messages);
			}
		}

		public List<MyPublish> getData() {
			return this.mMessages;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = View.inflate(parent.getContext(), R.layout.listitem_my_publish, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
			return new ItemViewHolder(v);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			ItemViewHolder h = (ItemViewHolder) holder;
			MyPublish myPublish = mMessages.get(position);
			h.date.setText(myPublish.getTime().replace("T", " "));
			h.addressName.setText(myPublish.getAddressname());
			h.address.setText(myPublish.getAddress());
			h.price.setText(myPublish.getPrice() + "/月");
			h.type.setText(myPublish.getIsflatshare() == 1 ? "整租" : "合租");
			try {
				JSONArray array = new JSONArray(myPublish.getImage());
				String url = "http://" + PropertiesUtil.getProperty("QINIU_URL") + "/" + array.getString(0) + Constant.IMAGE_CROP_RULE_W_200;
				Picasso.with(MyPublishActivity.this)
						.load(url)
						.placeholder(R.drawable.img_default)
						.error(R.drawable.img_default)
						.into(h.iv);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getItemCount() {
			return mMessages.size();
		}

		class ItemViewHolder extends RecyclerView.ViewHolder {
			TextView date;
			TextView addressName;
			TextView address;
			TextView type;
			TextView price;
			ImageView iv;

			public ItemViewHolder(View itemView) {
				super(itemView);
				date = (TextView) itemView.findViewById(R.id.listitem_my_publish_time_tv);
				address = (TextView) itemView.findViewById(R.id.listitem_my_publish_address_tv);
				addressName = (TextView) itemView.findViewById(R.id.listitem_my_publish_address_name_tv);
				type = (TextView) itemView.findViewById(R.id.listitem_my_publish_type_tv);
				price = (TextView) itemView.findViewById(R.id.listitem_my_publish_price_tv);
				iv = (ImageView) itemView.findViewById(R.id.listitem_my_publish_iv);
			}
		}
	}
}
