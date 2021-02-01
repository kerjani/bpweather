package com.weather.bp.di

import android.content.Context
import com.weather.bp.BuildConfig
import com.weather.bp.data.AppDatabase
import com.weather.bp.data.models.typeconverter.Iso8601TypeConverter
import com.weather.bp.retrofit.ApiService
import com.weather.bp.util.Constants
import com.weather.bp.util.Constants.BASE_API_URL
import com.weather.bp.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_API_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @IoCouroutineDispatcher
    fun providesIoCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    @DefaultCouroutineDispatcher
    fun providesDefaultCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Singleton
    @Provides
    fun provideSharedPreferencesHelper(@ApplicationContext appContext: Context) =
        SharedPreferencesHelper(appContext)

    @Provides
    fun provideDateFormat(): DateFormat = SimpleDateFormat(Constants.DATE_FORMAT)

    @Provides
    fun providesDateConverter() = Iso8601TypeConverter()

}