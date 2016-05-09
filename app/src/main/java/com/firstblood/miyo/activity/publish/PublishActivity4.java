package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.MapActivity;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Subscription;

public class PublishActivity4 extends AppCompatActivity {

	@InjectView(R.id.publish_price_et)
	EditText publishPriceEt;
	@InjectView(R.id.publish_price_location_tv)
	TextView publishPriceLocationTv;

	private HashMap<String, Object> dataMap;

	private RxBus bus;
	private Subscription mSubscription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish4);
		ButterKnife.inject(this);
		dataMap = (HashMap<String, Object>) getIntent().getSerializableExtra("dataMap");
		Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish4)).setRight(getString(R.string.publish_next), v -> {
			Intent intent = new Intent(this, PublishActivity5.class);
			setData();
			intent.putExtra("dataMap", dataMap);
			startActivity(intent);
		});
		publishPriceLocationTv.setOnClickListener(v -> startActivityForResult(new Intent(PublishActivity4.this, MapActivity.class), 1));

		bus = RxBus.getInstance();
		mSubscription = bus.toObserverable(PublishActivity5.publishSucceed.class).subscribe(publishSucceed -> finish());
	}

	private void setData() {
		dataMap.put("price", publishPriceEt.getText().toString());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				dataMap.put("address_x", data.getDoubleExtra("x", 0.00));
				dataMap.put("address_y", data.getDoubleExtra("y", 0.00));
				dataMap.put("address", data.getStringExtra("address"));
				dataMap.put("addressname", data.getStringExtra("addressName"));
				publishPriceLocationTv.setText(data.getStringExtra("addressName"));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!mSubscription.isUnsubscribed()) {
			mSubscription.unsubscribe();
		}
	}
}
