package com.zrodo.fsclz.activity;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class VideoPlayerActivity extends Activity{

	private VideoView videoView;
	private String filePath;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoview);
		Intent intent = getIntent();
		if(intent != null){
			Bundle data = intent.getExtras();
			if(data != null){
				filePath = data.getString("VIDEOPATH");
			}
		}
		
		videoView = (VideoView) findViewById(R.id.videoView);
		
		videoView.setMediaController(new MediaController(this));
		Uri videoUri = Uri.parse(filePath);
		videoView.setVideoURI(videoUri);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		videoView.setLayoutParams(layoutParams);
		videoView.start();
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
}
