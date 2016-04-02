package com.android.tedcoder.material;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.api.SemiMaterialService;
import com.android.tedcoder.material.entity.semimaterial.SemiCell;
import com.android.tedcoder.material.entity.semimaterial.SemiMaterialResBody;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.MarqueeTextView;
import com.android.tedcoder.material.view.SemiLineView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 库位 主页面
 * Created by kjh08490 on 2016/3/17.
 */
public class SemiMaterialActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    // 发送请求的标志码
    private static final int SENDFLAG = 0x110;
    private static final int TIMEFLAG = 0x111;

    // 系统退出的纪录时间
    private long mExitTime = 0;

    private static RequestHandler requestHandler = null;

    private static Handler timeHandler = null;

    private class RequestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SENDFLAG:
                    asyncRequest();
                    break;
            }
        }
    }

    private class TimeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMEFLAG:
                    refreshTime();
                    timeHandler.sendEmptyMessageDelayed(TIMEFLAG, 1000);
                    break;
            }
        }
    }

    private LinearLayout ll_0;
    private LinearLayout ll_a;
    private LinearLayout ll_b;
    private LinearLayout ll_c;
    private LinearLayout ll_d;
    private LinearLayout ll_e;
    private LinearLayout ll_f;
    private LinearLayout ll_g;
    private LinearLayout ll_h;
    private LinearLayout ll_i;

    private SemiLineView semiLineView_0 = null;
    private SemiLineView semiLineView_a = null;
    private SemiLineView semiLineView_b = null;
    private SemiLineView semiLineView_c = null;
    private SemiLineView semiLineView_d = null;
    private SemiLineView semiLineView_e = null;
    private SemiLineView semiLineView_f = null;
    private SemiLineView semiLineView_g = null;
    private SemiLineView semiLineView_h = null;
    private SemiLineView semiLineView_i = null;

    // title的容器
    private LinearLayout ll_title;

    private TextClock textClock;
    private TextView digitalClock;
    private TextView tv_weather;
    private TextView tv_temperature;
    private MarqueeTextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semimaterial);
        Log.e(TAG, "Semi_Material_onCreate");

        initTitleLayout();

        initContentLayout();

    }


    /**
     * 初始化 title layout
     */
    private void initTitleLayout() {
        ll_title = (LinearLayout) findViewById(R.id.ll_title);

        View view = LayoutInflater.from(this).inflate(R.layout.rawmaterial_title, null);
        textClock = (TextClock) view.findViewById(R.id.textClock);
        digitalClock = (TextView) view.findViewById(R.id.digitalClock);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_info = (MarqueeTextView) view.findViewById(R.id.tv_info);

        // 计算高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels / 11;
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        ll_title.addView(view);

        timeHandler = new TimeHandler();
        timeHandler.sendEmptyMessage(TIMEFLAG);
    }

    private void initContentLayout() {
        ll_0 = (LinearLayout) findViewById(R.id.ll_0);
        ll_a = (LinearLayout) findViewById(R.id.ll_a);
        ll_b = (LinearLayout) findViewById(R.id.ll_b);
        ll_c = (LinearLayout) findViewById(R.id.ll_c);
        ll_d = (LinearLayout) findViewById(R.id.ll_d);
        ll_e = (LinearLayout) findViewById(R.id.ll_e);
        ll_f = (LinearLayout) findViewById(R.id.ll_f);
        ll_g = (LinearLayout) findViewById(R.id.ll_g);
        ll_h = (LinearLayout) findViewById(R.id.ll_h);
        ll_i = (LinearLayout) findViewById(R.id.ll_i);

        semiLineView_0 = new SemiLineView(this);
        semiLineView_a = new SemiLineView(this);
        semiLineView_b = new SemiLineView(this);
        semiLineView_c = new SemiLineView(this);
        semiLineView_d = new SemiLineView(this);
        semiLineView_e = new SemiLineView(this);
        semiLineView_f = new SemiLineView(this);
        semiLineView_g = new SemiLineView(this);
        semiLineView_h = new SemiLineView(this);
        semiLineView_i = new SemiLineView(this);


        ll_0.addView(semiLineView_0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        semiLineView_0.setNumberLine();

        ll_a.addView(semiLineView_a, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_b.addView(semiLineView_b, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_c.addView(semiLineView_c, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_d.addView(semiLineView_d, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_e.addView(semiLineView_e, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_f.addView(semiLineView_f, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_g.addView(semiLineView_g, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_h.addView(semiLineView_h, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_i.addView(semiLineView_i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        requestHandler = new RequestHandler();
        requestHandler.sendEmptyMessage(SENDFLAG);
    }


    private void refreshTime() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (digitalClock != null) {
            digitalClock.setText(String.format("%s:%s", day < 10 ? "0" + String.valueOf(day) : String.valueOf(day)
                    , minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute)));
        }
    }


    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);// 小数点四舍五入取整
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "在按一次退出系统", Toast.LENGTH_LONG).show();
            mExitTime = System.currentTimeMillis();
        } else {
            this.finish();
        }


    }

    /**
     * Retrofit 异步请求
     */
    private void asyncRequest() {
        // 异步请求处理
        Log.e(TAG, "semi_material_async_Request");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Host.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SemiMaterialService service = retrofit.create(SemiMaterialService.class);
        Call<SemiMaterialResBody> rawMaterialResBodyCall = service.semiMaterialList("Srv", "VisualPlant.svc", "SemiMaterialStockQuery");
        rawMaterialResBodyCall.enqueue(new Callback<SemiMaterialResBody>() {
            @Override
            public void onResponse(Call<SemiMaterialResBody> call, Response<SemiMaterialResBody> response) {
                if (response != null & response.body() != null) {
                    handleCellList(response.body().d.Data.Cells);
                    handleMarqueeText(response.body().d.Data.Msg);
                }
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<SemiMaterialResBody> call, Throwable throwable) {
                Toast.makeText(SemiMaterialActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }
        });
    }


    /**
     * 处理list的数据
     *
     * @param cellList cell的数据源
     */
    private void handleCellList(ArrayList<SemiCell> cellList) {
        if (cellList == null && cellList.size() > 0) {
            return;
        }

        ArrayList<SemiCell> cell_a = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_b = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_c = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_d = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_e = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_f = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_g = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_h = new ArrayList<SemiCell>();
        ArrayList<SemiCell> cell_i = new ArrayList<SemiCell>();

        for (int i = 0; i < cellList.size(); i++) {
            SemiCell cell = cellList.get(i);
            if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("A")) {
                cell_a.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("B")) {
                cell_b.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("C")) {
                cell_c.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("D")) {
                cell_d.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("E")) {
                cell_e.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("F")) {
                cell_f.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("G")) {
                cell_g.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("H")) {
                cell_h.add(cell);
            } else if (!TextUtils.isEmpty(cell.LocCode) && cell.LocCode.contains("I")) {
                cell_i.add(cell);
            }

        }

        semiLineView_a.setLineCellData(cell_a);
        semiLineView_b.setLineCellData(cell_b);
        semiLineView_c.setLineCellData(cell_c);
        semiLineView_d.setLineCellData(cell_d);
        semiLineView_e.setLineCellData(cell_e);
        semiLineView_f.setLineCellData(cell_f);
        semiLineView_g.setLineCellData(cell_g);
        semiLineView_h.setLineCellData(cell_h);
        semiLineView_i.setLineCellData(cell_i);

    }

    /**
     * 处理滚动的字幕
     */
    private void handleMarqueeText(List<String> marqueeText) {
        if (marqueeText != null && marqueeText.size() > 0) {
            String showDate = "";
            if (marqueeText != null) {
                for (int i = 0; i < marqueeText.size(); i++) {
                    showDate = showDate + marqueeText.get(i) + "       ";
                }
                // 判断现实的文案是否一样
                if (showDate.equals(tv_info.getText().toString())) {
                    return;
                }
                tv_info.setText(showDate);
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Semi_Material_onDestroy");
        if (requestHandler != null) {
            requestHandler.removeMessages(SENDFLAG);
        }

        if (timeHandler != null) {
            timeHandler.removeMessages(TIMEFLAG);
        }
    }
}
