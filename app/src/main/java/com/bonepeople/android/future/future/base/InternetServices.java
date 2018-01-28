package com.bonepeople.android.future.future.base;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络连接接口
 * <p>
 * Created by bonepeople on 2018/1/22.
 */

public interface InternetServices {

    @GET("users/{userName}")
    Call<ResponseBody> basicInfo(@Path("userName") String userName);

    @FormUrlEncoded
    @POST("cons/listPage")
    Observable<ResponseBody> getConList(@Field("isApp") boolean isApp
            , @Field("appCityId") int appCityId
            , @Field("nativePlaceId") String nativePlaceId
            , @Field("sortType") int sortType
            , @Field("startPage") int startPage);
}