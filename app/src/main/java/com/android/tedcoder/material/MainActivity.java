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
    private Button btn_video;
    private Button btn_sys_video;
    private Button btn_download;
    private Button btn_fresco;

    private EditText et_request_time;
    private EditText et_host;


    private Button btn_rawmaterial;
    private Button btn_semimaterial;
    private Button btn_allmachine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_rawmaterial = (Button) findViewById(R.id.btn_rawmaterial);
        btn_rawmaterial.setOnClickListener(this);

        btn_semimaterial = (Button) findViewById(R.id.btn_semimaterial);
        btn_semimaterial.setOnClickListener(this);

        btn_allmachine = (Button) findViewById(R.id.btn_allmachine);
        btn_allmachine.setOnClickListener(this);

        et_host = (EditText) findViewById(R.id.et_host);
        et_request_time = (EditText) findViewById(R.id.et_request_time);

        btn_pdf = (Button) findViewById(R.id.btn_pdf);
        btn_pdf.setOnClickListener(this);

        btn_video = (Button) findViewById(R.id.btn_video);
        btn_video.setOnClickListener(this);

        btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);

        btn_fresco = (Button) findViewById(R.id.btn_fresco);
        btn_fresco.setOnClickListener(this);

        btn_sys_video = (Button) findViewById(R.id.btn_sys_video);
        btn_sys_video.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rawmaterial:
                initSetting();
                Intent raw_material_intent = new Intent(MainActivity.this, RawMaterialActivity.class);
                startActivity(raw_material_intent);
                break;
            case R.id.btn_semimaterial:
                initSetting();
                Intent semi_material_intent = new Intent(MainActivity.this, SemiMaterialActivity.class);
                startActivity(semi_material_intent);
                break;
            case R.id.btn_allmachine:
                initSetting();
                Intent allmachine_intent = new Intent(MainActivity.this, AllMachineActivity.class);
                startActivity(allmachine_intent);
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
            case R.id.btn_sys_video:
                Intent videoplay_intent = new Intent(MainActivity.this, VideoPlayActivity.class);
                startActivity(videoplay_intent);
                break;
            case R.id.btn_download:
                Toast.makeText(MainActivity.this, "Download", Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
