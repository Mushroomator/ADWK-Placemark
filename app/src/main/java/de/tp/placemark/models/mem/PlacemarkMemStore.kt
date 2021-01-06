package de.tp.placemark.models.mem

import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.models.PlacemarkStore
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
    }

    override fun update(placemark: PlacemarkModel) {
        var foundPlacemark: PlacemarkModel? = findById(placemark.id)
        if (foundPlacemark != null){
            foundPlacemark.title = placemark.title
            foundPlacemark.description = placemark.description
            foundPlacemark.image = placemark.image
            foundPlacemark.lat = placemark.lat
            foundPlacemark.lng = placemark.lng
            foundPlacemark.zoom = placemark.zoom
            info("Placemarks have been updated.")
        }
    }

    override fun findById(id: Long): PlacemarkModel? {
        return placemarks.find { id == it.id }
    }

    override fun delete(placemark: PlacemarkModel) {
        placemarks.remove(placemark)
    }
}