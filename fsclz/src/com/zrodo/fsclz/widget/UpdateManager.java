package com.zrodo.fsclz.widget;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.zrodo.fsclz.activity.R;
import com.zrodo.fsclz.service.Constant;
import com.zrodo.fsclz.service.ZRDApplication;
import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

public class UpdateManager {
	private Context mContext;
	//更新数据
	HashMap<String, String> mHashMap;
	
	private String apkName = "";
	
	//下载中....
	private static final int DOWNLOAD = 1;
	//下载完成
	private static final int DOWNLOAD_FINISH = 2;

	//记录进度条数量
	private int progress;
	//是否取消更新
	private boolean cancelUpdate = false;
	//private boolean touch = false;
	//进度条
	private MyProgress mProgress;
	//进度文字
	private Dialog mDownloadDialog;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		};
	};
	public UpdateManager(Context context , HashMap<String, String> mHashMap)
	{
		this.mContext = context;
		this.mHashMap = mHashMap;
		apkName = mHashMap.get("content")+".apk";
	}
	public void checkUpdate()
	{
		if (isUpdate())
		{
			showNoticeDialog();
		} 
	}
	public boolean isUpdate()
	{
		try {
			float versionCode = getVersionCode(mContext);
			if (null != mHashMap)
			{
				float serviceCode = Float.valueOf(mHashMap.get("version"));
				if (serviceCode > versionCode)
				{
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	private float getVersionCode(Context context)
	{
		float versionCode = 0;
		try
		{
			versionCode = context.getPackageManager().getPackageInfo("com.zrodo.fsclz.activity", 0).versionCode;
		} 
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}
	private void showNoticeDialog()
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		builder.setPositiveButton(R.string.soft_update_update, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}
	private void showDownloadDialog()
	{
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (MyProgress) v.findViewById(R.id.update_progress);
		builder.setView(v);
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				cancelUpdate = true;
				//touch= true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		downloadApk();
	}
	private void downloadApk()
	{
		new downloadApkThread().start();
	}
	private class downloadApkThread extends Thread
	{
		public void run()
		{
			try
			{
				//判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					//获取apk下载链接，获得文件大小和连接输入流
					URL url = new URL(mHashMap.get("url"));
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					//创建文件夹和apk文件，并获得其输出流
					File file = new File(Constant.down_path);
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(Constant.down_path, apkName);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					//缓存
					byte buf[] = new byte[1024];
					//写入到文件夹中
					do
					{
						int numread = is.read(buf);
						count += numread;
						//计算进度条的位置
						progress = (int) (((float) count / length) * 100);
						//更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							//下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						//写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);//点击取消就停止下载
					fos.close();
					is.close();
					mDownloadDialog.dismiss();
					/*if (touch==false) {
						ZRDApplication.getInstance().exit();
					}*/					
				} else {
					ZRDUtils.alert(mContext, MsgDuration.LONG, "SD卡不可用");
				}
			} catch (MalformedURLException e){
				e.printStackTrace();
				ZRDUtils.alert(mContext, MsgDuration.LONG, "下载地址出错，apk下载失败！！");
			} catch (IOException e){
				e.printStackTrace();
				ZRDUtils.alert(mContext, MsgDuration.LONG, "apk下载失败！！");
			}
			mDownloadDialog.dismiss();

		}
	};
	
	private void installApk()
	{
		File apkfile = new File(Constant.down_path, apkName);
		if (!apkfile.exists())
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}