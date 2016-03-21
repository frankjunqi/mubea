package com.android.tedcoder.material;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
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

    private SuperVideoPlayer video_player_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video_player_item = (SuperVideoPlayer) findViewById(R.id.video_player_item);
        video_player_item.setVideoPlayCallback(mVideoPlayCallback);
        startDLNAService();
        initVideoDate();
    }

    private SuperVideoPlayer.VideoPlayCallbackImpl mVideoPlayCallback = new SuperVideoPlayer.VideoPlayCallbackImpl() {
        @Override
        public void onCloseVideo() {
            video_player_item.close();
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
        video_player_item.setAutoHideController(true);

        Video video = new Video();

        // 视频的列表
        ArrayList<VideoUrl> videoList = new ArrayList<>();
        // 创建 video url
        VideoUrl videoUrl = new VideoUrl();
        videoUrl.setFormatName("480P");
        videoUrl.setFormatUrl("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");
        videoList.add(videoUrl);


        // 设置视频的Name
        video.setVideoName("测试视频一");
        video.setVideoUrl(videoList);

        // 需要播放的 video 对象的列表
        ArrayList<Video> videoArrayList = new ArrayList<>();
        videoArrayList.add(video);

        // 网络视频
        video_player_item.loadMultipleVideo(videoArrayList, 0, 0, 0);
        // 本地视频
        // video_player_item.loadLocalVideo("/sdcard/muber/haikou.mp4");
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
            // 当切换到ORIENTATION_PORTRAIT 的屏幕的时候，默认是关闭视频播放
            this.finish();

            /*
            正常是横竖屏切换的时候，video view 的自适应
            final WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            float width = DensityUtil.getWidthInPx(this);
            float height = DensityUtil.dip2px(this, 200.f);
            video_player_item.getLayoutParams().height = (int) height;
            video_player_item.getLayoutParams().width = (int) width;*/
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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

    private String isSdCrad() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();//获取跟目录
        }
        return "";
    }
}
