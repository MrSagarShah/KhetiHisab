package com.hisab.kheti.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

const val DB_NAME = "master.sqlite"
const val DB_VERSION = 2

@Database(entities = [(Crop::class), (Category::class), (Transaction::class)],
        version = DB_VERSION)
@TypeConverters(StringDateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDataDao(): GetDataDao
    abstract fun getInsertUpdateDao(): InsertUpdateDao

}