package com.android.tedcoder.material;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.api.MaterialService;
import com.android.tedcoder.material.entity.RawMaterialResBody;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 材料的 主页面
 * Created by kjh08490 on 2016/3/17.
 */
public class RawMaterialActivity extends AppCompatActivity {

    // 时隔10秒请求一次
    private static final int TENLOOPER = 10;
    // 发送请求的标志码
    private static final int SENDFLAG = 1;


    private static RequestHandler requestHandler;

    public class RequestHandler extends Handler {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SENDFLAG:
                    asyncRequest();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestHandler != null) {
            requestHandler.sendEmptyMessageDelayed(SENDFLAG, TENLOOPER * 1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawmaterial);
        requestHandler = new RequestHandler();
        asyncRequest();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Retrofit 异步请求
     */
    private void asyncRequest() {
        // 异步请求处理
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Host.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MaterialService service = retrofit.create(MaterialService.class);
        Call<RawMaterialResBody> rawMaterialResBodyCall = service.rawMaterialList("Srv", "VisualPlant.svc", "RawMaterialStockQuery");
        rawMaterialResBodyCall.enqueue(new Callback<RawMaterialResBody>() {
            @Override
            public void onResponse(Call<RawMaterialResBody> call, Response<RawMaterialResBody> response) {
                Log.e("resbody = ", response.body().d.__type);
                Log.e("resbody = ", response.body().d.Data.__type);
                Log.e("resbody = ", response.body().d.Data.Cells.get(0).__type);
                Log.e("resbody = ", response.body().d.Data.Cells.get(0).__type);
                Log.e("resbody = ", (response.body().d.Data.Cells.get(0).Diameter + "").equals("null") + "");
                Toast.makeText(RawMaterialActivity.this, "null", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<RawMaterialResBody> call, Throwable throwable) {
                Log.e("ww", throwable.getMessage());
                Toast.makeText(RawMaterialActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Retrofit 同步请求
     */
    private void synchroRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 同步请求处理
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Host.HOST)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MaterialService service = retrofit.create(MaterialService.class);
                Call<RawMaterialResBody> rawMaterialResBodyCall = service.rawMaterialList("Srv", "VisualPlant.svc", "RawMaterialStockQuery");
                try {
                    RawMaterialResBody rawMaterialResBody = rawMaterialResBodyCall.execute().body();
                    Log.e("resbody : --type = %s", rawMaterialResBody.d.__type);
                    Log.e("resbody : status = %s", rawMaterialResBody.d.Status);
                    Log.e("resbody : cell = %s", rawMaterialResBody.d.Data.Cells.get(0).__type);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestHandler.removeMessages(SENDFLAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestHandler.removeMessages(SENDFLAG);
    }
}
