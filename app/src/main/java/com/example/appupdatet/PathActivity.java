package com.example.appupdatet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bobo.utils.DiffUtils;
import com.bobo.utils.PatchUtils;
import com.example.administrator.testguid.MD5Utils;
import com.example.administrator.testguid.R;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PathActivity extends Activity {
	private String oldapk ;//老版本
	private String newapk;//新版本
	private String pathapk;//补丁
	private String complexapk;//合成的apk
	private ProgressDialog dialog;
	private static String SDCardPath = Environment.getExternalStorageDirectory()+ File.separator+"liyeyu";
	private static String TAG = "liyeyu";
	private String mOldMD5;
	private String mNewMD5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path);
		dialog = new ProgressDialog(this);
		dialog.setMessage("请稍后...");
		oldapk = SDCardPath+"/app-release_1.0.apk";
		newapk = SDCardPath+"/app-release_2.0.apk";
		pathapk = SDCardPath+"/path.apk";
		complexapk = SDCardPath + "/newApk.apk";
		mOldMD5 = MD5Utils.md5(new File(oldapk));
		mNewMD5 = MD5Utils.md5(new File(newapk));
		Log.i(TAG,""+Runtime.getRuntime().availableProcessors());
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		pool.scheduleWithFixedDelay(new Runnable() {
			int count;
			@Override
			public void run() {
				Log.i(TAG,""+count++);
			}
		},1,2, TimeUnit.SECONDS);
	}


	static {
		System.loadLibrary("diff");
	}

	public void onclick(View view) {
		switch (view.getId()) {
			case R.id.diff://拆分版本生成补丁
				dialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						DiffUtils.genDiff(oldapk, newapk, pathapk);
						Log.i(TAG,"oldapk:"+mOldMD5);
						Log.i(TAG,"newapk:"+mNewMD5);
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(PathActivity.this, "补丁生成完成。", Toast.LENGTH_LONG).show();
								dialog.dismiss();
							}
						});
					}
				}).start();

				break;
			case R.id.add: //合成新版本
				dialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						PatchUtils.patch(oldapk, complexapk, pathapk);
						final String compleMD5 = MD5Utils.md5(new File(complexapk));
						Log.i(TAG,"complexapk:"+compleMD5);
						runOnUiThread(new Runnable() {
							public void run() {
								if(mNewMD5.equals(compleMD5)){
									Toast.makeText(PathActivity.this, "新版本生成完成。",  Toast.LENGTH_LONG).show();
									dialog.dismiss();
									Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
									intent.setData(Uri.fromFile(new File(complexapk)));
									startActivity(intent);
								}else{
									Toast.makeText(PathActivity.this, "MD5校验失败",  Toast.LENGTH_LONG).show();
								}

							}
						});
					}
				}).start();

				break;

		}
	}
}
