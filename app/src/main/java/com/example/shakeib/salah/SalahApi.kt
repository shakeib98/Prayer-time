package com.example.shakeib.salah

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SalahApi {
    @GET("v1/calendarByCity")
    fun getTimings(@Query("city") city:String, @Query("country") country:String, @Query("method") method:Int, @Query("year") year:Int, @Query("month") month:Int): Call<SalahResponse>
}