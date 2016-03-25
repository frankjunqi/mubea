package com.android.tedcoder.material;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by kjh08490 on 2016/3/18.
 */
public class VideoPlayActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private VideoView video_view;
    public static final String TAG = "VideoPlayer";
    private Uri mUri;
    // 视频播放的短点
    private int mPositionWhenPaused = -1;

    private MediaController mMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        video_view = (VideoView) findViewById(R.id.video_view);
        mMediaController = new MediaController(this);

        // Intent 本地raw包下
        //mUri = Uri.parse("android.resource://com.android.tedcoder.material/" + R.raw.haikou);

        // Intent fileStr 调用
        //mUri = Uri.parse("file:///sdcard/Movies/haikou.mp4");

        // Intent 网络地址
        mUri = Uri.parse("http://tcw-voice.b0.upaiyun.com/ScreamVoiceResource/haikou.mp4");

        // Intent File 文件路径
        //File file = new File("/sdcard/muber/haikou.mp4");
        //mUri = Uri.fromFile(file);

        //设置MediaController
        video_view.setMediaController(mMediaController);
        video_view.setOnErrorListener(this);

    }
    //监听MediaPlayer上报的错误信息

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("onerror", what + " " + extra);
        return false;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        // Video 播完的时候得到通知 继续播放
        // 播放完成finish页面
        Toast.makeText(this, "视频播放完成.", Toast.LENGTH_LONG).show();
        this.finish();
    }


    //开始
    @Override
    public void onStart() {
        super.onStart();
        // Play Video
        video_view.setVideoURI(mUri);
        video_view.start();
    }


    //暂停
    @Override
    public void onPause() {
        // Stop video when the activity is pause.
        super.onPause();
        mPositionWhenPaused = video_view.getCurrentPosition();
        video_view.stopPlayback();
    }

    @Override
    public void onResume() {
        // Resume video player
        super.onResume();
        if (mPositionWhenPaused >= 0) {
            video_view.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
    }

    /**
     * 设置文件权限
     *
     * @param path 文件地址
     * @return 修改过权限的File
     */
    public static File createRWXfile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        file.setReadable(true, true);
        file.setWritable(true, true);
        file.setExecutable(true, true);
        return file;
    }


}
