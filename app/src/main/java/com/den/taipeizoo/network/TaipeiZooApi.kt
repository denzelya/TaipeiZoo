package com.den.taipeizoo.network

import com.den.taipeizoo.models.ResponseDistrict
import com.den.taipeizoo.models.ResponsePlant
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TaipeiZooApi {

    @GET("api/v1/dataset/f18de02f-b6c9-47c0-8cda-50efad621c14?scope=resourceAquire")
    suspend fun getPlants(
        @Query("q") q: String = "",
        @Query("limit") limit: String = "",
        @Query("offset") offset: String = ""
    ): Response<ResponsePlant>

    @GET("/api/v1/dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a?scope=resourceAquire")
    suspend fun getDistricts() : Response<ResponseDistrict>

    companion object {
        operator fun invoke() :TaipeiZooApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://data.taipei")
                .build()
                .create(TaipeiZooApi:: class.java)
        }
    }
}