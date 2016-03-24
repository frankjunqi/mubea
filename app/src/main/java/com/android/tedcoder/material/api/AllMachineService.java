package com.android.tedcoder.material.api;

import com.android.tedcoder.material.entity.allmachine.AllMachineResBody;
import com.android.tedcoder.material.entity.rawmaterial.RawMaterialResBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public interface AllMachineService {

    // 最大的cell的行数
    public static final int MAXCELLCOUNT = 7;


    @GET("/{srv}/{svc}/{queryname}")
    Call<AllMachineResBody> allMachineList(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);
}
