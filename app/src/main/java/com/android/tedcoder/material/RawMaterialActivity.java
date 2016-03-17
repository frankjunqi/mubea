package com.android.tedcoder.material;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.api.MaterialService;
import com.android.tedcoder.material.entity.Cell;
import com.android.tedcoder.material.entity.RawMaterialResBody;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.RawLineView;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 材料的 主页面
 * Created by kjh08490 on 2016/3/17.
 */
public class RawMaterialActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

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

    private LinearLayout ll_a;
    private LinearLayout ll_b;
    private LinearLayout ll_d;
    private LinearLayout ll_e;

    private RawLineView rawLineView_a = null;
    private RawLineView rawLineView_b = null;
    private RawLineView rawLineView_d = null;
    private RawLineView rawLineView_e = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawmaterial);
        Log.e(TAG, "onCreate");

        ll_a = (LinearLayout) findViewById(R.id.ll_a);
        ll_b = (LinearLayout) findViewById(R.id.ll_b);
        ll_d = (LinearLayout) findViewById(R.id.ll_d);
        ll_e = (LinearLayout) findViewById(R.id.ll_e);


        rawLineView_a = new RawLineView(this);
        rawLineView_b = new RawLineView(this);
        rawLineView_d = new RawLineView(this);
        rawLineView_e = new RawLineView(this);


        ll_a.addView(rawLineView_a, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_b.addView(rawLineView_b, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_d.addView(rawLineView_d, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_e.addView(rawLineView_e, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        requestHandler = new RequestHandler();
        requestHandler.sendEmptyMessage(SENDFLAG);
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
        Log.e(TAG, "asyncRequest");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Host.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MaterialService service = retrofit.create(MaterialService.class);
        Call<RawMaterialResBody> rawMaterialResBodyCall = service.rawMaterialList("Srv", "VisualPlant.svc", "RawMaterialStockQuery");
        rawMaterialResBodyCall.enqueue(new Callback<RawMaterialResBody>() {
            @Override
            public void onResponse(Call<RawMaterialResBody> call, Response<RawMaterialResBody> response) {
                handleCellList(response.body().d.Data.Cells);
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<RawMaterialResBody> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                Toast.makeText(RawMaterialActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, TENLOOPER * 1000);
            }
        });
    }


    private void handleCellList(ArrayList<Cell> cellList) {
        ArrayList<Cell> cell_a = new ArrayList<Cell>();
        ArrayList<Cell> cell_b = new ArrayList<Cell>();
        ArrayList<Cell> cell_d = new ArrayList<Cell>();
        ArrayList<Cell> cell_e = new ArrayList<Cell>();

        for (int i = 0; i < cellList.size(); i++) {
            Cell cell = cellList.get(i);
            if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("A")) {
                cell_a.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("B")) {
                cell_b.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("D")) {
                cell_d.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("E")) {
                cell_e.add(cell);
            }
        }

        rawLineView_a.setLineCellData(cell_a);
        rawLineView_b.setLineCellData(cell_b);
        rawLineView_d.setLineCellData(cell_d);
        rawLineView_e.setLineCellData(cell_e);

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
                    Log.e(TAG, "resbody : --type = %s" + rawMaterialResBody.d.__type);
                    Log.e(TAG, "resbody : status = %s" + rawMaterialResBody.d.Status);
                    Log.e(TAG, "resbody : cell = %s" + rawMaterialResBody.d.Data.Cells.get(0).__type);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (requestHandler != null) {
            requestHandler.removeMessages(SENDFLAG);
        }
    }
}
