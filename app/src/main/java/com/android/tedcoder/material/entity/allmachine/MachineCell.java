package com.android.tedcoder.material.entity.allmachine;

import java.io.Serializable;

/**
 * Created by kjh08490 on 2016/3/24.
 */
public class MachineCell implements Serializable {
            /*"__type": "MMachineRow:#AFMES.VisualPlant.DTO",
            "CustPN": "8888-999",
            "Customer": "SVW",
            "CustomerLogo": "svw.jpg",
            "Name": "拉拔",
            "OrderPercent": 0.7,
            "PO": "8888888",
            "ProdCount": 7000,
            "ProdPercent": 0.7,
            "ProdPlanCount": 10000,
            "QSState": "1",
            "QSStateCode": "",
            "State": "1",
            "StateCode": ""*/

    public String __type;
    // 客户物料号
    public String CustPN;
    // 客户
    public String Customer;
    // 客户logo文件名
    public String CustomerLogo;
    // 设备名称
    public String Name;
    // 订单完成百分比
    public String OrderPercent;
    // 订单编号
    public String PO;
    // 生产数量
    public String ProdCount;
    // 完成率
    public String ProdPercent;
    // 计划生产数量
    public String ProdPlanCount;
    // QS状态(质量释放)
    public String QSState;
    // QS状态代码
    public String QSStateCode;
    // 运行状态
    public String State;
    // 运行状态代码
    public String StateCode;


}
