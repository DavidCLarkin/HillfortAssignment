package org.wit.hillfort.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.hillfort.models.HillfortModel

@Database(entities = arrayOf(HillfortModel::class), version = 1)
abstract class Database : RoomDatabase()
{

    abstract fun hillfortDao(): HillfortDao
}