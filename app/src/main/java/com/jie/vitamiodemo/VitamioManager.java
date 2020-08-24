package com.jie.vitamiodemo;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VitamioManager {

	private static final int PROGRESS = 0x01;

	private VideoView mVideoView;
	private OnVideoProgressListener mVideoProgressListener;

	// 更新进度条
	private Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(@NonNull Message msg) {
			switch (msg.what) {
				case PROGRESS:
					if (mVideoProgressListener != null) {
						mVideoProgressListener.onProgress(getCurrentPosition());
						mHandler.sendEmptyMessageDelayed(PROGRESS, 1000);
					}
					break;
			}
			return false;
		}
	});

	public interface OnVideoProgressListener {
		void onProgress(long progress);
	}

	public VitamioManager(VideoView mVideoView) {
		this.mVideoView = mVideoView;
	}

	/**
	 * 设置文件路径
	 */
	public void setVideoPath(String path) {
		mVideoView.setVideoPath(path);
	}

	/**
	 * 设置网络路径
	 */
	public void setVideoURI(Uri uri) {
		mVideoView.setVideoURI(uri);
	}

	/**
	 * 设置播放速度 [0.5 - 2.0]
	 */
	public void setPlaybackSpeed(MediaPlayer mediaPlayer, float speed) {
		mediaPlayer.setPlaybackSpeed(speed);
	}

	/**
	 * 获取当前视频播放的位置
	 */
	public long getCurrentPosition() {
		return mVideoView.getCurrentPosition();
	}

	/**
	 * 获取当前视频总长度
	 * @return
	 */
	public long getDuration() {
		return mVideoView.getDuration();
	}

	/**
	 * 是否播放
	 * @return
	 */
	public boolean isPlaying() {
		return mVideoView.isPlaying();
	}

	/**
	 * 播放
	 */
	public void start() {
		mVideoView.start();
		mHandler.sendEmptyMessage(PROGRESS);
	}

	/**
	 * 暂停
	 */
	public void pause() {
		mVideoView.pause();
	}

	/**
	 * 重新播放
	 */
	public void resume() {
		mVideoView.seekTo(0);
	}

	/**
	 * 停止播放 释放资源
	 */
	public void stopPlayback() {
		mVideoView.stopPlayback();
		mHandler.removeMessages(PROGRESS);
	}

	/**
	 * 从第几毫秒开始播放
	 */
	public void seekTo(int msec) {
		mVideoView.seekTo(msec);
	}

	/**
	 * 视频播放完毕的监听
	 */
	public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
		mVideoView.setOnCompletionListener(listener);
	}

	/**
	 * 发生错误的监听
	 */
	public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
		mVideoView.setOnErrorListener(listener);
	}

	/**
	 * 视频加载完成的监听
	 */
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
		mVideoView.setOnPreparedListener(listener);
	}

	/**
	 * 视频进度监听
	 */
	public void setOnVideoProgress(OnVideoProgressListener listener) {
		this.mVideoProgressListener = listener;
	}
}
