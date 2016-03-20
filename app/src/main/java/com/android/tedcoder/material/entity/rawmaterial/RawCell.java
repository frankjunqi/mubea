package com.android.tedcoder.material.entity.rawmaterial;

import java.io.Serializable;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public class RawCell implements Serializable {
    public String __type;
    public String LocCode;// 库位代码
    public String LocID;// 库位id
    public String Diameter;// 线径
    public String InOutTime;//  出入库时间
    public String MaterialCode;// 原材料代码
    public String RawMaterialID;// 原材料id
}
