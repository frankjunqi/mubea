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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.tedcoder.material.api.AllMachineService;
import com.android.tedcoder.material.api.Host;
import com.android.tedcoder.material.entity.allmachine.AllMachineResBody;
import com.android.tedcoder.material.entity.allmachine.MachineCell;
import com.android.tedcoder.material.gsonfactory.GsonConverterFactory;
import com.android.tedcoder.material.view.AllMachinePageView;
import com.android.tedcoder.material.view.AllMachineView;
import com.android.tedcoder.material.view.TitleLineView;

import java.util.ArrayList;
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
    private static final int SCROLLTIME = 0x12;


    // 系统退出的纪录时间
    private long mExitTime = 0;
    private static RequestHandler requestHandler = null;

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

    // title的容器
    private LinearLayout ll_title;
    private TitleLineView titleLineView;
    private LinearLayout ll_cell_title;

    // content
    private RecyclerView recyclerview;
    private SpecialCardsAdapter specialCardsAdapter;
    private int visiableIndex = 0;

    private DisplayMetrics dm;
    private int titleHeight = 0;
    private int totalHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 初始化 title layout
     */
    private void initTitleLayout() {
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        titleLineView = new TitleLineView(AllMachineActivity.this);
        titleLineView.setTitle("生产看板");
        ll_title.addView(titleLineView);
    }

    private void initCellTitle() {
        ll_cell_title = (LinearLayout) findViewById(R.id.ll_cell_title);
        AllMachineView allMachineView = new AllMachineView(AllMachineActivity.this, dm.widthPixels);
        allMachineView.setDefaultTitleCell();
        allMachineView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, titleHeight));
        ll_cell_title.addView(allMachineView);
    }

    private void initContentLayout() {
        requestHandler = new RequestHandler();
        requestHandler.sendEmptyMessage(SENDFLAG);
        requestHandler.sendEmptyMessage(SCROLLTIME);
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
                Toast.makeText(AllMachineActivity.this, "网络出现异常，请检查网络链接", Toast.LENGTH_LONG).show();
                requestHandler.sendEmptyMessageDelayed(SENDFLAG, Host.TENLOOPER * 1000);
            }
        });
    }


    /**
     * 处理list的数据
     *
     * @param cellList cell的数据源
     */
    private void handleRowsList(ArrayList<MachineCell> cellList) {
        if (cellList == null && cellList.size() > 0) {
            return;
        }
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
                    showDate = showDate + marqueeText.get(i) + "   ";
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
        Log.e(TAG, "AllMachine_onDestroy");
        if (requestHandler != null) {
            requestHandler.removeMessages(SENDFLAG);
            requestHandler.removeMessages(SCROLLTIME);
        }
    }

    class SpecialCardsAdapter extends RecyclerView.Adapter<SpecialCardsAdapter.ViewHolder> {

        private ArrayList<MachineCell> cellList;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AllMachinePageView view = new AllMachinePageView(AllMachineActivity.this, dm.widthPixels);
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
            int totolPager = cellList.size() % Host.MAXCELLCOUNT > 0 ? cellList.size() / Host.MAXCELLCOUNT + 1 : cellList.size() / Host.MAXCELLCOUNT;
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
