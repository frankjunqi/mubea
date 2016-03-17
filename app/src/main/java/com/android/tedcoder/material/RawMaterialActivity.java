package com.android.tedcoder.material;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.api.MaterialService;
import com.android.tedcoder.material.entity.Cell;
import com.android.tedcoder.material.entity.RawMaterialResBody;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.MarqueeTextView;
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

    // title的容器
    private LinearLayout ll_title;

    private TextClock textClock;
    private DigitalClock digitalClock;
    private TextView tv_weather;
    private TextView tv_temperature;
    private MarqueeTextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rawmaterial);
        Log.e(TAG, "onCreate");

        ll_a = (LinearLayout) findViewById(R.id.ll_a);
        ll_b = (LinearLayout) findViewById(R.id.ll_b);
        ll_d = (LinearLayout) findViewById(R.id.ll_d);
        ll_e = (LinearLayout) findViewById(R.id.ll_e);

        ll_title = (LinearLayout) findViewById(R.id.ll_title);

        rawLineView_a = new RawLineView(this);
        rawLineView_b = new RawLineView(this);
        rawLineView_d = new RawLineView(this);
        rawLineView_e = new RawLineView(this);


        ll_a.addView(rawLineView_a, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_b.addView(rawLineView_b, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_d.addView(rawLineView_d, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_e.addView(rawLineView_e, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        initTitleLayout();

        requestHandler = new RequestHandler();
        requestHandler.sendEmptyMessage(SENDFLAG);
    }


    /**
     * 初始化 title layout
     */
    private void initTitleLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.rawmaterial_title, null);
        textClock = (TextClock) view.findViewById(R.id.textClock);
        digitalClock = (DigitalClock) view.findViewById(R.id.digitalClock);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_info = (MarqueeTextView) view.findViewById(R.id.tv_info);

        textClock.setFormat12Hour("yyyy/MM/dd");
        tv_info.setFocusable(true);

        // 计算高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels / 9;
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        ll_title.addView(view);
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);// 小数点四舍五入取整
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
                handleMarqueeText(response.body().d.Data.Msg);
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<RawMaterialResBody> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                Toast.makeText(RawMaterialActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }
        });
    }


    /**
     * 处理list的数据
     *
     * @param cellList cell的数据源
     */
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
     * 处理滚动的字幕
     */
    private void handleMarqueeText(String marqueeText) {
        if (TextUtils.isEmpty(marqueeText)) {
            marqueeText = "This is nothing to tell you. You can set the message. Tell Boss. ";
        }
        tv_info.setText(marqueeText);
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
