package com.android.tedcoder.material.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.material.R;
import com.android.tedcoder.material.entity.allmachine.MachineCell;

/**
 * 看板 cell view的显示
 * <p/>
 * Created by kjh08490 on 2016/3/24.
 */
public class AllMachineView extends LinearLayout {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    // 标号
    private TextView tv_00;
    private TextView tv_01;
    private TextView tv_02;
    private TextView tv_03;
    private TextView tv_04;
    private TextView tv_05;
    private TextView tv_06;
    private TextView tv_07;
    private TextView tv_08;

    public AllMachineView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.allmachine_line_item, this);
        tv_00 = (TextView) findViewById(R.id.tv_00);
        tv_01 = (TextView) findViewById(R.id.tv_01);
        tv_02 = (TextView) findViewById(R.id.tv_02);
        tv_03 = (TextView) findViewById(R.id.tv_03);
        tv_04 = (TextView) findViewById(R.id.tv_04);
        tv_05 = (TextView) findViewById(R.id.tv_05);
        tv_06 = (TextView) findViewById(R.id.tv_06);
        tv_07 = (TextView) findViewById(R.id.tv_07);
        tv_08 = (TextView) findViewById(R.id.tv_08);
    }

    public void setMachineCellData(MachineCell machineCell) {
        tv_00.setText(machineCell == null || TextUtils.isEmpty(machineCell.Name) ? "" : machineCell.Name);
        tv_01.setText(machineCell == null || TextUtils.isEmpty(machineCell.State) ? "" : machineCell.State);
        tv_02.setText(machineCell == null || TextUtils.isEmpty(machineCell.QSState) ? "" : machineCell.QSState);
        tv_03.setText(machineCell == null || TextUtils.isEmpty(machineCell.CustPN) ? "" : machineCell.CustPN);
        tv_04.setText(machineCell == null || TextUtils.isEmpty(machineCell.ProdPlanCount) ? "" : machineCell.ProdPlanCount);
        tv_05.setText(machineCell == null || TextUtils.isEmpty(machineCell.ProdCount) ? "" : machineCell.ProdCount);
        tv_06.setText(machineCell == null || TextUtils.isEmpty(machineCell.ProdPercent) ? "" : machineCell.ProdPercent);
    }

}