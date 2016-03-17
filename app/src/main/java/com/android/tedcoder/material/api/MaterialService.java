package com.android.tedcoder.material.api;

import com.android.tedcoder.material.entity.RawMaterialResBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public interface MaterialService {
    @GET("/{srv}/{svc}/{queryname}")
    Call<RawMaterialResBody> rawMaterialList(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);
}
