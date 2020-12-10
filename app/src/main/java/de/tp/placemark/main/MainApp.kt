package de.tp.placemark.main

import android.app.Application
import de.tp.placemark.models.PlacemarkMemStore
import de.tp.placemark.models.PlacemarkModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

  val placemarks = PlacemarkMemStore()


  override fun onCreate() {
    super.onCreate()
    info("Placemark app started")
  }
}