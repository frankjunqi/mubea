package com.android.tedcoder.material;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.android.tedcoder.material.api.Host;


public class FirstActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_rawmaterial;
    private Button btn_semimaterial;
    private Button btn_allmachine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btn_rawmaterial = (Button) findViewById(R.id.btn_rawmaterial);
        btn_rawmaterial.setOnClickListener(this);

        btn_semimaterial = (Button) findViewById(R.id.btn_semimaterial);
        btn_semimaterial.setOnClickListener(this);

        btn_allmachine = (Button) findViewById(R.id.btn_allmachine);
        btn_allmachine.setOnClickListener(this);

        String host = getSP(Host.KWYHOST);
        if (!TextUtils.isEmpty(host) && host.startsWith("http://") && host.endsWith("/")) {
            Host.HOST = host;
        }

    }

    private String getSP(String key) {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rawmaterial:
                Intent raw_material_intent = new Intent(FirstActivity.this, RawMaterialActivity.class);
                startActivity(raw_material_intent);
                this.finish();
                break;
            case R.id.btn_semimaterial:
                Intent semi_material_intent = new Intent(FirstActivity.this, SemiMaterialActivity.class);
                startActivity(semi_material_intent);
                this.finish();
                break;
            case R.id.btn_allmachine:
                Intent allmachine_intent = new Intent(FirstActivity.this, AllMachineActivity.class);
                startActivity(allmachine_intent);
                this.finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
