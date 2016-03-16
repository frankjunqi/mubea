package com.android.tedcoder.androidvideoplayer.rawmaterial.entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kjh08490 on 2016/3/16.
 */
public interface RequestBody {
    @GET("/{srv}/{svc}/{queryname}")
    Call<List<Test>> contributors(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);
/*    Call<RawMaterialResBody> contributors(
            @Path("srv") String srv,
            @Path("svc") String svc,
            @Path("queryname") String queryname);*/
}
