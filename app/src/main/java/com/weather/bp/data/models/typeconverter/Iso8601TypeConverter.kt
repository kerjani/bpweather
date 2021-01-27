package com.weather.bp.data.models.typeconverter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

class Iso8601TypeConverter { // TODO use joda time -> lower minimum API level will be possible

    @TypeConverter
    fun stringToDateTimeInMillis(iso8601Date: String): Long {
        val temporalAccessor: TemporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(iso8601Date)
        val instant: Instant = Instant.from(temporalAccessor)
        return Date.from(instant).time
    }

    @TypeConverter
    fun dateTimeInMillisToString(dateInMillis: Long): String {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(dateInMillis))
    }

}
