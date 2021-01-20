package com.weather.bp

import com.weather.bp.data.models.ConsolidatedWeather

val fakeWeather = ConsolidatedWeather(
    id= 0,
    created = "2021-01-19T10:12:55.401514Z",
    the_temp = 2.356,
    weather_state_name = "Clear",
    weather_state_abbr = "c"
)

val invalidDataException = Exception("Invalid Data")