package com.example.personaltasks.model

import androidx.room.TypeConverter
import java.util.Date

class Converter {
    @TypeConverter
    fun parseDateToTimestamp(date: Date?):Long?{ return date?.time  }

    @TypeConverter
    fun parseTimestampToDate(value:Long): Date { return Date(value)}

}

