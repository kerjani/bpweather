package com.weather.bp.data.source.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.weather.bp.MainCoroutineRule
import com.weather.bp.data.AppDatabase
import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.fakeWeather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class WeatherDaoTest {

    private lateinit var database: AppDatabase

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        systemUnderTest = database.weatherDao()
    }

    @After
    fun cleanUp() {
        database.close()
    }

    private lateinit var systemUnderTest: WeatherDao

    @Test
    fun insertWeather_verifyWeatherDbIsNotEmpty() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertAll(listOf(fakeWeather))

        val weather = systemUnderTest.getLatestWeatherForecast()

        assertThat<ConsolidatedWeather>(weather, `is`(notNullValue()))
        assertThat(weather.created, `is`(fakeWeather.created))
        assertThat(weather.the_temp, `is`(fakeWeather.the_temp))
        assertThat(weather.weather_state_abbr, `is`(fakeWeather.weather_state_abbr))
        assertThat(weather.weather_state_name, `is`(fakeWeather.weather_state_name))
    }

    @Test
    fun insertWeatherWithSameId_ReplaceOnConflict_returnNewlyInsertedWeather() =
        mainCoroutineRule.runBlockingTest {
            val newWeatherEntity = ConsolidatedWeather(
                "1",
                fakeWeather.id,
                3.4,
                "",
                ""
            )
            // Insert first weather
            systemUnderTest.insertAll(listOf(fakeWeather))

            // Insert new weather with same id
            systemUnderTest.insertAll(listOf(newWeatherEntity))

            val weather = systemUnderTest.getLatestWeatherForecast()

            assertThat<ConsolidatedWeather>(weather, `is`(notNullValue()))
            assertThat(weather, `is`(newWeatherEntity))
            assertThat(weather.weather_state_name, `is`(newWeatherEntity.weather_state_name))
            assertThat(weather.weather_state_abbr, `is`(newWeatherEntity.weather_state_abbr))
            assertThat(weather.the_temp, `is`(newWeatherEntity.the_temp))
            assertThat(weather.created, `is`(newWeatherEntity.created))
        }

    @Test
    fun deleteWeather_returnNullValue() = mainCoroutineRule.runBlockingTest {
        systemUnderTest.insertAll(listOf(fakeWeather))
        systemUnderTest.deleteAll()

        val weather = systemUnderTest.getLatestWeatherForecast()

        assertThat(weather, `is`(nullValue()))
    }

}
