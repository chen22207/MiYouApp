package com.firstblood.miyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.firstblood.miyo.fragment.HomePageFragment;
import com.firstblood.miyou.R;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.main_fl)
    FrameLayout mainFl;
    @InjectView(R.id.main_rg)
    RadioGroup mainRg;

    private FragmentManager fragmentManager;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String[] FRAGMENT_TAG = {"首页", "收藏", "消息", "我"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        fragmentManager = getSupportFragmentManager();
        initEvent();
        mainRg.check(R.id.main_tab_home_rb);


    }

    private void initEvent() {
        RxRadioGroup.checkedChanges(mainRg).subscribe(integer -> {
            hideAllFragment();
            switch (integer) {
                case R.id.main_tab_home_rb:
                    Fragment f = fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
                    if (f != null) {
                        fragmentManager.beginTransaction().show(f).commit();
                    } else {
                        HomePageFragment homePageFragment = HomePageFragment.newInstance();
                        fragmentList.add(homePageFragment);
                        fragmentManager.beginTransaction().add(R.id.main_fl, homePageFragment, FRAGMENT_TAG[0]).commit();
                    }
                    break;
                case R.id.main_1_rb:
                    Fragment f1 = fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
                    if (f1 != null) {
                        fragmentManager.beginTransaction().show(f1).commit();
                    } else {
                        HomePageFragment homePageFragment = HomePageFragment.newInstance();
                        fragmentList.add(homePageFragment);
                        fragmentManager.beginTransaction().add(R.id.main_fl, homePageFragment, FRAGMENT_TAG[1]).commit();
                    }
                    break;
                case R.id.main_2_rb:
                    Fragment f2 = fragmentManager.findFragmentByTag(FRAGMENT_TAG[2]);
                    if (f2 != null) {
                        fragmentManager.beginTransaction().show(f2).commit();
                    } else {
                        HomePageFragment homePageFragment = HomePageFragment.newInstance();
                        fragmentList.add(homePageFragment);
                        fragmentManager.beginTransaction().add(R.id.main_fl, homePageFragment, FRAGMENT_TAG[2]).commit();
                    }
                    break;
                case R.id.main_tab_mine_rb:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
            }
        });

    }

    private void hideAllFragment() {
        for (Fragment fragment : fragmentList) {
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
    }

}
