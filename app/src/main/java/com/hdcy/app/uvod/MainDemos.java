package com.hdcy.app.uvod;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.uvod.preference.SettingsActivity;
import com.ucloud.common.util.SystemUtil;


public class MainDemos extends AppCompatActivity implements OnItemClickListener{
	String[] demoDirects;
	String[] demoNames;
	public static final String TAG = "MainDemos";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ListView mListView = (ListView) findViewById(R.id.listview);
		demoNames = getResources().getStringArray(R.array.uvod_demoNames);
		mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, demoNames));
		mListView.setOnItemClickListener(this);
		demoDirects = getResources().getStringArray(R.array.uvod_demoDirects);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(!SystemUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "当前网络不可用.", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (SystemUtil.getConnectedType(this)) {
			case ConnectivityManager.TYPE_MOBILE:
				Toast.makeText(this, "当前网络: mobile", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_ETHERNET:
				Toast.makeText(this, "当前网络: ehternet", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_WIFI:
				Toast.makeText(this, "当前网络: wifi", Toast.LENGTH_SHORT).show();
				break;
		}

		if (demoDirects != null && demoDirects.length > position && !TextUtils.isEmpty(demoDirects[position].trim())) {
			Intent intent = new Intent();
			intent.setAction(demoDirects[position]);
			intent.putExtra("title", demoNames[position]);
			intent.putExtra("videoPath", "http://mediademo.ufile.ucloud.com.cn/ucloud_promo_140s.mp4");
//			intent.putExtra("videoPath", "http://ulive-record.ufile.ucloud.com.cn/101841470662011.m3u8");
//			intent.putExtra("videoPath", "http://uc-hls.ufile.ucloud.cn/1470744319684927_05v3.m3u8");
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				SettingsActivity.intentTo(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
