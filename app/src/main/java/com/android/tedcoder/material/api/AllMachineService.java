package com.android.tedcoder.material.api;

import com.android.tedcoder.material.entity.allmachine.AllMachineResBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public interface AllMachineService {

    @GET("/{srv}/{svc}/{queryname}")
    Call<AllMachineResBody> allMachineList(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);
}
