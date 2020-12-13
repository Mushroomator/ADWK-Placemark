package de.tp.placemark.main

import android.app.Application
import de.tp.placemark.models.PlacemarkJSONStore
import de.tp.placemark.models.PlacemarkMemStore
import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.models.PlacemarkStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

  lateinit var placemarks: PlacemarkStore

  override fun onCreate() {
    super.onCreate()
    placemarks = PlacemarkJSONStore(this.applicationContext)
    info("Placemark app started")
  }
}