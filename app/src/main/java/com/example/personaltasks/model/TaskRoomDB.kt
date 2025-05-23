package com.example.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract  class TaskRoomDB: RoomDatabase() {
    abstract  fun taskDao(): TaskDao
}