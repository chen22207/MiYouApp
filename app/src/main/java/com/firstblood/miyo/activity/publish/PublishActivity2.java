package com.firstblood.miyo.activity.publish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PublishActivity2 extends AppCompatActivity {

    @InjectView(R.id.publish_type_shi_jia_iv)
    ImageView publishTypeShiJiaIv;
    @InjectView(R.id.publish_type_shi_number_tv)
    TextView publishTypeShiNumberTv;
    @InjectView(R.id.publish_type_shi_jian_iv)
    ImageView publishTypeShiJianIv;
    @InjectView(R.id.publish_type_ting_jia_iv)
    ImageView publishTypeTingJiaIv;
    @InjectView(R.id.publish_type_ting_number_tv)
    TextView publishTypeTingNumberTv;
    @InjectView(R.id.publish_type_ting_jian_iv)
    ImageView publishTypeTingJianIv;
    @InjectView(R.id.publish_type_wei_jia_iv)
    ImageView publishTypeWeiJiaIv;
    @InjectView(R.id.publish_type_wei_number_tv)
    TextView publishTypeWeiNumberTv;
    @InjectView(R.id.publish_type_wei_jian_iv)
    ImageView publishTypeWeiJianIv;
    @InjectView(R.id.publish_type_yangtai_jia_iv)
    ImageView publishTypeYangtaiJiaIv;
    @InjectView(R.id.publish_type_yangtai_number_tv)
    TextView publishTypeYangtaiNumberTv;
    @InjectView(R.id.publish_type_yangtai_jian_iv)
    ImageView publishTypeYangtaiJianIv;
    @InjectView(R.id.publish_house_type_next_bt)
    Button publishHouseTypeNextBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish2);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.publish_type_shi_jia_iv, R.id.publish_type_shi_jian_iv, R.id.publish_type_ting_jia_iv, R.id.publish_type_ting_jian_iv, R.id.publish_type_wei_jia_iv, R.id.publish_type_wei_jian_iv, R.id.publish_type_yangtai_jia_iv, R.id.publish_type_yangtai_jian_iv, R.id.publish_house_type_next_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_type_shi_jia_iv:
                break;
            case R.id.publish_type_shi_jian_iv:
                break;
            case R.id.publish_type_ting_jia_iv:
                break;
            case R.id.publish_type_ting_jian_iv:
                break;
            case R.id.publish_type_wei_jia_iv:
                break;
            case R.id.publish_type_wei_jian_iv:
                break;
            case R.id.publish_type_yangtai_jia_iv:
                break;
            case R.id.publish_type_yangtai_jian_iv:
                break;
            case R.id.publish_house_type_next_bt:
                break;
        }
    }
}
