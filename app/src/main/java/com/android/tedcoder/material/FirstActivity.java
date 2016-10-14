package com.android.tedcoder.material;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


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
