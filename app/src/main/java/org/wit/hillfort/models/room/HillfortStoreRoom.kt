package org.wit.hillfort.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortStoreRoom(val context: Context) : HillfortStore
{

    var dao: HillfortDao

    init
    {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db").fallbackToDestructiveMigration().build()
        dao = database.hillfortDao()
    }

    suspend override fun findAll(): List<HillfortModel>
    {
        val deferredhillforts = bg {
            dao.findAll()
        }
        val hillforts = deferredhillforts.await()
        return hillforts
    }

    suspend override fun findById(id: Long): HillfortModel?
    {
        val deferredhillfort = bg {
            dao.findById(id)
        }
        val hillfort = deferredhillfort.await()
        return hillfort
    }

    suspend override fun create(hillfort: HillfortModel)
    {
        bg {
            dao.create(hillfort)
        }
    }

    suspend override fun update(hillfort: HillfortModel)
    {
        bg {
            dao.update(hillfort)
        }
    }

    suspend override fun delete(hillfort: HillfortModel)
    {
        bg {
            dao.deletehillfort(hillfort)
        }
    }

    override fun clear()
    {
    }
}