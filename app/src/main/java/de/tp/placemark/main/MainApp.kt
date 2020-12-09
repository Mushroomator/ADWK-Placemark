package de.tp.placemark.main

import android.app.Application
import de.tp.placemark.models.PlacemarkModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp: Application(), AnkoLogger {

  val placemarks = ArrayList<PlacemarkModel>()


  override fun onCreate() {
    super.onCreate()
    info("Placemark app started")
  }
}