package com.android.tedcoder.material;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.tedcoder.wkvideoplayer.dlna.engine.DLNAContainer;
import com.android.tedcoder.wkvideoplayer.dlna.service.DLNAService;
import com.android.tedcoder.wkvideoplayer.model.Video;
import com.android.tedcoder.wkvideoplayer.model.VideoUrl;
import com.android.tedcoder.wkvideoplayer.util.DensityUtil;
import com.android.tedcoder.wkvideoplayer.view.MediaController;
import com.android.tedcoder.wkvideoplayer.view.SuperVideoPlayer;

import java.util.ArrayList;

/**
 * 视频播放
 * Created by kjh08490 on 2016/3/17.
 */
public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private ImageView play_btn;
    private SuperVideoPlayer video_player_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        play_btn = (ImageView) findViewById(R.id.play_btn);
        video_player_item = (SuperVideoPlayer) findViewById(R.id.video_player_item);

        play_btn.setOnClickListener(this);
        video_player_item.setVideoPlayCallback(mVideoPlayCallback);

        startDLNAService();
    }

    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            video_player_item.close();
            play_btn.setVisibility(View.VISIBLE);
            video_player_item.setVisibility(View.GONE);
            resetPageToPortrait();
        }

        @Override
        public void onSwitchPageType() {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                video_player_item.setPageType(MediaController.PageType.SHRINK);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                video_player_item.setPageType(MediaController.PageType.EXPAND);
            }
        }

        @Override
        public void onPlayFinish() {

        }
    };


    private void initVideoDate() {
        play_btn.setVisibility(View.GONE);
        video_player_item.setVisibility(View.VISIBLE);
        video_player_item.setAutoHideController(false);

        Video video = new Video();
        VideoUrl videoUrl1 = new VideoUrl();
        videoUrl1.setFormatName("720P");
        videoUrl1.setFormatUrl("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");
        VideoUrl videoUrl2 = new VideoUrl();
        videoUrl2.setFormatName("480P");
        videoUrl2.setFormatUrl("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");
        ArrayList<VideoUrl> arrayList1 = new ArrayList<>();
        arrayList1.add(videoUrl1);
        arrayList1.add(videoUrl2);
        video.setVideoName("测试视频一");
        video.setVideoUrl(arrayList1);

        Video video2 = new Video();
        VideoUrl videoUrl3 = new VideoUrl();
        videoUrl3.setFormatName("720P");
        videoUrl3.setFormatUrl("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");
        VideoUrl videoUrl4 = new VideoUrl();
        videoUrl4.setFormatName("480P");
        videoUrl4.setFormatUrl("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");
        ArrayList<VideoUrl> arrayList2 = new ArrayList<>();
        arrayList2.add(videoUrl3);
        arrayList2.add(videoUrl4);
        video2.setVideoName("测试视频二");
        video2.setVideoUrl(arrayList2);

        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);
        videoArrayList.add(video2);

        video_player_item.loadMultipleVideo(videoArrayList, 0, 0, 0);
    }

    /***
     * 恢复屏幕至竖屏
     */
    private void resetPageToPortrait() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            video_player_item.setPageType(MediaController.PageType.SHRINK);
        }
    }


    private void startDLNAService() {
        // Clear the device container.
        DLNAContainer.getInstance().clear();
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        startService(intent);
    }

    private void stopDLNAService() {
        Intent intent = new Intent(getApplicationContext(), DLNAService.class);
        stopService(intent);
    }


    /***
     * 旋转屏幕之后回调
     *
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (null == video_player_item) return;
        /***
         * 根据屏幕方向重新设置播放器的大小
         */
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().invalidate();
            float height = DensityUtil.getWidthInPx(this);
            float width = DensityUtil.getHeightInPx(this);
            video_player_item.getLayoutParams().height = (int) width;
            video_player_item.getLayoutParams().width = (int) height;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            video_player_item.getLayoutParams().height = (int) height;
            video_player_item.getLayoutParams().width = (int) width;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                initVideoDate();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDLNAService();
    }
}
