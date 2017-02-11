package com.example.devapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	/**
	 * 测试使用
	 *
	 * @param view
	 */
	public void toTestActivity(View view) {
		try {
			Intent intent = new Intent();
			intent.setAction("hxqc.dev.devActivity");
			MainActivity.this.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			showDialog();
		}
	}

	/**
	 * 开发使用
	 *
	 * @param view
	 */
	public void toDevActivity(View view) {
//		Intent intent = new Intent(this, WebActivity.class);
//		startActivity(intent);
		try {
			Intent intent = new Intent();
			intent.setAction("hxqc.dev.testActivity");
			MainActivity.this.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			showDialog();
		}
	}

	public void showDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("未安装\"恒信汽车\"，去下载安装");
		builder.setTitle("提示").setPositiveButton("下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri uri = Uri.parse("http://www.pgyer.com/hxqcbeta");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


}
