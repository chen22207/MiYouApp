package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish3);
        ButterKnife.inject(this);
        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish3)).setRight(getString(R.string.publish_next), v -> {
            Intent intent = new Intent(this, PublishActivity4.class);
            startActivity(intent);
        });
    }
}
