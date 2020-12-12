package de.tp.placemark.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.atomic.AtomicLong

var lastId: AtomicLong = AtomicLong(0L)

internal fun getId(): Long {
    return lastId.addAndGet(1)
}

class PlacemarkMemStore: PlacemarkStore, AnkoLogger {

    val placemarks = ArrayList<PlacemarkModel>()

    override fun findAll(): List<PlacemarkModel> {
        return this.placemarks
    }

    override fun create(placemark: PlacemarkModel) {
        placemark.id = getId()
        placemarks.add(placemark)
        logAll()
    }

    override fun logAll() {
        this.placemarks.forEach{ info("${it}") }
    }

    override fun update(placemark: PlacemarkModel) {
        var foundPlacemark: PlacemarkModel? = placemarks.find{ it.id == placemark.id }
        if (foundPlacemark != null){
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            info("Placemarks have been updated.")
            logAll()
        }
    }
}