package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;

public class PublishActivity3 extends AppCompatActivity {

    @InjectView(R.id.publish_match_kuandai)
    CheckBox publishMatchKuandai;
    @InjectView(R.id.publish_match_xiyiji)
    CheckBox publishMatchXiyiji;
    @InjectView(R.id.publish_match_dianshi)
    CheckBox publishMatchDianshi;
    @InjectView(R.id.publish_match_bingxiang)
    CheckBox publishMatchBingxiang;
    @InjectView(R.id.publish_match_reshuiqi)
    CheckBox publishMatchReshuiqi;
    @InjectView(R.id.publish_match_kongtiao)
    CheckBox publishMatchKongtiao;
    @InjectView(R.id.publish_match_menjin)
    CheckBox publishMatchMenjin;
    @InjectView(R.id.publish_match_dianti)
    CheckBox publishMatchDianti;
    @InjectView(R.id.publish_match_tingche)
    CheckBox publishMatchTingche;
    @InjectView(R.id.publish_match_yugang)
    CheckBox publishMatchYugang;
    @InjectView(R.id.publish_match_chongwu)
    CheckBox publishMatchChongwu;
    @InjectView(R.id.publish_match_chouyan)
    CheckBox publishMatchChouyan;
    @InjectView(R.id.publish_match_jucan)
    CheckBox publishMatchJucan;

	private HashMap<String, Object> dataMap;
	private RxBus bus;
	private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish3);
        ButterKnife.inject(this);
	    dataMap = (HashMap<String, Object>) getIntent().getSerializableExtra("dataMap");
	    Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish3)).setRight(getString(R.string.publish_next), v -> {
            Intent intent = new Intent(this, PublishActivity4.class);
	        setData();
	        intent.putExtra("dataMap", dataMap);
		    startActivity(intent);
        });
	    bus = RxBus.getInstance();
	    mSubscription = bus.toObserverable(PublishActivity5.publishSucceed.class).subscribe(publishSucceed -> finish());
    }

	private void setData() {
		dataMap.put("wifi", publishMatchKuandai.isChecked() ? 1 : 0);
		dataMap.put("heater", publishMatchReshuiqi.isChecked() ? 1 : 0);
		dataMap.put("television", publishMatchDianshi.isChecked() ? 1 : 0);
		dataMap.put("airconditioner", publishMatchKongtiao.isChecked() ? 1 : 0);
		dataMap.put("refrigerator", publishMatchBingxiang.isChecked() ? 1 : 0);
		dataMap.put("washingmachine", publishMatchXiyiji.isChecked() ? 1 : 0);
		dataMap.put("elevator", publishMatchDianti.isChecked() ? 1 : 0);
		dataMap.put("accesscontrol", publishMatchMenjin.isChecked() ? 1 : 0);
		dataMap.put("parkingspace", publishMatchTingche.isChecked() ? 1 : 0);
		dataMap.put("smoking", publishMatchChouyan.isChecked() ? 1 : 0);
		dataMap.put("bathtub", publishMatchYugang.isChecked() ? 1 : 0);
		dataMap.put("keepingpets", publishMatchChongwu.isChecked() ? 1 : 0);
		dataMap.put("paty", publishMatchJucan.isChecked() ? 1 : 0);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!mSubscription.isUnsubscribed()) {
			mSubscription.unsubscribe();
		}
	}
}
