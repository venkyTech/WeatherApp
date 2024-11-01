package com.wether.app.model

sealed class WeatherResponseState {

    data class Loading(val isLoading: Boolean) : WeatherResponseState()

    data class OnSuccess(val uiModel: WeatherData) : WeatherResponseState()

    data class OnFailed(val errorMsg: String) : WeatherResponseState()
}
