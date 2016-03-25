package com.android.tedcoder.material.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.tedcoder.material.api.AllMachineService;
import com.android.tedcoder.material.entity.allmachine.MachineCell;

import java.util.ArrayList;

/**
 * 看板 page view的显示
 * <p/>
 * Created by kjh08490 on 2016/3/24.
 */
public class AllMachinePageView extends LinearLayout {

    private ArrayList<AllMachineView> allMachineViews = new ArrayList<>();

    public AllMachinePageView(Context context) {
        super(context);
        initMachinePage();
    }

    // 初始化此page的总的cell的view
    private void initMachinePage() {
        setOrientation(LinearLayout.VERTICAL);
        setWeightSum(AllMachineService.MAXCELLCOUNT);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < AllMachineService.MAXCELLCOUNT; i++) {
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
        int startIndex = page * AllMachineService.MAXCELLCOUNT;

        for (int i = 0, j = 0; j < AllMachineService.MAXCELLCOUNT; i++, j++) {
            if (i + startIndex < cellList.size()) {
                allMachineViews.get(j).setMachineCellData(cellList.get(i + startIndex));
            } else {
                allMachineViews.get(j).setMachineCellData(null);
            }
        }
    }

}
