package com.android.tedcoder.material.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tedcoder.material.R;
import com.android.tedcoder.material.entity.semimaterial.SemiCell;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2016/3/17.
 */
public class SemiLineView extends LinearLayout {

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
    private TextView tv_09;
    private ArrayList<TextView> tv_as = new ArrayList<TextView>();

    public SemiLineView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.semimaterial_line_item, this);
        tv_00 = (TextView) findViewById(R.id.tv_00);
        tv_01 = (TextView) findViewById(R.id.tv_01);
        tv_02 = (TextView) findViewById(R.id.tv_02);
        tv_03 = (TextView) findViewById(R.id.tv_03);
        tv_04 = (TextView) findViewById(R.id.tv_04);
        tv_05 = (TextView) findViewById(R.id.tv_05);
        tv_06 = (TextView) findViewById(R.id.tv_06);
        tv_07 = (TextView) findViewById(R.id.tv_07);
        tv_08 = (TextView) findViewById(R.id.tv_08);
        tv_09 = (TextView) findViewById(R.id.tv_09);
        tv_as.add(tv_00);
        tv_as.add(tv_01);
        tv_as.add(tv_02);
        tv_as.add(tv_03);
        tv_as.add(tv_04);
        tv_as.add(tv_05);
        tv_as.add(tv_06);
        tv_as.add(tv_07);
        tv_as.add(tv_08);
        tv_as.add(tv_09);
    }

    public void setLineCellData(ArrayList<SemiCell> cellList) {
        if (cellList == null || cellList.size() == 0) {
            return;
        }

        SemiCell semiCellFirst = new SemiCell();

        SemiCell semiCell = cellList.get(0);
        if (semiCell.LocCode.contains("A")) {
            semiCellFirst.Diameter = "A";
        } else if (semiCell.LocCode.contains("B")) {
            semiCellFirst.Diameter = "B";
        } else if (semiCell.LocCode.contains("C")) {
            semiCellFirst.Diameter = "C";
        } else if (semiCell.LocCode.contains("D")) {
            semiCellFirst.Diameter = "D";
        } else if (semiCell.LocCode.contains("E")) {
            semiCellFirst.Diameter = "E";
        } else if (semiCell.LocCode.contains("F")) {
            semiCellFirst.Diameter = "F";
        } else if (semiCell.LocCode.contains("G")) {
            semiCellFirst.Diameter = "G";
        } else if (semiCell.LocCode.contains("H")) {
            semiCellFirst.Diameter = "H";
        } else if (semiCell.LocCode.contains("I")) {
            semiCellFirst.Diameter = "I";
        }
        cellList.add(0, semiCellFirst);

        int totolViewSize = tv_as.size();
        for (int i = 0; i < cellList.size() && i < totolViewSize; i++) {
            SemiCell cell = cellList.get(i);
            String diameter = TextUtils.isEmpty(cell.Diameter) ? "" : cellList.get(i).Diameter;
            String strength = TextUtils.isEmpty(cell.Strength) ? "" : cellList.get(i).Strength;
            tv_as.get(i).setText(diameter + (TextUtils.isEmpty(strength) ? "" : "\n") + strength);
        }

    }

    /**
     * 设置数字
     */
    public void setNumberLine() {
        for (int i = 0; i < tv_as.size(); i++) {
            if (i != 0) {
                tv_as.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.semi_title_text_size));
                tv_as.get(i).setText(String.valueOf(i));
            }
        }
    }
}
