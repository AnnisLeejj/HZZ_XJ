package com.heking.hzz.Helper.HttpHelper;

import com.heking.hzz.Model.Test2;
import com.heking.hzz.Model.Zhengfudaohang;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Lee on 2017/10/18.
 */

public interface HttpApi {
    @GET
    Single<String> get(@Url String Url);

    @GET
    Call<String> getString(@Url String Url);

    @GET
    Single<List<Zhengfudaohang>> zfdh(@Url String Url);

    @GET
    Single<Test2> test(@Url String Url);
}
