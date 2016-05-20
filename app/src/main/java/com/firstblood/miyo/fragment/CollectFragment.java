package com.firstblood.miyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.cs.widget.imageview.MaterialImageView;
import com.cs.widget.recyclerview.RecyclerViewDivider;
import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.house.HouseDetailActivity;
import com.firstblood.miyo.activity.user.LoginActivity;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Collection;
import com.firstblood.miyo.module.CollectionModule;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.util.RxBus;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cs on 16/5/15.
 */
public class CollectFragment extends Fragment {
	private final int count = 20;
	@InjectView(R.id.base_header_title_tv)
	TextView mBaseHeaderTitleTv;
	@InjectView(R.id.collect_xrv)
	XRecyclerView mCollectXrv;
	@InjectView(R.id.collect_empty_rl)
	RelativeLayout mCollectEmptyRl;
	private MyListAdapter adapter;
	private int index;
	private RxBus bus;
	private Subscription subscription;
	private boolean shouldRefresh = true;

	public CollectFragment() {
		bus = RxBus.getInstance();
		subscription = bus.toObserverable(CollectionChangeAction.class).subscribe(collectionChangeAction -> shouldRefresh = true);
	}

	public static CollectFragment newInstance() {
		return new CollectFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_collect, null);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mBaseHeaderTitleTv.setText(R.string.title_collection);

		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mCollectXrv.setLayoutManager(layoutManager);
		mCollectXrv.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
		adapter = new MyListAdapter();
		mCollectXrv.setAdapter(adapter);
		mCollectXrv.setEmptyView(mCollectEmptyRl);
		mCollectXrv.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				requestRefresh();
			}

			@Override
			public void onLoadMore() {
				requestLoadMore();
			}
		});

		mCollectEmptyRl.setOnClickListener(v -> requestRefresh());
	}

	@Override
	public void onResume() {
		super.onResume();
		if (shouldRefresh) {
			mCollectXrv.setRefreshing(true);
			shouldRefresh = false;
		}
	}

	private void requestRefresh() {
		index = 0;
		requestCollcetion(Constant.TYPE_REFRESH);
	}

	private void requestLoadMore() {
		index += count;
		requestCollcetion(Constant.TYPE_LOADMORE);
	}

	private void requestCollcetion(int type) {
		UserServices services = HttpMethods.getInstance().getClassInstance(UserServices.class);
		services.getMyCollection(SpUtils.getInstance().getUserId(), index, count)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<CollectionModule>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						if (e instanceof SocketTimeoutException) {
							Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
						} else if (e instanceof ConnectException) {
							Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getActivity(), "错误:" + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						if (type == Constant.TYPE_LOADMORE) {
							mCollectXrv.loadMoreComplete();
						} else if (type == Constant.TYPE_REFRESH) {
							mCollectXrv.refreshComplete();
						}
					}

					@Override
					public void onNext(CollectionModule collectionModule) {
						if (type == Constant.TYPE_LOADMORE) {
							adapter.addData(collectionModule.getData());
							adapter.notifyDataSetChanged();
							mCollectXrv.loadMoreComplete();
							if (collectionModule.getData().isEmpty()) {
								AlertMessageUtil.showAlert(getActivity(), "没有更多了");
								mCollectXrv.setLoadingMoreEnabled(false);
							}
						} else if (type == Constant.TYPE_REFRESH) {
							adapter.clearData();
							adapter.addData(collectionModule.getData());
							adapter.notifyDataSetChanged();
							mCollectXrv.refreshComplete();
							if (!collectionModule.getData().isEmpty()) {
								mCollectXrv.setLoadingMoreEnabled(true);
							}
						}
					}
				});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
		if (subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}

	public static class CollectionChangeAction {
	}

	private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

		private ArrayList<Collection> collections;

		private MyListAdapter() {
			collections = new ArrayList<>();
		}

		public void clearData() {
			this.collections.clear();
		}

		public void addData(ArrayList<Collection> collections) {
			this.collections.addAll(collections);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = View.inflate(parent.getContext(), R.layout.listitem_collect, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 245, getActivity().getResources().getDisplayMetrics())));
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			Collection h = collections.get(position);
			holder.titleTv.setText(h.getAddress());
			holder.priceTv.setText("￥" + h.getPrice());
			Picasso.with(getContext())
					.load(CommonUtils.getQiNiuImgUrl(h.getHeadphoto(), Constant.IMAGE_CROP_RULE_W_200))
					.placeholder(R.drawable.icon_default_head_img)
					.into(holder.headPortraitIv);
			try {

				String image = h.getImage();
				if (!TextUtils.isEmpty(image)) {
					JSONArray array = new JSONArray(image);
					Picasso.with(getContext())
							.load(CommonUtils.getQiNiuImgUrl(array.getString(0), Constant.IMAGE_CROP_RULE_W_720))
							.placeholder(R.drawable.img_default)
							.into(holder.bgIv);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			holder.itemView.setOnClickListener(v -> {
				if (SpUtils.getInstance().getUser() == null) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
					intent.putExtra("houseId", h.getId());
					startActivity(intent);
				}
			});
		}

		@Override
		public int getItemCount() {
			return collections.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			MaterialImageView bgIv;
			TextView titleTv;
			TextView priceTv;
			CircleImageView headPortraitIv;
			TextView date;

			public ViewHolder(View itemView) {
				super(itemView);
				bgIv = (MaterialImageView) itemView.findViewById(R.id.listitem_collect_bg_iv);
				headPortraitIv = (CircleImageView) itemView.findViewById(R.id.listitem_collect_head_portrait_iv);
				priceTv = (TextView) itemView.findViewById(R.id.listitem_collect_price_tv);
				titleTv = (TextView) itemView.findViewById(R.id.listitem_collect_address_tv);
				date = (TextView) itemView.findViewById(R.id.listitem_collect_date_tv);
			}
		}
	}
}
