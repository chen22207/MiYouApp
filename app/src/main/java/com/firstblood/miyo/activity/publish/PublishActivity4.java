package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PublishActivity4 extends AppCompatActivity {

	@InjectView(R.id.publish_price_et)
	EditText publishPriceEt;
	@InjectView(R.id.publish_price_city_et)
	EditText publishPriceCityEt;
	@InjectView(R.id.publish_price_location_et)
	EditText publishPriceLocationEt;

	private HashMap<String, Object> dataMap;

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
	}

	private void setData() {
		dataMap.put("price", publishPriceEt.getText().toString());

	}
}
