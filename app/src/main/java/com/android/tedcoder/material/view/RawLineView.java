package com.android.tedcoder.material.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.material.R;
import com.android.tedcoder.material.entity.rawmaterial.RawCell;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2016/3/17.
 */
public class RawLineView extends LinearLayout {

    private Context mContext;

    // 标号
    private TextView tv_a01;
    private TextView tv_a02;
    private TextView tv_a03;
    private TextView tv_a04;
    private TextView tv_a05;
    private TextView tv_a06;
    private TextView tv_a07;
    private ArrayList<TextView> tv_a0s = new ArrayList<TextView>();

    // 对应的数值
    private TextView tv_data_a01;
    private TextView tv_data_a02;
    private TextView tv_data_a03;
    private TextView tv_data_a04;
    private TextView tv_data_a05;
    private TextView tv_data_a06;
    private TextView tv_data_a07;
    private ArrayList<TextView> tv_data_a0s = new ArrayList<TextView>();


    public RawLineView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.rawmaterial_line_item, this);
        tv_a01 = (TextView) findViewById(R.id.tv_a01);
        tv_a02 = (TextView) findViewById(R.id.tv_a02);
        tv_a03 = (TextView) findViewById(R.id.tv_a03);
        tv_a04 = (TextView) findViewById(R.id.tv_a04);
        tv_a05 = (TextView) findViewById(R.id.tv_a05);
        tv_a06 = (TextView) findViewById(R.id.tv_a06);
        tv_a07 = (TextView) findViewById(R.id.tv_a07);
        tv_a0s.add(tv_a01);
        tv_a0s.add(tv_a02);
        tv_a0s.add(tv_a03);
        tv_a0s.add(tv_a04);
        tv_a0s.add(tv_a05);
        tv_a0s.add(tv_a06);
        tv_a0s.add(tv_a07);

        tv_data_a01 = (TextView) findViewById(R.id.tv_data_a01);
        tv_data_a02 = (TextView) findViewById(R.id.tv_data_a02);
        tv_data_a03 = (TextView) findViewById(R.id.tv_data_a03);
        tv_data_a04 = (TextView) findViewById(R.id.tv_data_a04);
        tv_data_a05 = (TextView) findViewById(R.id.tv_data_a05);
        tv_data_a06 = (TextView) findViewById(R.id.tv_data_a06);
        tv_data_a07 = (TextView) findViewById(R.id.tv_data_a07);
        tv_data_a0s.add(tv_data_a01);
        tv_data_a0s.add(tv_data_a02);
        tv_data_a0s.add(tv_data_a03);
        tv_data_a0s.add(tv_data_a04);
        tv_data_a0s.add(tv_data_a05);
        tv_data_a0s.add(tv_data_a06);
        tv_data_a0s.add(tv_data_a07);
    }

    public void setLineCellData(ArrayList<RawCell> cellList) {
        if (cellList == null || cellList.size() == 0) {
            return;
        }
        int totolViewSize = 7;
        for (int i = 0; i < cellList.size() && i < totolViewSize; i++) {
            RawCell cell = cellList.get(i);
            tv_a0s.get(i).setText(TextUtils.isEmpty(cell.LocCode) ? "" : cellList.get(i).LocCode);
            tv_data_a0s.get(i).setText(TextUtils.isEmpty(cell.Diameter) ? "" : cellList.get(i).Diameter);
        }

    }
}
