package com.android.tedcoder.material.entity.rawmaterial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public class RawMaterial implements Serializable{
    public String __type;
    public List<String> Msg = new ArrayList<>();
    public ArrayList<RawCell> Cells = new ArrayList<>();

}
