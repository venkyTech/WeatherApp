package com.wether.app.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherRepository
import com.wether.app.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Weather report view model
 */

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    var city: MutableState<String> = mutableStateOf("")

    val uiState: MutableState<WeatherResponseState> =
        mutableStateOf(WeatherResponseState.Loading(false))

    /**
     * get weather report based on the city
     */
    fun getWeatherReport() {
        showLoader()
        viewModelScope.launch {
            repository.getWeatherReport(city = city.value).catch {
               updateError()
            }.collectLatest { response ->
                response?.also {
                    uiState.value = WeatherResponseState.OnSuccess(it.mapToUIModel())
                } ?: updateError()
            }
        }
    }

    /**
     * show content loader
     */
    private fun showLoader() {
        uiState.value = WeatherResponseState.Loading(true)
    }

    /**
     * Update error in ui model
     */
    private fun updateError() {
        uiState.value = WeatherResponseState.OnFailed(Constants.API_ERROR_MSG)
    }
}