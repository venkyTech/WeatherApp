package com.example.weatherapp.api

import com.wether.app.model.WeatherResponse
import com.wether.app.service.Remote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Weather report repository
 */
class WeatherRepository @Inject constructor(private val remote: Remote) {

    /**
     * get weather report
     */
    fun getWeatherReport(city: String): Flow<WeatherResponse?> {
        return flow {
           emit(remote.getWeatherReport(city))
        }
    }
}