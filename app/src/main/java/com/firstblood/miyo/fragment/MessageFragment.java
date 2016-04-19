package com.firstblood.miyo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Message;
import com.firstblood.miyo.netservices.MessageServices;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人消息
 */
public class MessageFragment extends Fragment {


	@InjectView(R.id.base_header_title_tv)
	TextView mBaseHeaderTitleTv;
	@InjectView(R.id.message_rv)
	RecyclerView mMessageRv;
	@InjectView(R.id.message_srl)
	SwipeRefreshLayout mMessageSrl;
	@InjectView(R.id.message_no_data_rl)
	RelativeLayout mMessageNoDataRl;

	private final int messageNumber = 20;

	private MyRecyclerAdapter adapter;
	private LinearLayoutManager layoutManager;

	private boolean isLoading = false;

	public MessageFragment() {
	}

	public static MessageFragment newInstance() {
		return new MessageFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container, false);
		ButterKnife.inject(this, view);
		mBaseHeaderTitleTv.setText("消息");
		layoutManager = new LinearLayoutManager(getActivity());
		mMessageRv.setLayoutManager(layoutManager);
		adapter = new MyRecyclerAdapter();
		adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeChanged(int positionStart, int itemCount) {
				super.onItemRangeChanged(positionStart, itemCount);
				isLoading = false;
			}
		});
		mMessageRv.setAdapter(adapter);
		mMessageRv.addOnScrollListener(onScrollListener);
		mMessageSrl.setColorSchemeResources(android.R.color.holo_blue_light);
		mMessageSrl.setOnRefreshListener(onRefreshListener);
		return view;
	}

	private SwipeRefreshLayout.OnRefreshListener onRefreshListener = this::requestRefresh;

	private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
		boolean isMesaured = false;
		boolean isAfterTouch = false;

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			super.onScrollStateChanged(recyclerView, newState);
			if (newState == RecyclerView.SCROLL_STATE_IDLE) {
				isAfterTouch = true;
			}
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			super.onScrolled(recyclerView, dx, dy);
			int visibleItem = recyclerView.getChildCount();
			if (visibleItem < adapter.getItemCount() && !isMesaured && isAfterTouch) {
				adapter.setMinNumberItemShowFooter(adapter.getItemCount());
				isMesaured = true;
			}

			int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
			if (lastVisibleItemPosition == adapter.getItemCount() - 1 && !isLoading) {
				isLoading = true;
				requestMessageList();
			}

		}
	};

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mMessageSrl.post(() -> mMessageSrl.setRefreshing(true));
		requestMessageList();
	}

	private void requestRefresh() {
		requestMessageList();
	}

	private void requestMessageList() {
		MessageServices services = HttpMethods.getInstance().getClassInstance(MessageServices.class);
		services.getMessageList(SpUtils.getInstance().getUserId(), messageNumber)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<List<Message>>() {
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
						mMessageSrl.setRefreshing(false);
					}

					@Override
					public void onNext(List<Message> messages) {
						adapter.addData(messages);
						adapter.notifyDataSetChanged();
						mMessageSrl.setRefreshing(false);
						if (messages.isEmpty()) {
							mMessageNoDataRl.setVisibility(View.VISIBLE);
						} else {
							mMessageNoDataRl.setVisibility(View.GONE);
						}
					}
				});
	}

	private class MyRecyclerAdapter extends RecyclerView.Adapter {
		private static final int TYPE_ITEM = 1;
		private static final int TYPE_FOOTER = 2;
		public static final int DEFAULT_MINNUMBERITEM = 10;

		private int minNumberItemShowFooter = DEFAULT_MINNUMBERITEM;//item个数不小于此数量才显示footer。
		private List<Message> mMessages;

		public MyRecyclerAdapter() {
			mMessages = new ArrayList<>();
		}

		public void setData(List<Message> messages) {
			if (!messages.isEmpty()) {
				mMessages.clear();
				mMessages.addAll(messages);
			}
		}

		public void addData(List<Message> messages) {
			if (!messages.isEmpty()) {
				mMessages.addAll(messages);
			}
		}

		public void setMinNumberItemShowFooter(int minNumberItemShowFooter) {
			this.minNumberItemShowFooter = minNumberItemShowFooter;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == getItemCount() - 1 && getItemCount() > minNumberItemShowFooter) {
				return TYPE_FOOTER;
			} else {
				return TYPE_ITEM;
			}
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == TYPE_ITEM) {
				View v = View.inflate(parent.getContext(), R.layout.listitem_message, null);
				v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
				return new ItemViewHolder(v);
			}
			if (viewType == TYPE_FOOTER) {
				View view = View.inflate(parent.getContext(), R.layout.listitem_footer, null);
				view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
				return new FooterViewHolder(view);
			}
			return null;
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if (holder instanceof ItemViewHolder) {
				ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
				Message message = mMessages.get(position);
				itemViewHolder.mListItemMessageTitleTv.setText(message.getType() == 1 ? "合租消息" : "系统消息");
				itemViewHolder.mListItemMessageTypeIv.setImageResource(message.getType() == 1 ? R.drawable.icon_message_hezu : R.drawable.icon_message_system);
				itemViewHolder.mListItemMessageContentTv.setText(message.getContent());
				itemViewHolder.mListItemMessageTimeTv.setText(message.getTime());
			}
			if (holder instanceof FooterViewHolder) {

			}
		}

		@Override
		public int getItemCount() {
			return mMessages.size() > minNumberItemShowFooter ? mMessages.size() + 1 : mMessages.size();
		}

		class ItemViewHolder extends RecyclerView.ViewHolder {
			ImageView mListItemMessageTypeIv;
			TextView mListItemMessageTitleTv;
			TextView mListItemMessageContentTv;
			TextView mListItemMessageTimeTv;

			public ItemViewHolder(View itemView) {
				super(itemView);
				mListItemMessageTypeIv = (ImageView) itemView.findViewById(R.id.list_item_message_type_iv);
				mListItemMessageTitleTv = (TextView) itemView.findViewById(R.id.list_item_message_title_tv);
				mListItemMessageContentTv = (TextView) itemView.findViewById(R.id.list_item_message_content_tv);
				mListItemMessageTimeTv = (TextView) itemView.findViewById(R.id.list_item_message_time_tv);
			}
		}

		class FooterViewHolder extends RecyclerView.ViewHolder {

			public FooterViewHolder(View itemView) {
				super(itemView);
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}
