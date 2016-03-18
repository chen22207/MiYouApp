package com.firstblood.miyo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.firstblood.miyo.fragment.HomePageFragment;
import com.firstblood.miyou.R;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.main_vp)
    ViewPager mainVp;
    @InjectView(R.id.main_rg)
    RadioGroup mainRg;

    private Fragment[] fragments = {
            HomePageFragment.newInstance(),
            HomePageFragment.newInstance(),
            HomePageFragment.newInstance(),
            HomePageFragment.newInstance()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initEvent();
        mainRg.check(R.id.main_tab_home_rb);

    }

    private void initEvent() {
        RxRadioGroup.checkedChanges(mainRg).subscribe(integer -> {
            switch (integer) {
                case R.id.main_tab_home_rb:
                    mainVp.setCurrentItem(0);
                    break;
            }
        });
        mainVp.setAdapter(new MyPageChangeAdapter(getSupportFragmentManager()));

    }

    @OnPageChange(R.id.main_vp)
    void onPageSelected(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;

        }
    }

    private class MyPageChangeAdapter extends FragmentPagerAdapter {

        public MyPageChangeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
