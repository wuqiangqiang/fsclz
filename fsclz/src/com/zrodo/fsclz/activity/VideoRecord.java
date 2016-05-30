package com.zrodo.fsclz.activity;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.zrodo.fsclz.utils.ActivityUtil.MsgDuration;
import com.zrodo.fsclz.utils.ZRDUtils;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoRecord extends Activity implements OnClickListener{
    
	protected static final int RESULT_CODE = 1;
	//视频录制中的两个按钮
	private Button record , stop;
	TextView currentTimeLengthTextView, currentFileSizeTextView;
	
	//系统的视频文件
	private File videoFile;
	private MediaRecorder mRecorder;
	
	// 显示视频预览的SurfaceView  
    SurfaceView sView;  
    // 记录是否正在进行录制
	private boolean isRecording = false;
	private Handler handler;
	// 添加计时器
	private Chronometer timer;
	
	private LinearLayout lin_chronometer;
	
	private Camera camera;
	
	//控制时长定时器
	private Timer timer_timeLength;
	//控制文件大小定时器
	private Timer timer_fileSize;
		
	@Override    
	public void onCreate(Bundle savedInstanceState) {    
	        super.onCreate(savedInstanceState);    
	        setContentView(R.layout.videorecord);
	        
	        lin_chronometer = (LinearLayout) findViewById(R.id.lin_chronometer);
	        record = (Button) findViewById(R.id.record);
	        stop = (Button) findViewById(R.id.stop);
	        timer = (Chronometer) findViewById(R.id.chronometer);
	        //将计时器置于最顶层
	        lin_chronometer.bringToFront();
	        //让stop按钮不可用
	        //stop.setEnabled(false);
	        //为两相按钮的单击事件绑定监听器
	        record.setOnClickListener(this);
	        stop.setOnClickListener(this);
	     // 获取程序界面中的SurfaceView  
	        sView = (SurfaceView) this.findViewById(R.id.videoView);  
	        /** 
	         * 出现这些问题基本上都是以下两个方法参数导致的，每个手机的分辨率和预览大小支持都不一样，设置错误就会报错。 
	         */  
//	        sView.getHolder().setFixedSize(320, 280);  
	        // 设置该组件让屏幕不会自动关闭  
	        sView.getHolder().setKeepScreenOn(true);  
	                    
	        handler = new Handler(){  
	            @Override  
	            public void handleMessage(Message msg) {  
	                if(msg.what == 0x123){  
	                    stop();
	                    ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "录制视频由于已超过最长时间，自动停止！");    
	                }else if(msg.what == 0x124){  
	                    stop();
	                    ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "录制视频由于已超过设置的最大文件大小，自动停止！");
	                }  
	            }  
	        };
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		     // 单击录制按钮  
            case R.id.record:
               if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            	   ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "SD卡不存在，请插入SD卡！");      
            	   return;  
                 } 
            	 init();  
                 start();
               //启动停止录制的定时器  
                 timer_timeLength = new Timer();  

                 timer_timeLength.schedule(new EndTimerThread(), 30 * 1000);

                 break;

                 // 单击停止按钮  
            case R.id.stop:  
                // 如果正在进行录制  
                 stop();  
                 break;  
		
            default:
			     break;
		}
	}
	
	/** 
     * 初始化MediaRecoder 
     * 参数设置顺序很重要，否则出错 
     * @throws IOException  
     */
	
	private void init(){  
        // 创建保存录制视频的视频文件  
        try {  
            videoFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/myvideo.mp4");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 创建MediaPlayer对象  
        mRecorder = new MediaRecorder();  
        mRecorder.reset();  
          
        camera = Camera.open();  
        camera.setDisplayOrientation(90);  
          
        Parameters parameters = camera.getParameters();  
          
        camera.unlock();  
        mRecorder.setCamera(camera);  
          
        // 设置从麦克风采集声音  
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
        // 设置从摄像头采集图像  
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
        // 设置视频文件的输出格式  
        // 必须在设置声音编码格式、图像编码格式之前设置  
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
        //构造CamcorderProfile，使用高质量视频录制  
//        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);  
//        mRecorder.setProfile(camcorderProfile);  
        // 设置声音编码的格式  
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
        // 设置图像编码的格式  
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);  
        /** 
         *设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错 
         *且这里的分辨率必须是该camera支持的 
         */  
        List<Size> supportedVideoSizes = parameters.getSupportedVideoSizes();  
        long maxFileSize = Long.parseLong(200 * 1024 + "");//如果获取不到视频分辨率信息则默认设置个100k  
          
        if(null != supportedVideoSizes && supportedVideoSizes.size() > 0){  
            mRecorder.setVideoSize(supportedVideoSizes.get(0).width, supportedVideoSizes.get(0).height);  
            //maxFileSize = Long.parseLong(sharedPreferences.getInt("maxFileSize", 0) * 1024 * 1024 + "");
            maxFileSize = Long.parseLong(80 * 1024 * 1024 + "");  
        }else{
        	ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "无法获取视频分辨率信息，此处默认最大200K。");
            //Toast.makeText(VideoRecord.this, "无法获取视频分辨率信息，此处默认最大200K。", 200).show();  
        }  
          
        // 每秒 4帧。。设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错  
        mRecorder.setVideoFrameRate(15);  
          
        System.out.println("maxFileSize:"+maxFileSize);  
        mRecorder.setMaxFileSize(maxFileSize);  
        mRecorder.setOnInfoListener(new OnInfoListener() {  
              
            @Override  
            public void onInfo(MediaRecorder mr, int what, int extra) {  
                if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED){  
                    stop();
                    ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "录制视频由于已超过设置的最大文件大小，自动停止！");
                    //Toast.makeText(VideoRecord.this, "录制视频由于已超过设置的最大文件大小，自动停止！", 200).show();  
                }  
                  
            }  
        });  
          
          
        mRecorder.setOutputFile(videoFile.getAbsolutePath());           
        // 指定使用SurfaceView来预览视频  
        mRecorder.setPreviewDisplay(sView.getHolder().getSurface());  
    }
	
	/** 
     * 开始录制视频 
     * @throws IOException  
     * @throws IllegalStateException  
     */  
    private void start(){  
        if(!isRecording){  
            try {  
                mRecorder.prepare();  
            } catch (IllegalStateException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            // 开始录制  
            mRecorder.start();
            // 将计时器清零
            timer.setBase(SystemClock.elapsedRealtime());   
            //开始计时  
            timer.start();  
            //  让record按钮不可见,stop可见
            record.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);

            isRecording = true;
            
        }else{  
        	ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "您正在录制，请先停止录制视频！");
        }  
    }
    
    /** 
     * 停止录制视频 
     */  
    @SuppressWarnings("static-access")
	private void stop(){  
        if (isRecording){  
            // 停止录制  
            mRecorder.stop();  
            // 释放资源  
            mRecorder.release();  
            mRecorder = null;  
              
            camera.stopPreview();  
            camera.release();  
            camera = null;
            //计时器停止计时
            timer.stop(); 
            //  让record按钮可见,stop不可见
            record.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);

            isRecording = false;
            
            Intent intent = new Intent();
			intent.putExtra("VIDEOFILE", videoFile.getAbsolutePath());
			intent.addFlags(intent.FLAG_ACTIVITY_NO_USER_ACTION);
			setResult(RESULT_CODE, intent);			
			finish();
        }else{
        	ZRDUtils.alert(VideoRecord.this, MsgDuration.LONG, "对不起，您还没有开始录制视频！");
        }  
          
        if(null != timer_timeLength){  
            timer_timeLength.cancel();  
        }  
        if(null != timer_fileSize){  
            timer_fileSize.cancel();  
        }  
    }
    
    /** 
     * 停止录像定时器 
     */   
    class EndTimerThread extends TimerTask {  
        @Override    
        public void run() {    
            handler.sendEmptyMessage(0x123);  
            this.cancel();   
        }   
    }  
}
