package com.android.tedcoder.material.entity.allmachine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kjh08490 on 2016/3/24.
 */
public class AllMachine implements Serializable {
    public String __type;
    // 动态信息
    public List<String> Msg = new ArrayList<>();
    // 设备情况
    public ArrayList<MachineCell> Rows = new ArrayList<>();
}
