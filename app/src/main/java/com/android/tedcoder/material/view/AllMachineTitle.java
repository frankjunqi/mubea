package com.android.tedcoder.material.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.material.R;
import com.android.tedcoder.material.entity.allmachine.MachineCell;

/**
 * 看板 cell view的显示
 * <p/>
 * Created by kjh08490 on 2016/3/24.
 */
public class AllMachineTitle extends LinearLayout {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    // 标号
    private TextView tv_00;
    private TextView tv_01;
    private TextView tv_02;
    private MarqueeTextView tv_03;

    private TextView tv_06;
    private TextView tv_07;
    private TextView tv_08;

    public AllMachineTitle(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.allmachine_title, this);
        tv_00 = (TextView) findViewById(R.id.tv_00);
        tv_01 = (TextView) findViewById(R.id.tv_01);
        tv_02 = (TextView) findViewById(R.id.tv_02);
        tv_03 = (MarqueeTextView) findViewById(R.id.tv_03);
        tv_06 = (TextView) findViewById(R.id.tv_06);
        tv_07 = (TextView) findViewById(R.id.tv_07);
        tv_08 = (TextView) findViewById(R.id.tv_08);
    }

    /**
     * 设置标题
     */
    public void setDefaultTitleCell() {
        MachineCell machineCell = new MachineCell();
        machineCell.Name = "设备名";
        machineCell.State = "设备状态";
        machineCell.QSState = "质量释放";
        machineCell.CustPN = "订单信息";
        machineCell.ProdPlanCount = "计划数量";
        machineCell.ProdCount = "生产数量";
        machineCell.ProdPercent = "完成率";
        tv_00.setText(TextUtils.isEmpty(machineCell.Name) ? "" : machineCell.Name);
        tv_01.setText(TextUtils.isEmpty(machineCell.State) ? "" : machineCell.State);
        tv_02.setText(TextUtils.isEmpty(machineCell.QSState) ? "" : machineCell.QSState);
        tv_03.setText(TextUtils.isEmpty(machineCell.CustPN) ? "" : machineCell.CustPN);
        tv_06.setText(TextUtils.isEmpty(machineCell.ProdPlanCount) ? "" : machineCell.ProdPlanCount);
        tv_07.setText(TextUtils.isEmpty(machineCell.ProdCount) ? "" : machineCell.ProdCount);
        tv_08.setText(TextUtils.isEmpty(machineCell.ProdPercent) ? "" : machineCell.ProdPercent);
        tv_03.setGravity(Gravity.CENTER);
    }

}