package com.example.shakeib.salah.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SalahTable(
    @PrimaryKey(autoGenerate = true) var id:Int,
    @ColumnInfo var fajr:String,
    @ColumnInfo var sunrise:String,
    @ColumnInfo var dhuhr:String,
    @ColumnInfo var asr:String,
    @ColumnInfo var magrib:String,
    @ColumnInfo var isha:String,
    @ColumnInfo var date:String
)