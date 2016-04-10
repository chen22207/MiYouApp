package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;

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

    private int shi = 1;//室
    private int ting = 1;//厅
    private int wei = 1;//卫
    private int tai = 1;//阳台

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish2);
        ButterKnife.inject(this);

        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish2)).setRight(getString(R.string.publish_next), v -> {
            Intent intent = new Intent(this, PublishActivity3.class);
            startActivity(intent);
        });
    }

    @OnClick({R.id.publish_type_shi_jia_iv, R.id.publish_type_shi_jian_iv, R.id.publish_type_ting_jia_iv, R.id.publish_type_ting_jian_iv, R.id.publish_type_wei_jia_iv, R.id.publish_type_wei_jian_iv, R.id.publish_type_yangtai_jia_iv, R.id.publish_type_yangtai_jian_iv,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_type_shi_jia_iv:
                shi++;
                publishTypeShiNumberTv.setText(shi + "");
                break;
            case R.id.publish_type_shi_jian_iv:
                if (shi > 0) {
                    shi--;
                    publishTypeShiNumberTv.setText(shi + "");
                }
                break;
            case R.id.publish_type_ting_jia_iv:
                ting++;
                publishTypeTingNumberTv.setText(ting + "");
                break;
            case R.id.publish_type_ting_jian_iv:
                if (ting > 0) {
                    ting--;
                }
                publishTypeTingNumberTv.setText(ting + "");
                break;
            case R.id.publish_type_wei_jia_iv:
                wei++;
                publishTypeWeiNumberTv.setText(wei + "");
                break;
            case R.id.publish_type_wei_jian_iv:
                if (wei > 0) {
                    wei--;
                }
                publishTypeWeiNumberTv.setText(wei + "");
                break;
            case R.id.publish_type_yangtai_jia_iv:
                tai++;
                publishTypeYangtaiNumberTv.setText(tai + "");
                break;
            case R.id.publish_type_yangtai_jian_iv:
                if (tai > 0) {
                    tai--;
                }
                publishTypeYangtaiNumberTv.setText(tai + "");
                break;
        }
    }
}
