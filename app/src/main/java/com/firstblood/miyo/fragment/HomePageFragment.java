package com.firstblood.miyo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.firstblood.miyou.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class HomePageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.home_page_lv)
    ListView homePageLv;

    private MyListAdapter adapter;

    private List<ImageView> images = new ArrayList<>();
    private List<ImageView> imageIndexs = new ArrayList<>();

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

        View header = inflater.inflate(R.layout.listitem_home_page_header, null);
        ViewPager vp = (ViewPager) header.findViewById(R.id.list_item_home_page_vp);
        LinearLayout imageIndexLl = (LinearLayout) header.findViewById(R.id.list_item_home_page_image_index_ll);


        MyPagerAdapter pagerAdapter = new MyPagerAdapter();
        for (int i = 0; i < 5; i++) {
            ImageView iv = new ImageView(getActivity());
//            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(R.drawable.f1);
            images.add(iv);
            ImageView iv1 = new ImageView(getActivity());
            iv1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            iv1.setImageResource(R.drawable.shape_image_index_white);
            imageIndexs.add(iv1);
            imageIndexLl.addView(iv1);
        }
        vp.setAdapter(pagerAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetAllImageIndex();
                imageIndexs.get(position).setImageResource(R.drawable.shape_image_index_gray);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter = new MyListAdapter();
        homePageLv.addHeaderView(header);
        homePageLv.setAdapter(adapter);

        return view;
    }

    private void resetAllImageIndex() {
        for (ImageView imageIndex : imageIndexs) {
            imageIndex.setImageResource(R.drawable.shape_image_index_white);
        }
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(R.drawable.f1);
            return iv;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
