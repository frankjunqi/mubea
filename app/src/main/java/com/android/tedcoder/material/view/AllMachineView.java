package com.android.tedcoder.material.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.material.R;
import com.android.tedcoder.material.entity.allmachine.MachineCell;

import java.text.NumberFormat;

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
    private TextView tv_06;
    private TextView tv_07;
    private TextView tv_08;
    private LinearLayout ll_08;

    private int width = 0;
    private NumberFormat num;

    public AllMachineView(Context context, int width) {
        super(context);
        this.mContext = context;
        this.width = width;
        num = NumberFormat.getPercentInstance();
        num.setMaximumIntegerDigits(3);
        num.setMaximumFractionDigits(2);
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.allmachine_line_item, this);
        tv_00 = (TextView) findViewById(R.id.tv_00);
        tv_01 = (TextView) findViewById(R.id.tv_01);
        tv_02 = (TextView) findViewById(R.id.tv_02);
        tv_03 = (TextView) findViewById(R.id.tv_03);
        tv_06 = (TextView) findViewById(R.id.tv_06);
        tv_07 = (TextView) findViewById(R.id.tv_07);
        tv_08 = (TextView) findViewById(R.id.tv_08);
        ll_08 = (LinearLayout) findViewById(R.id.ll_08);
    }

    public void setMachineCellData(MachineCell machineCell) {
        tv_00.setText(machineCell == null || TextUtils.isEmpty(machineCell.Name) ? "" : machineCell.Name);
        tv_01.setText(machineCell == null || TextUtils.isEmpty(machineCell.StateCode) ? "" : machineCell.StateCode);
        // 设备状态为1绿色 2黄色 3红色
        if (machineCell != null && !TextUtils.isEmpty(machineCell.State)) {
            if ("1".equals(machineCell.State)) {
                tv_01.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_shape, 0, 0, 0);
            } else if ("2".equals(machineCell.State)) {
                tv_01.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_shape, 0, 0, 0);
            } else if ("3".equals(machineCell.State)) {
                tv_01.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_shape, 0, 0, 0);
            }
        } else {
            tv_01.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        tv_02.setText(machineCell == null || TextUtils.isEmpty(machineCell.QSStateCode) ? "" : machineCell.QSStateCode);
        // 质量释放 1绿色 2黄色
        if (machineCell != null && !TextUtils.isEmpty(machineCell.QSState)) {
            if ("1".equals(machineCell.QSState)) {
                tv_02.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_shape, 0, 0, 0);
            } else if ("2".equals(machineCell.QSState)) {
                tv_02.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yellow_shape, 0, 0, 0);
            }
        } else {
            tv_02.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        tv_03.setText(machineCell == null || TextUtils.isEmpty(machineCell.CustPN) ? "" : machineCell.CustPN);
        tv_06.setText(machineCell == null || TextUtils.isEmpty(machineCell.ProdPlanCount) ? "" : machineCell.ProdPlanCount);
        tv_07.setText(machineCell == null || TextUtils.isEmpty(machineCell.ProdCount) ? "" : machineCell.ProdCount);
        // 计算进度
        float percent = 0.0f;
        try {
            percent = Float.parseFloat(machineCell.ProdPercent);
        } catch (Exception e) {
            percent = 0.0f;
        }
        tv_08.setText(String.valueOf(num.format(percent)));
        float percentwidth = (width / 9 * percent);
        tv_08.setLayoutParams(new LinearLayout.LayoutParams((int) percentwidth, LinearLayout.LayoutParams.MATCH_PARENT));
        tv_08.setBackgroundResource(R.drawable.blue_shape);
    }

    /**
     * 设置标题
     */
    public void setDefaultTitleCell() {
        MachineCell machineCell = new MachineCell();
        machineCell.Name = "设备名";
        machineCell.State = "设备状态";
        machineCell.QSState = "质量释放";
        machineCell.CustPN = "订单相关信息";
        machineCell.ProdPlanCount = "计划数量";
        machineCell.ProdCount = "生成数量";
        machineCell.ProdPercent = "完成率";
        tv_00.setText(TextUtils.isEmpty(machineCell.Name) ? "" : machineCell.Name);
        tv_01.setText(TextUtils.isEmpty(machineCell.State) ? "" : machineCell.State);
        tv_02.setText(TextUtils.isEmpty(machineCell.QSState) ? "" : machineCell.QSState);
        tv_03.setText(TextUtils.isEmpty(machineCell.CustPN) ? "" : machineCell.CustPN);
        tv_06.setText(TextUtils.isEmpty(machineCell.ProdPlanCount) ? "" : machineCell.ProdPlanCount);
        tv_07.setText(TextUtils.isEmpty(machineCell.ProdCount) ? "" : machineCell.ProdCount);
        tv_08.setText(TextUtils.isEmpty(machineCell.ProdPercent) ? "" : machineCell.ProdPercent);
        ll_08.setGravity(Gravity.CENTER);
    }

}