package com.firstblood.miyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cs.networklibrary.http.ApiException;
import com.cs.networklibrary.http.HttpMethods;
import com.cs.widget.imageview.MaterialImageView;
import com.cs.widget.recyclerview.RecyclerViewDivider;
import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.house.HouseDetailActivity;
import com.firstblood.miyo.activity.user.LoginActivity;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Banner;
import com.firstblood.miyo.module.HomePageData;
import com.firstblood.miyo.module.House;
import com.firstblood.miyo.netservices.HouseServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.view.convenientbanner.LocalImageHolderView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class HomePageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
	private final int count = 20;
	@InjectView(R.id.home_page_xrv)
	XRecyclerView mHomePageXrv;
    private MyListAdapter adapter;
    private List<ImageView> images = new ArrayList<>();
    private List<ImageView> imageIndexs = new ArrayList<>();
	private View headerView;
	private ConvenientBanner<Banner> convenientBanner;
	private int index = 0;

	public HomePageFragment() {
	}

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.inject(this, view);

	    headerView = inflater.inflate(R.layout.listitem_home_page_header, null);
	    convenientBanner = (ConvenientBanner<Banner>) headerView.findViewById(R.id.list_item_home_page_cb);

	    return view;
    }

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		HouseServices services = HttpMethods.getInstance().getClassInstance(HouseServices.class);
		Observable.zip(services.getBanner(), services.getHeadPage(index, count), (arrayListHttpResult, houseModuleHttpResult) -> {
			if (!arrayListHttpResult.getResultCode().equals("0000")) {
				throw new ApiException(arrayListHttpResult.getResultMsg());
			}
			if (!houseModuleHttpResult.getResultCode().equals("0000")) {
				throw new ApiException(houseModuleHttpResult.getResultMsg());
			}
			return new HomePageData(arrayListHttpResult.getData(), houseModuleHttpResult.getData());
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(getActivity(), this::initView));

	}

	private void initView(HomePageData data) {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mHomePageXrv.setLayoutManager(layoutManager);
		mHomePageXrv.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
		adapter = new MyListAdapter();
		adapter.setData(data.getHouseModule().getData());
		mHomePageXrv.setAdapter(adapter);

		convenientBanner.setPages(LocalImageHolderView::new, data.getBanners())
				.setPageIndicator(new int[]{R.drawable.shape_image_index_white, R.drawable.shape_image_index_gray})
				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
		convenientBanner.startTurning(3000);
		mHomePageXrv.addHeaderView(headerView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

		private ArrayList<House> houses;

		private MyListAdapter() {
			houses = new ArrayList<>();
		}

		public void setData(ArrayList<House> houses) {
			this.houses.clear();
			this.houses.addAll(houses);
		}

		public void addData(ArrayList<House> houses) {
			this.houses.addAll(houses);
		}

	    @Override
	    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		    View v = View.inflate(parent.getContext(), R.layout.listitem_home_page, null);
		    v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
		    return new ViewHolder(v);
	    }

	    @Override
	    public void onBindViewHolder(ViewHolder holder, int position) {
		    House h = houses.get(position);
		    holder.titleTv.setText(h.getTitle());
		    holder.priceTv.setText("￥" + h.getPrice());
		    Picasso.with(getContext())
				    .load(CommonUtils.getQiNiuImgUrl(h.getHeadphoto(), Constant.IMAGE_CROP_RULE_W_200))
				    .placeholder(R.drawable.icon_default_head_img)
				    .into(holder.headPortraitIv);
		    try {
			    JSONArray array = new JSONArray(h.getImage());
			    Picasso.with(getContext())
					    .load(CommonUtils.getQiNiuImgUrl(array.getString(0), Constant.IMAGE_CROP_RULE_W_720))
					    .placeholder(R.drawable.img_default)
					    .into(holder.bgIv);
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
		    return houses.size();
	    }

		class ViewHolder extends RecyclerView.ViewHolder {
		    MaterialImageView bgIv;
		    TextView titleTv;
		    TextView priceTv;
		    CircleImageView headPortraitIv;

			public ViewHolder(View itemView) {
				super(itemView);
				bgIv = (MaterialImageView) itemView.findViewById(R.id.list_item_home_page_bg_civ);
				headPortraitIv = (CircleImageView) itemView.findViewById(R.id.list_item_home_page_head_portrait_iv);
				priceTv = (TextView) itemView.findViewById(R.id.list_item_home_page_price_tv);
				titleTv = (TextView) itemView.findViewById(R.id.list_item_home_page_house_title_tv);
			}
		}
	}
}
