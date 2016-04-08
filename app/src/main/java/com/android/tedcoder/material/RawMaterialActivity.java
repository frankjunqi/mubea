package com.android.tedcoder.material;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.api.RawMaterialService;
import com.android.tedcoder.material.entity.rawmaterial.RawCell;
import com.android.tedcoder.material.entity.rawmaterial.RawMaterialResBody;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.RawLineView;
import com.android.tedcoder.material.view.TitleLineView;

import java.util.ArrayList;
import java.util.List;

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
    private static final int SENDFLAG = 0x10;

    // 系统退出的纪录时间
    private long mExitTime = 0;

    private static RequestHandler requestHandler = null;

    private class RequestHandler extends Handler {

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
    private LinearLayout ll_bottom;
    private LinearLayout ll_content;

    private TitleLineView titleLineView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_rawmaterial);
        Log.e(TAG, "RawMaterial_onCreate");

        // 计算高度
        WindowManager wm = (WindowManager) getApplication()
                .getSystemService(Context.WINDOW_SERVICE);
        int heightpix = wm.getDefaultDisplay().getHeight();

        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        titleLineView = new TitleLineView(RawMaterialActivity.this);
        titleLineView.setTitle("原材料库存");

        // 设置title的height
        ll_title.addView(titleLineView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (heightpix - 18) / 9));
        // 设置bottom的height
        ll_bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 18));
        // 设置content的height
        ll_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightpix - 18 - (heightpix - 18) / 9));

        initContentLayout();
    }

    private void initContentLayout() {
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
        Log.e(TAG, "raw_material_async_Request");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Host.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RawMaterialService service = retrofit.create(RawMaterialService.class);
        Call<RawMaterialResBody> rawMaterialResBodyCall = service.rawMaterialList("Srv", "VisualPlant.svc", "RawMaterialStockQuery");
        rawMaterialResBodyCall.enqueue(new Callback<RawMaterialResBody>() {
            @Override
            public void onResponse(Call<RawMaterialResBody> call, Response<RawMaterialResBody> response) {
                if (response != null & response.body() != null) {
                    handleCellList(response.body().d.Data.Cells);
                    handleMarqueeText(response.body().d.Data.Msg);
                }
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<RawMaterialResBody> call, Throwable throwable) {
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
    private void handleCellList(ArrayList<RawCell> cellList) {
        if (cellList == null && cellList.size() > 0) {
            return;
        }

        ArrayList<RawCell> cell_a = new ArrayList<RawCell>();
        ArrayList<RawCell> cell_b = new ArrayList<RawCell>();
        ArrayList<RawCell> cell_d = new ArrayList<RawCell>();
        ArrayList<RawCell> cell_e = new ArrayList<RawCell>();

        for (int i = 0; i < cellList.size(); i++) {
            RawCell cell = cellList.get(i);
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
    private void handleMarqueeText(List<String> marqueeText) {
        if (marqueeText != null && marqueeText.size() > 0) {
            String showDate = "";
            if (marqueeText != null) {
                for (int i = 0; i < marqueeText.size(); i++) {
                    showDate = showDate + marqueeText.get(i);
                }
                // 判断现实的文案是否一样
                if (showDate.equals(titleLineView.getNoticContent())) {
                    return;
                }
                titleLineView.setNoticeContent(showDate);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "RawMaterial_onDestroy");
        if (requestHandler != null) {
            requestHandler.removeMessages(SENDFLAG);
        }
    }
}
