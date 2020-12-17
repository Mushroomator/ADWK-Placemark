package de.tp.placemark.views.placemarkList

import de.tp.placemark.activities.PlacemarkMapsActivity
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.views.placemark.PlacemarkView
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class PlacemarkListPresenter(val view: PlacemarkListView) {
  var app: MainApp

  init {
    app = view.application as MainApp
  }

  fun getPlacemarks() = app.placemarks.findAll()

  fun doAddPlacemark() {
    view.startActivityForResult<PlacemarkView>(0)
  }

  fun doEditPlacemark(placemark: PlacemarkModel) {
    view.startActivityForResult(view.intentFor<PlacemarkView>().putExtra("placemark_edit", placemark), 0)
  }

  fun doShowPlacemarksMap() {
    view.startActivity<PlacemarkMapsActivity>()
  }
}