package com.android.tedcoder.material;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Frank on 16/4/23.
 */
public class SecretActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "TAG";

    private EditText et_secret;

    private Button btn_sure;

    private Context mContext;

    private TextView tv_currentversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);
        mContext = this;
        tv_currentversion = (TextView) findViewById(R.id.tv_currentversion);
        et_secret = (EditText) findViewById(R.id.et_secret);
        btn_sure = (Button) findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);

        et_secret.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });

        tv_currentversion.setText("版本号:  " + getVersionCode(mContext));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                String secret = et_secret.getText().toString().replace(" ", "");
                if ("mesmubea".equals(secret)) {
                    Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                } else {
                    Toast.makeText(this, "密码错误，请联系管理员！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
