package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>()
{}.type

fun generateRandomId(): Long
{
    return Random().nextLong()
}

class HillfortSONStore : HillfortStore, AnkoLogger
{

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context)
    {
        this.context = context
        if (exists(context, JSON_FILE))
        {
            deserialize()
        }
    }

    suspend override fun findAll(): MutableList<HillfortModel>
    {
        return hillforts
    }

    suspend override fun findById(id: Long): HillfortModel?
    {
        val foundhillfort: HillfortModel? = hillforts.find { it.id == id }
        return foundhillfort
    }

    suspend override fun create(hillfort: HillfortModel)
    {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    suspend override fun update(hillfort: HillfortModel)
    {
        val hillfortsList = findAll() as ArrayList<HillfortModel>
        var foundhillfort: HillfortModel? = hillfortsList.find { p -> p.id == hillfort.id }
        if (foundhillfort != null)
        {
            foundhillfort.title = hillfort.title
            foundhillfort.description = hillfort.description
            foundhillfort.image = hillfort.image
            foundhillfort.lat = hillfort.lat
            foundhillfort.lng = hillfort.lng
            foundhillfort.zoom = hillfort.zoom
        }
        serialize()
    }

    suspend override fun delete(hillfort: HillfortModel)
    {
        hillforts.remove(hillfort)
        serialize()
    }

    private fun serialize()
    {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize()
    {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}
