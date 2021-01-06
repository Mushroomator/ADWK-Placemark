package de.tp.placemark.main

import android.app.Application
import de.tp.placemark.models.PlacemarkStore
import de.tp.placemark.models.room.PlacemarkStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

  lateinit var placemarks: PlacemarkStore

  override fun onCreate() {
    super.onCreate()
    placemarks = PlacemarkStoreRoom(this.applicationContext)
    info("Placemark app started")
  }
}