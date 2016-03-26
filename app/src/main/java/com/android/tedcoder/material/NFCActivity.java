package com.android.tedcoder.material;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Frank on 16/3/26.
 */
public class NFCActivity extends AppCompatActivity {

    private TextView tv_nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        tv_nfc = (TextView) findViewById(R.id.tv_nfc);
    }
}
