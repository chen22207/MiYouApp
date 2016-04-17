package com.firstblood.miyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.publish.PublishActivity1;
import com.firstblood.miyo.activity.user.UserInfoCompleteActivity;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.User;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.util.RxBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chenshuai12619 on 2016/4/5 10:01.
 */
public class MineFragment extends Fragment {

    @InjectView(R.id.mine_publish_bt)
    Button minePublishBt;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @InjectView(R.id.mine_header_iv)
    CircleImageView mMineHeaderIv;
    @InjectView(R.id.mine_username_tv)
    TextView mMineUsernameTv;
    @InjectView(R.id.mine_edit_info_tv)
    TextView mMineEditInfoTv;
    @InjectView(R.id.mine_publish_tv)
    TextView mMinePublishTv;
    @InjectView(R.id.mine_setting_tv)
    TextView mMineSettingTv;

    private RxBus bus;
	private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.inject(this, v);
	    user = (User) SpUtils.getInstance().getModule(SpDictionary.SP_USER);
	    if (user != null) {
		    CommonUtils.loadHeadImage(getActivity(), user, mMineHeaderIv);
		    mMineUsernameTv.setText(user.getNickName());
	    }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.mine_header_iv, R.id.mine_username_tv, R.id.mine_edit_info_tv, R.id.mine_publish_tv, R.id.mine_setting_tv, R.id.mine_publish_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_header_iv:
                break;
            case R.id.mine_username_tv:
                break;
            case R.id.mine_edit_info_tv:
                getActivity().startActivity(new Intent(getActivity(), UserInfoCompleteActivity.class));
                break;
            case R.id.mine_publish_tv:
                break;
            case R.id.mine_setting_tv:

	            break;
            case R.id.mine_publish_bt:
                startActivity(new Intent(getActivity(), PublishActivity1.class));
                break;
        }
    }
}
