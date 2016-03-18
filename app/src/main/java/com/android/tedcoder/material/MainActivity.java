package com.android.tedcoder.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_pdf;
    private Button btn_setting;
    private Button btn_video;
    private Button btn_fresco;

    private EditText et_host;
    private EditText et_request_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);

        et_host = (EditText) findViewById(R.id.et_host);
        et_request_time = (EditText) findViewById(R.id.et_request_time);

        btn_pdf = (Button) findViewById(R.id.btn_pdf);
        btn_pdf.setOnClickListener(this);

        btn_video = (Button) findViewById(R.id.btn_video);
        btn_video.setOnClickListener(this);

        btn_fresco = (Button) findViewById(R.id.btn_fresco);
        btn_fresco.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                initSetting();
                break;
            case R.id.btn_pdf:
                Intent intent = new Intent(MainActivity.this, PDFViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_video:
                Intent video_intent = new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(video_intent);
                break;
            case R.id.btn_fresco:
                Intent fresco_intent = new Intent(MainActivity.this, FrescoViewActivity.class);
                startActivity(fresco_intent);
                break;
        }
    }

    private void initSetting() {
        String host = et_host.getText().toString();
        String request_time = et_request_time.getText().toString();
        if (!TextUtils.isEmpty(host)) {
            // do  something
            Host.HOST = host;
        }

        int time = 10;
        if (!TextUtils.isEmpty(request_time)) {
            try {
                time = Integer.parseInt(request_time);
                if (time < 5) {
                    Toast.makeText(MainActivity.this, "时间间隔必须大于5秒", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                time = 10;
            }
        }
        Host.TENLOOPER = time;

        Intent intent = new Intent(MainActivity.this, RawMaterialActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
