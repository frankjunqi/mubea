package com.android.tedcoder.material.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public class Material implements Serializable{
    public String __type;
    public List<String> Msg = new ArrayList<>();
    public ArrayList<Cell> Cells = new ArrayList<>();

}
