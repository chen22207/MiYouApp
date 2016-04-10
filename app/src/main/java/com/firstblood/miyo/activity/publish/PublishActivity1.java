package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;

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
        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish1));
    }

    @OnClick({R.id.publish_zhengzu_rl, R.id.publish_hezu_rl})
    public void onClick(View view) {
        Intent intent = new Intent(PublishActivity1.this, PublishActivity2.class);
        switch (view.getId()) {
            case R.id.publish_zhengzu_rl:
                break;
            case R.id.publish_hezu_rl:
                break;
        }
        startActivity(intent);
    }
}
