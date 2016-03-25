package com.android.tedcoder.material;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tedcoder.material.api.AllMachineService;
import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.entity.allmachine.AllMachineResBody;
import com.android.tedcoder.material.entity.allmachine.MachineCell;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.AllMachinePageView;
import com.android.tedcoder.material.view.AllMachineView;
import com.android.tedcoder.material.view.MarqueeTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 生成看板 主页面
 * Created by kjh08490 on 2016/3/17.
 */
public class AllMachineActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    // 发送请求的标志码
    private static final int SENDFLAG = 0x10;
    private static final int TIMEFLAG = 0x11;
    private static final int SCROLLTIME = 0x12;


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
                case SCROLLTIME:
                    int count = recyclerview.getAdapter().getItemCount();
                    if (count != 0) {
                        if (visiableIndex > count) {
                            visiableIndex = 0;
                        }
                        Log.e(TAG, "position = " + visiableIndex % count + "-----count ==" + count + "-----visiableIndex ==" + visiableIndex);
                        recyclerview.scrollToPosition(visiableIndex % count);
                        visiableIndex++;
                    }
                    requestHandler.sendEmptyMessageDelayed(SCROLLTIME, Host.TIME * 1000);
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

    // title的容器
    private LinearLayout ll_title;
    private LinearLayout ll_cell_title;

    // content
    private RecyclerView recyclerview;
    private SpecialCardsAdapter specialCardsAdapter;
    private int visiableIndex = 0;

    private TextClock textClock;
    private TextView digitalClock;
    private TextView tv_weather;
    private TextView tv_temperature;
    private MarqueeTextView tv_info;

    private DisplayMetrics dm;
    private int titleHeight = 0;
    private int contentHeight = 0;
    private int totalHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmachine);
        Log.e(TAG, "RawMaterial_onCreate");
        initDisplay();
        initTitleLayout();
        initCellTitle();
        initRecycleView();
        initContentLayout();
    }

    // 初始化屏幕信息
    private void initDisplay() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        totalHeight = dm.heightPixels;
    }

    // 初始化recycle view
    private void initRecycleView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        recyclerview.setLayoutManager(layoutManager);
        specialCardsAdapter = new SpecialCardsAdapter();
        recyclerview.setAdapter(specialCardsAdapter);
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

        titleHeight = dm.heightPixels / (AllMachineService.MAXCELLCOUNT + 2);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));
        ll_title.addView(view);

        timeHandler = new TimeHandler();
        timeHandler.sendEmptyMessage(TIMEFLAG);
    }

    private void initCellTitle() {
        ll_cell_title = (LinearLayout) findViewById(R.id.ll_cell_title);
        AllMachineView allMachineView = new AllMachineView(AllMachineActivity.this);
        MachineCell machineCell = new MachineCell();
        machineCell.Name = "设备名";
        machineCell.State = "设备状态";
        machineCell.QSState = "质量释放";
        machineCell.CustPN = "订单相关信息";
        machineCell.ProdPlanCount = "计划数量";
        machineCell.ProdCount = "生成数量";
        machineCell.ProdPercent = "完成率";
        allMachineView.setMachineCellData(machineCell);
        allMachineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, titleHeight));
        ll_cell_title.addView(allMachineView);
    }

    private void initContentLayout() {
        requestHandler = new RequestHandler();
        requestHandler.sendEmptyMessage(SENDFLAG);
        requestHandler.sendEmptyMessage(SCROLLTIME);
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
        Log.e(TAG, "AllMachine_async_Request");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Host.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AllMachineService service = retrofit.create(AllMachineService.class);
        Call<AllMachineResBody> allMachineResBodyCall = service.allMachineList("Srv", "VisualPlant.svc", "AllMachineQuery");
        allMachineResBodyCall.enqueue(new Callback<AllMachineResBody>() {
            @Override
            public void onResponse(Call<AllMachineResBody> call, Response<AllMachineResBody> response) {
                if (response != null & response.body() != null) {
                    handleRowsList(response.body().d.Data.Rows);
                    handleMarqueeText(response.body().d.Data.Msg);
                }
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }

            @Override
            public void onFailure(Call<AllMachineResBody> call, Throwable throwable) {
                Log.e("error", throwable != null ? throwable.getMessage() : "");
                Toast.makeText(AllMachineActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }
        });
    }


    private int index = 0;


    /**
     * 处理list的数据
     *
     * @param cellList cell的数据源
     */
    private void handleRowsList(ArrayList<MachineCell> cellList) {
        if (cellList == null && cellList.size() > 0) {
            return;
        }
        /*if (index % 4 == 1 || index % 4 == 2 || index % 4 == 3) {
            for (int i = 0; i < index % 4; i++) {
                cellList.addAll(cellList);
            }
        }
        index++;*/

        /*for (int i = 0; i < 3; i++) {
            cellList.addAll(cellList);
        }*/

        specialCardsAdapter.setCellList(cellList);
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
        Log.e(TAG, "AllMachine_onDestroy");
        if (requestHandler != null) {
            requestHandler.removeMessages(SENDFLAG);
            requestHandler.removeMessages(SCROLLTIME);
        }

        if (timeHandler != null) {
            timeHandler.removeMessages(TIMEFLAG);
        }
    }

    class SpecialCardsAdapter extends RecyclerView.Adapter<SpecialCardsAdapter.ViewHolder> {

        private ArrayList<MachineCell> cellList;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AllMachinePageView view = new AllMachinePageView(AllMachineActivity.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, totalHeight - titleHeight * 2));
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据到ViewHolder上
            holder.allMachinePageView.setMachineViewList(position, cellList);
        }

        public void setCellList(ArrayList<MachineCell> cellList) {
            this.cellList = cellList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (cellList == null) {
                return 0;
            }
            int totolPager = cellList.size() % AllMachineService.MAXCELLCOUNT > 0 ? cellList.size() / AllMachineService.MAXCELLCOUNT + 1 : cellList.size() / AllMachineService.MAXCELLCOUNT;
            return totolPager;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public AllMachinePageView allMachinePageView;

            public ViewHolder(AllMachinePageView itemView) {
                super(itemView);
                this.allMachinePageView = itemView;
            }
        }

    }


}
