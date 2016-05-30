package com.zrodo.fsclz.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import com.zrodo.fsclz.activity.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ZRDUtils {

	public final static String CoorType_GCJ02 = "gcj02";
	public final static String CoorType_BD09LL= "bd09ll";
	public final static String CoorType_BD09MC= "bd09";
	public static float[] EARTH_WEIGHT = {0.1f,0.2f,0.4f,0.6f,0.8f}; // 推算计算权重_地球
	public static final String PREFS_FILE = "zrdmobile_preference";
	public static final String LAUNCHED = "LAUNCHED";
	private static Dialog mProgressDialog;
	private static DelayOperController mDelayOper;
	
	/**
     * Tosat消息提示框
     */
    public static void alert(Context mContext, ActivityUtil.MsgDuration msgDuration,CharSequence msg){
        switch (msgDuration){
            case SHORT:
                ActivityUtil.shortToast(mContext,msg);
                break;
            case LONG:
                ActivityUtil.longToast(mContext,msg);
                break;
            default:
                break;
        }
    }
    
    /**
     * Tosat消息提示框_R文件方式 
     */
    public static void alert(Context mContext, ActivityUtil.MsgDuration msgDuration,int res){
        CharSequence msg = mContext.getString(res);
    	alert(mContext, msgDuration, msg);
    }
    
    //SharedPreferences缓存机制
    public static boolean isLaunched (Context c) {
		SharedPreferences preference = c.getSharedPreferences(PREFS_FILE, 0);
		return preference.getBoolean(LAUNCHED, false);
	}
	
	public static void setLaunched (Context c, boolean flag) {
		SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE, 0);
		prefs.edit().putBoolean(LAUNCHED, flag).commit();
	}
	
    //show ProgressDialog by Resource strings
	public static void showProgressDialog(Context c,int res){
		String msg = c.getString(res);
		showProgressDialog(c, msg);
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static String getTimes(String times){
		return new SimpleDateFormat(times).format(new Date());
	}
	
	//show ProgressDialog by string msg(file R)
	public static void showProgressDialog(Context c,String msg){
		try {
			dismissProgressDialog(); 
			mProgressDialog = CustomProgress.show(c, msg, true, null);
			mDelayOper = new DelayOperController(c);
			mDelayOper.timer.schedule(mDelayOper, 30000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//意圖(Intent)公共類
	@SuppressWarnings("static-access")
	public static void CommIntent(Context context,Bundle data, Class<?> cls) {
		Intent intent = new Intent();
		intent.addFlags(intent.FLAG_ACTIVITY_NO_USER_ACTION);
		intent.setClass(context, cls);
		if (data == null) {
			context.startActivity(intent);
		} else {
			intent.putExtras(data);
			context.startActivity(intent);
		}

	}
	
	//全角显示长文本（将textview中的字符全角化）
	public static String ToDBC(String input) {  
		   char[] c = input.toCharArray();  
		   for (int i = 0; i< c.length; i++) {  
		       if (c[i] == 12288) {  
		         c[i] = (char) 32;  
		         continue;  
		       }if (c[i]> 65280&& c[i]< 65375)  
		          c[i] = (char) (c[i] - 65248);  
		       }  
		   return new String(c);  
	}  
	
	public static void dismissProgressDialog () {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			if (mDelayOper != null) {
				mDelayOper.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ProgressDialog timeout info
    @SuppressLint("HandlerLeak") 
    static class DelayOperController extends TimerTask {
    	public Timer timer;
    	private Context c;
    	
    	public DelayOperController (Context c) {
    		timer = new Timer();
    		this.c = c;
    	}
    	
		@Override
		public void run() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
				handler.sendEmptyMessage(0);  
			}
		}
		
		private Handler handler = new Handler() {  
	    	  
	        @Override  
	        public void handleMessage(Message msg) {  
	            // TODO Auto-generated method stub  
	        	Toast.makeText(c, R.string.socket_connection, Toast.LENGTH_SHORT).show();
	        }  
	          
	    };  
	    
    }
    
    public static boolean isPwd(String strNewPwd) {
		String strPattern = "^[A-Za-z0-9]+$";
		boolean result = Pattern.matches(strPattern, strNewPwd);
		return result;
	}
    
    /**
     * 转换成小图片
     * @return
     */
    public static Bitmap convert2SmallBitmap(String photoPath,int mPhotoSizeInKb){
        //将图片压缩,大小在mPhotoSizeInKb左右。
        byte[] data = BitmapUtil.scaleAndCompress(photoPath, 460, 800, mPhotoSizeInKb);
        Bitmap resizedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (resizedBitmap.getWidth() > resizedBitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.setRotate(90);

            resizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight(), matrix, true);
        }
        return resizedBitmap;
    }
    
   /**
     * 创建圆角图片
     */
   public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
   }
   
}
