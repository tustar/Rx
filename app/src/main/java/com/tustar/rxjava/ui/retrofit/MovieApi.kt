package com.tustar.rxjava.ui.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("top250")
    fun listTop250(@Query("start") start: Int,
                   @Query("count") count: Int): Observable<MovieBean>
}