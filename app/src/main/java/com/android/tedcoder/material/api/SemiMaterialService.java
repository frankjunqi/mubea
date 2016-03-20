package com.android.tedcoder.material.api;

import com.android.tedcoder.material.entity.semimaterial.SemiMaterialResBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public interface SemiMaterialService {
    @GET("/{srv}/{svc}/{queryname}")
    Call<SemiMaterialResBody> semiMaterialList(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);
}
