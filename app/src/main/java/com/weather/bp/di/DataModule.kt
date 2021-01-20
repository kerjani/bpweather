package com.weather.bp.di

import com.weather.bp.data.source.local.WeatherLocalDataSource
import com.weather.bp.data.source.local.WeatherLocalDataSourceImpl
import com.weather.bp.data.source.remote.WeatherRemoteDataSource
import com.weather.bp.data.source.remote.WeatherRemoteDataSourceImpl
import com.weather.bp.data.source.repository.WeatherRepository
import com.weather.bp.data.source.repository.WeatherRepositoryImpl
import com.weather.bp.util.RefreshIntervalHelper
import com.weather.bp.util.RefreshIntervalHelperIml
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun providesWeatherLocalDataSource(
        weatherLocalDataSourceImpl: WeatherLocalDataSourceImpl
    ): WeatherLocalDataSource

    @Binds
    abstract fun providesWeatherRemoteDataSource(
        weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource

    @Binds
    abstract fun providesWeatherRepository(
        repositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    abstract fun providesRefreshIntervalHelper(
        repositoryImpl: RefreshIntervalHelperIml
    ): RefreshIntervalHelper
}