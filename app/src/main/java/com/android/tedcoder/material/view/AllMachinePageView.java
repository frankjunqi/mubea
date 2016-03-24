package com.android.tedcoder.material.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.android.tedcoder.material.entity.allmachine.MachineCell;

import java.util.ArrayList;

/**
 * 看板 page view的显示
 * <p/>
 * Created by kjh08490 on 2016/3/24.
 */
public class AllMachinePageView extends LinearLayout {

    // 最大的cell的行数
    private static final int MAXCELLCOUNT = 7;
    private ArrayList<AllMachineView> allMachineViews = new ArrayList<AllMachineView>();

    public AllMachinePageView(Context context) {
        super(context);
        initMachinePage();
    }

    // 初始化此page的总的cell的view
    private void initMachinePage() {
        setOrientation(LinearLayout.VERTICAL);
        setWeightSum(MAXCELLCOUNT);
        for (int i = 0; i < MAXCELLCOUNT; i++) {
            AllMachineView allMachineView = new AllMachineView(getContext());
            addView(allMachineView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
            allMachineViews.add(allMachineView);
        }
    }

    /**
     * 设置cell的数据源
     *
     * @param page     第几页: 从 0 开始计数
     * @param cellList 数据源
     */
    public void setMachineViewList(int page, ArrayList<MachineCell> cellList) {
        if (cellList == null || cellList.size() == 0) {
            return;
        }

        // 判断是否是存在的页卡
        int startIndex = page * MAXCELLCOUNT;
        int maxEndIndex = (page + 1) * MAXCELLCOUNT;

        for (int i = startIndex, j = 0; i < cellList.size() && i < maxEndIndex; i++, j++) {
            allMachineViews.get(j).setMachineCellData(cellList.get(i));
        }
    }

}
