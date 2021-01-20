package com.weather.bp.ui.main

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.weather.bp.MainCoroutineRule
import com.weather.bp.data.source.repository.WeatherRepository
import com.weather.bp.fakeWeather
import com.weather.bp.getOrAwaitValue
import com.weather.bp.invalidDataException
import com.weather.bp.util.RefreshIntervalHelper
import com.weather.bp.util.RefreshIntervalHelperIml
import com.weather.bp.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainViewModelTest {

    private var repository: WeatherRepository = mock(WeatherRepository::class.java)

    private var refreshIntervalHelper: RefreshIntervalHelper = mock(RefreshIntervalHelper::class.java)

    private lateinit var systemUnderTest: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        systemUnderTest =
            MainViewModel(repository, refreshIntervalHelper)
    }

    @Test
    fun `assert that getWeather receives weather data from the repository successfully `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData(false)).thenReturn(
                Result.Success(
                    fakeWeather
                )
            )
            `when`(refreshIntervalHelper.isDataInvalid()).thenReturn(false)

            systemUnderTest.getWeather()
            verify(repository, times(1)).getWeatherData(false)

            assertThat(systemUnderTest.latestWeatherForecast.getOrAwaitValue(), `is`(fakeWeather))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(nullValue()))
        }

    @Test
    fun `assert that getWeather receives null data from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData(false)).thenReturn(Result.Success(null))
            `when`(repository.getWeatherData(true)).thenReturn(Result.Success(null))

            systemUnderTest.getWeather()
            verify(repository, times(1)).getWeatherData( false)
            verify(repository, times(1)).getWeatherData( true)

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that getWeather receives an error from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData(false)).thenReturn(Result.Success(null))
            `when`(repository.getWeatherData(true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.getWeather()
            verify(repository, times(1)).getWeatherData( false)
            verify(repository, times(1)).getWeatherData( true)

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(invalidDataException.message))
        }

    @Test
    fun `assert that refreshWeather receives weather data from the repository successfully `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData( true)).thenReturn(
                Result.Success(
                    fakeWeather
                )
            )

            systemUnderTest.refreshWeather()
            verify(repository, times(1)).getWeatherData( true)

            assertThat(systemUnderTest.latestWeatherForecast.getOrAwaitValue(), `is`(fakeWeather))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(nullValue()))
        }

    @Test
    fun `assert that refreshWeather receives null data from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData(true)).thenReturn(Result.Success(null))

            systemUnderTest.refreshWeather()
            verify(repository, times(1)).getWeatherData(true)

            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(not(nullValue())))
            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
        }

    @Test
    fun `assert that refreshWeather receives an error from the repository `() =
        mainCoroutineRule.runBlockingTest {
            `when`(repository.getWeatherData(true)).thenReturn(
                Result.Error(
                    invalidDataException
                )
            )

            systemUnderTest.refreshWeather()

            assertThat(systemUnderTest.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(systemUnderTest.error.getOrAwaitValue(), `is`(invalidDataException.message))
        }
}
