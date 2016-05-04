package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishActivity1 extends AppCompatActivity {

	private HashMap<String, String> dataMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish1);
        ButterKnife.inject(this);
        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish1));
	    dataMap = new HashMap<>();
    }

    @OnClick({R.id.publish_zhengzu_rl, R.id.publish_hezu_rl})
    public void onClick(View view) {
        Intent intent = new Intent(PublishActivity1.this, PublishActivity2.class);
        switch (view.getId()) {
            case R.id.publish_zhengzu_rl:
	            dataMap.put("isflatshare", "1");
	            break;
	        case R.id.publish_hezu_rl:
	            dataMap.put("isflatshare", "2");
		        break;
        }
	    intent.putExtra("dataMap", dataMap);
	    startActivity(intent);
    }
}
