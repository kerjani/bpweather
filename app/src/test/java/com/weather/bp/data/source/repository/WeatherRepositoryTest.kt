package com.weather.bp.data.source.repository

import com.weather.bp.util.Result
import com.weather.bp.MainCoroutineRule
import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.data.source.local.WeatherLocalDataSource
import com.weather.bp.data.source.remote.WeatherRemoteDataSource
import com.weather.bp.fakeWeather
import com.weather.bp.invalidDataException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    @Mock
    private lateinit var localDataSource: WeatherLocalDataSource

    private lateinit var systemUnderTest: WeatherRepositoryImpl

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest = WeatherRepositoryImpl(remoteDataSource, localDataSource, Dispatchers.Main)
    }

    @Test
    fun `assert that getWeather with refresh as true fetches successfully from the remote source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getConsolidatedWeatherData()).thenReturn(
                Result.Success(listOf(fakeWeather))
            )

            val response = systemUnderTest.getWeatherData( true)

            verify(remoteDataSource, times(1)).getConsolidatedWeatherData()
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Success -> {
                    val weather = response.data
                    assertThat<ConsolidatedWeather>(weather as ConsolidatedWeather, `is`(notNullValue()))
                    assertThat(weather.created, `is`(fakeWeather.created))
                    assertThat(weather.id, `is`(fakeWeather.id))
                    assertThat(weather.the_temp, `is`(fakeWeather.the_temp))
                    assertThat(weather.weather_state_abbr, `is`(fakeWeather.weather_state_abbr))
                    assertThat(weather.weather_state_name, `is`(fakeWeather.weather_state_name))
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as false fetches successfully from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getLatestWeatherForecast()).thenReturn(
                fakeWeather
            )

            val response = systemUnderTest.getWeatherData(false)

            verify(localDataSource, times(1)).getLatestWeatherForecast()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    val weather = response.data

                    assertThat<ConsolidatedWeather>(weather as ConsolidatedWeather, `is`(notNullValue()))
                    assertThat(weather.created, `is`(fakeWeather.created))
                    assertThat(weather.id, `is`(fakeWeather.id))
                    assertThat(weather.the_temp, `is`(fakeWeather.the_temp))
                    assertThat(weather.weather_state_abbr, `is`(fakeWeather.weather_state_abbr))
                    assertThat(weather.weather_state_name, `is`(fakeWeather.weather_state_name))
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as true returns fetches from the remote source but returns an Error`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getConsolidatedWeatherData()).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            val response = systemUnderTest.getWeatherData( true)

            verify(remoteDataSource, times(1)).getConsolidatedWeatherData()
            verifyNoMoreInteractions(localDataSource)

            when (response) {
                is Result.Error -> {
                    assertThat(response.exception, `is`(invalidDataException))
                }
            }
        }

    @Test
    fun `assert that getWeather with refresh as false fetches null data from the local source`() =
        mainCoroutineRule.runBlockingTest {
            `when`(localDataSource.getLatestWeatherForecast()).thenReturn(
                null
            )

            val response = systemUnderTest.getWeatherData( false)

            verify(localDataSource, times(1)).getLatestWeatherForecast()
            verifyNoMoreInteractions(remoteDataSource)

            when (response) {
                is Result.Success -> {
                    assertThat(response.data, `is`(nullValue()))
                }
            }
        }


    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}
