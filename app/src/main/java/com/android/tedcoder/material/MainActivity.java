package com.android.tedcoder.material;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_fresco;

    private EditText et_request_time;
    private EditText et_host;
    private EditText et_ainimation_time;


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
        et_ainimation_time = (EditText) findViewById(R.id.et_ainimation_time);

        String host = getSP(Host.KWYHOST);
        if (TextUtils.isEmpty(host)) {
            et_host.setText(Host.HOST);
        }else{
            et_host.setText(host);
        }

        btn_fresco = (Button) findViewById(R.id.btn_fresco);
        btn_fresco.setOnClickListener(this);

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
            case R.id.btn_fresco:
                Intent fresco_intent = new Intent(MainActivity.this, FrescoViewActivity.class);
                startActivity(fresco_intent);
                break;
        }
    }

    private void savaSP(String key, String value) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString(key, value);
        //提交当前数据
        editor.commit();
    }

    private String getSP(String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    private void initSetting() {
        String host = et_host.getText().toString();
        String request_time = et_request_time.getText().toString();
        String animatin_time = et_ainimation_time.getText().toString();
        if (!TextUtils.isEmpty(host)) {
            // do  something
            Host.HOST = host;
            savaSP(Host.KWYHOST, host);
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

        int animation_time = 5;
        if (!TextUtils.isEmpty(animatin_time)) {
            try {
                animation_time = Integer.parseInt(animatin_time);
                if (animation_time < 5) {
                    Toast.makeText(MainActivity.this, "时间间隔必须大于5秒", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                animation_time = 5;
            }
        }
        Host.TIME = animation_time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
