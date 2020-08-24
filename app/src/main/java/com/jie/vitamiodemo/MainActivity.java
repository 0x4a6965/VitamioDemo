package com.jie.vitamiodemo;

import androidx.appcompat.app.AppCompatActivity;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "System";

	private VideoView mVvVideo;
	private SeekBar mSbBar;
	private ImageView mIvControl;
	private TextView mTvVideoTime;

	//	private String mVideoPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//	private String mVideoPath = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";
	private String mVideoPath = "http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4";
	private String mVideoAllDuration; // 总播放时长
	private VitamioManager mVitamioManager;
	private boolean mIsFinish = false; // 视频是否播放完毕

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Vitamio.isInitialized(getApplicationContext()); // 初始化Vitamio

		mVvVideo = findViewById(R.id.vv_video);
		mSbBar = findViewById(R.id.sb_bar);
		mIvControl = findViewById(R.id.iv_control);
		mTvVideoTime = findViewById(R.id.tv_video_time);

		initVideo();
	}

	private void initVideo() {
		mVitamioManager = new VitamioManager(mVvVideo);
		mVitamioManager.setVideoPath(mVideoPath); // 设置播放路径

		// 控制播放/暂停
		mIvControl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mVitamioManager.isPlaying()) {
					setIvControl(true);
					mVitamioManager.pause();
				} else {
					// 如果视频播放完毕就重新播放，没播放完就继续播放
					if (mIsFinish) {
						mVitamioManager.resume();
						mIsFinish = false;
					} else {
						mVitamioManager.start();
					}
					setIvControl(false);
				}
			}
		});

		// 视频加载完成监听
		mVitamioManager.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.i(TAG, "onPrepared: 视频加载完毕");
				mVitamioManager.setPlaybackSpeed(mp, 1.0f); // 设置播放速度
				long allDuration = mVitamioManager.getDuration(); // 获取视频总时长
				mVideoAllDuration = Utils.formatTime(allDuration); // 将视频总时长转换成 h:m:s
				mSbBar.setMax((int) allDuration); // 设置进度
				mVitamioManager.start(); // 播放视频
				setIvControl(false);
			}
		});

		// 更新进度条
		mVitamioManager.setOnVideoProgress(new VitamioManager.OnVideoProgressListener() {
			@Override
			public void onProgress(long progress) {
				mSbBar.setProgress((int) progress);
				mTvVideoTime.setText(Utils.formatTime(progress) + "/" + mVideoAllDuration);
			}
		});

		// 视频播放完成
		mVitamioManager.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.i(TAG, "onCompletion: 播放完毕");
				mIsFinish = true;
				setIvControl(true);
			}
		});

		// 处理错误
		mVitamioManager.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				setIvControl(true);
				Log.e(TAG, "onError: what = " + what + ", extra = " + extra);
				return false;
			}
		});

		// 拖动进度条
		mSbBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				setIvControl(false);
				mVitamioManager.seekTo(mSbBar.getProgress()); // 播放进度条拖动完毕的视频位置
			}
		});
	}

	/**
	 * 设置播放/暂停图标
	 */
	private void setIvControl(boolean stopPlay) {
		int resId = stopPlay ? R.drawable.mediacontroller_play : R.drawable.mediacontroller_pause;
		if (mIvControl != null) mIvControl.setImageResource(resId);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mVitamioManager != null) {
			mVitamioManager.stopPlayback(); // 停止播放
		}
	}
}
