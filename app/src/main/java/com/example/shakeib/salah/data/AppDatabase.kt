package com.example.shakeib.salah.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [SalahTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun salahDao():SalahDao
}