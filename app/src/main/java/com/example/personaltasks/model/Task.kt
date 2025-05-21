package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Task(

    var dueDate: Date,

    var title: String = "",

    var description: String = "",

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

): Parcelable
