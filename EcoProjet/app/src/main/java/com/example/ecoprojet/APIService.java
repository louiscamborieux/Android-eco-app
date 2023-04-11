package com.example.ecoprojet;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIService {


    @GET("records")
    Call<JsonResponseList>listCharger(@Query("refine")List<String> refines ,
                                      @Query("offset")int offset,
                                      @Query("where")String clause);

    @GET("records")
    Call<JsonResponseList>listCharger(@QueryMap Map<String,Object> params);



    @GET
    Call<JsonResponseRecord> chargeur(@Url String url);



}
