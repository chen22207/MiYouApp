package com.firstblood.miyo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.cs.widget.recyclerview.RecyclerViewDivider;
import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.message.MessageDetailActivity;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Message;
import com.firstblood.miyo.module.MessageModule;
import com.firstblood.miyo.netservices.MessageServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.RxBus;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人消息
 */
public class MessageFragment extends Fragment {

    private static final int TYPE_REFRESH = 1;
    private static final int TYPE_LOAD_MORE = 2;
	private final int msgCount = 15;
	@InjectView(R.id.base_header_title_tv)
    TextView mBaseHeaderTitleTv;
    @InjectView(R.id.message_rv)
    XRecyclerView mMessageRv;
    @InjectView(R.id.message_no_data_rl)
    RelativeLayout mMessageNoDataRl;
    private int index = 0;
    private MyRecyclerAdapter adapter;
    private MessageServices services;

    private RxBus bus = RxBus.getInstance();

    private Subscription subscribe;

    public MessageFragment() {
        services = HttpMethods.getInstance().getClassInstance(MessageServices.class);
    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribe = bus.toObserverable(MessageDetailActivity.ReadMsg.class)
                .subscribe(readMsg -> {
                    for (Message message : adapter.getData()) {
                        if (message.getId().equals(readMsg.getMsgId())) {
                            message.setState(1);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.inject(this, view);
        mBaseHeaderTitleTv.setText("消息");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageRv.setLayoutManager(layoutManager);
	    mMessageRv.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
	    adapter = new MyRecyclerAdapter();
        mMessageRv.setAdapter(adapter);
        mMessageRv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                requestRefresh();
            }

            @Override
            public void onLoadMore() {
                requestLoadMore();
            }
        });
	    mMessageRv.setEmptyView(mMessageNoDataRl);
	    return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMessageRv.setRefreshing(true);
    }

    private void requestRefresh() {
        adapter.clearData();
        index = 0;
        requestMessageList(TYPE_REFRESH);
    }

    private void requestLoadMore() {
        index += msgCount;
        requestMessageList(TYPE_LOAD_MORE);
    }

    private void requestMessageList(int type) {
        services.getMessageList(SpUtils.getInstance().getUserId(), index, msgCount)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
		        .subscribe(new Subscriber<MessageModule>() {
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
                        if (type == TYPE_LOAD_MORE) {
                            mMessageRv.loadMoreComplete();
                        } else if (type == TYPE_REFRESH) {
                            mMessageRv.refreshComplete();
                        }
                    }

                    @Override
                    public void onNext(MessageModule message) {
	                    adapter.addData(message.getData());
	                    adapter.notifyDataSetChanged();
                        if (type == TYPE_LOAD_MORE) {
                            mMessageRv.loadMoreComplete();
	                        if (message.getData().isEmpty()) {
		                        AlertMessageUtil.showAlert(getActivity(), "没有更多了");
                                mMessageRv.setLoadingMoreEnabled(false);
                            }
                        } else if (type == TYPE_REFRESH) {
                            mMessageRv.refreshComplete();
	                        if (!message.getData().isEmpty()) {
		                        mMessageRv.setLoadingMoreEnabled(true);
                            }
                        }

                    }
                });
    }

	private void intentToDetail(String msgId) {
		Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
		intent.putExtra("msgId", msgId);
		getActivity().startActivity(intent);
	}

    private void requestDeleteMsg(String msgId) {
        services.deleteMessage(msgId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(getActivity(), noData -> {
                    for (Message message : adapter.getData()) {
                        if (message.getId().equals(msgId)) {
                            adapter.getData().remove(message);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (!subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

	private class MyRecyclerAdapter extends RecyclerView.Adapter {

		private List<Message> mMessages;

		public MyRecyclerAdapter() {
			mMessages = new ArrayList<>();
		}

		public void clearData() {
			this.mMessages.clear();
		}

		public void addData(List<Message> messages) {
			if (!messages.isEmpty()) {
				mMessages.addAll(messages);
			}
		}

		public List<Message> getData() {
			return this.mMessages;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = View.inflate(parent.getContext(), R.layout.listitem_message, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
			return new ItemViewHolder(v);
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			Message message = mMessages.get(position);
			itemViewHolder.mListItemMessageTitleTv.setText((message.getType() == 1 ? "合租消息" : "系统消息") + "--" + (message.getState() == 0 ? "未读" : "已读"));
			itemViewHolder.mListItemMessageTypeIv.setImageResource(message.getType() == 1 ? R.drawable.icon_message_hezu : R.drawable.icon_message_system);
			itemViewHolder.mListItemMessageContentTv.setText(message.getContent());
			itemViewHolder.mListItemMessageTimeTv.setText(message.getTime().replace("T", " "));
			itemViewHolder.itemView.setOnClickListener(v -> intentToDetail(message.getId()));
			itemViewHolder.itemView.setOnLongClickListener(v -> {
				new AlertDialog.Builder(getActivity())
						.setTitle("确认删除此消息？")
						.setPositiveButton("删除", (dialog, which) -> {
							requestDeleteMsg(message.getId());
							dialog.dismiss();
						})
						.setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
						.show();
				return true;
			});

		}

		@Override
		public int getItemCount() {
			return mMessages.size();
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
    }
}
