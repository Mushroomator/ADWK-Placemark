package de.tp.placemark.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.tp.placemark.helpers.exists
import de.tp.placemark.helpers.read
import de.tp.placemark.helpers.write
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

val JSON_FILE = "placemarks.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<PlacemarkModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class PlacemarkJSONStore : PlacemarkStore, AnkoLogger {

    val context: Context
    var placemarks = mutableListOf<PlacemarkModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<PlacemarkModel> {
        return placemarks
    }

    override fun create(placemark: PlacemarkModel) {
        placemark.id = generateRandomId()
        placemarks.add(placemark)
        serialize()
    }


    override fun update(placemark: PlacemarkModel) {
        var foundPlacemark: PlacemarkModel? = placemarks.find{ it.id == placemark.id }
        if (foundPlacemark != null && !foundPlacemark.equals(placemark)){
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.lat = placemark.lat
            foundPlacemark.lng = placemark.lng
            foundPlacemark.zoom = placemark.zoom
            info("Placemarks have been updated.")
            logAll()
            serialize()
        }
    }

    override fun delete(placemark: PlacemarkModel) {
        placemarks.remove(placemark)
        serialize()
    }

    override fun logAll() {
        placemarks.forEach{ info(it) }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(placemarks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        placemarks = Gson().fromJson(jsonString, listType)
    }
}
