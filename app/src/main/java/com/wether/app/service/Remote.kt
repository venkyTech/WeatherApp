package com.wether.app.service

import com.wether.app.service.WeatherApiService
import javax.inject.Inject

/**
 * App Remote service
 */
class Remote @Inject constructor(private val api: WeatherApiService) {
    /**
     * get weather report
     */
    suspend fun getWeatherReport(city: String) = api.getWeather(city).body()
}