package com.example.shakeib.salah.data

import android.arch.persistence.db.SupportSQLiteQueryBuilder
import android.arch.persistence.room.*
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.RawQuery



@Dao
interface SalahDao{
    @Insert
    fun insertAll(salah:List<SalahTable>)

    @Query("SELECT * FROM SalahTable WHERE date = :date")
    fun selectAll(date:String):List<SalahTable>

    @Query("DELETE FROM SalahTable")
    fun deleteAll()

    /*@SkipQueryVerification
    @Query("DELETE FROM sqlite_sequence")
    fun deleteSqliteSeq()*/


}