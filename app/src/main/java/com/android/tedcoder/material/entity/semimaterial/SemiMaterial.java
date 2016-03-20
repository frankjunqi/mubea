package com.android.tedcoder.material.entity.semimaterial;

import java.util.ArrayList;

/**
 * Created by kjh08490 on 2016/3/20.
 */
public class SemiMaterial {
    public String __type;

    // 库位信息
    public ArrayList<SemiCell> Cells = new ArrayList<>();

    // 动态消息
    public ArrayList<String> Msg = new ArrayList<>();
}
