package com.weather.bp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weather.bp.data.models.ConsolidatedWeather
import com.weather.bp.data.models.typeconverter.ConsolidatedWeatherTypeConverter
import com.weather.bp.data.models.typeconverter.Iso8601TypeConverter
import com.weather.bp.data.source.local.dao.WeatherDao

@Database(entities = [ConsolidatedWeather::class], version = 1, exportSchema = false)
@TypeConverters(ConsolidatedWeatherTypeConverter::class, Iso8601TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        /**
         * Volatile forces all accesses (read or write) to the volatile variable to occur to main memory.
         * Under the JSR 133 memory model, it is still true that volatile variables cannot be reordered with each other.
         */
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
    }

}
