package com.wether.app.service

import com.wether.app.common.Constants
import com.wether.app.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Weather report api service
 */
interface WeatherApiService {
    /**
     * get weather report based on the city
     */
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>

}