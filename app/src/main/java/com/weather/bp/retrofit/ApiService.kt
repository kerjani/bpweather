package com.weather.bp.retrofit

import com.weather.bp.data.models.LocationWeatherResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("location/804365")
    suspend fun getLocationWeather(): Response<LocationWeatherResponse>
}
