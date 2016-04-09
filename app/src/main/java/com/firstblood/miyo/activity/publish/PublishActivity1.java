package com.firstblood.miyo.activity.publish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PublishActivity1 extends AppCompatActivity {

    @InjectView(R.id.publish_zhengzu_rl)
    RelativeLayout publishZhengzuRl;
    @InjectView(R.id.publish_hezu_rl)
    RelativeLayout publishHezuRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish1);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.publish_zhengzu_rl, R.id.publish_hezu_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.publish_zhengzu_rl:
                break;
            case R.id.publish_hezu_rl:
                break;
        }
    }
}
