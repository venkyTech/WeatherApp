package com.wether.app.model

import com.wether.app.common.Constants.IMAGE_BASE_URL


/**
 * Weather Response
 * @param name city name
 * @param main Temperature info
 * @param weather  Weather information
 */
data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Location
)

/**
 * Temperature info
 * @param temp Temperature
 */
data class Main(
    val temp: Float,
    val humidity: Int,
    val pressure: Double
)

/**
 * Weather information
 * @param description weather description
 */
data class Weather(
    val main: String,
    val icon: String,
    val description: String
)

/**
 * Location data
 */
data class Location(
    val country: String
)

data class Wind(val speed: Double)

fun WeatherResponse.mapToUIModel(): WeatherData {
    val weather = this.weather.first()
    return WeatherData(
        temp = "${(this.main.temp)}",
        windSpeed = "${this.wind.speed}",
        humidity = "${this.main.humidity}",
        pressure = "${this.main.pressure}",
        description = weather.description,
        title = weather.main,
        icon = IMAGE_BASE_URL + weather.icon + ".png",
        location = "${this.name}, ${this.sys.country}"
    )

}

