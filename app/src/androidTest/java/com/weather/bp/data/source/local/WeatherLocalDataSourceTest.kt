package com.weather.bp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.weather.bp.MainCoroutineRule
import com.weather.bp.data.AppDatabase
import com.weather.bp.fakeWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class WeatherLocalDataSourceTest {

    private lateinit var database: AppDatabase

    private lateinit var systemUnderTest: WeatherLocalDataSourceImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = WeatherLocalDataSourceImpl(database.weatherDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveWeather_getLatestWeatherForecast_returnWeather() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveWeather(listOf(fakeWeather))

        val returnedWeather = systemUnderTest.getLatestWeatherForecast()

        assertThat(returnedWeather, `is`(notNullValue()))

        assertThat(returnedWeather, `is`(fakeWeather))
        assertThat(returnedWeather!!.weather_state_name, `is`(fakeWeather.weather_state_name))
        assertThat(returnedWeather.weather_state_abbr, `is`(fakeWeather.weather_state_abbr))
        assertThat(returnedWeather.the_temp, `is`(fakeWeather.the_temp))
        assertThat(returnedWeather.created, `is`(fakeWeather.created))

    }

    @Test
    fun deleteWeather_getWeatherReturnsNull() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.saveWeather(listOf(fakeWeather))
        systemUnderTest.deleteWeather()

        val result = systemUnderTest.getLatestWeatherForecast()
        assertThat(result, `is`(nullValue()))
    }

}
