package com.android.tedcoder.material.entity;

import java.io.Serializable;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public class RawMaterialObj implements Serializable {
    public String __type;
    public Material Data = new Material();
    public String Status;
    public String Msg;
    public String MsgList;

}