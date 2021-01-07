package de.tp.placemark.views.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.placemark.main.MainApp
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.placemark.views.BasePresenter

class PlacemarkMapPresenter(view: PlacemarkMapView): BasePresenter(view) {

  init {
    app = view.application as MainApp
  }

  fun doPopulateMap(map: GoogleMap) {
    map.uiSettings.setZoomControlsEnabled(true)

    doAsync {
      val placemarks = app.placemarks.findAll()
      uiThread {
        placemarks.forEach{
          val loc = LatLng(it.location.lat, it.location.lng)
          val options = MarkerOptions().title(it.title).position(loc)
          map.addMarker(options).tag = it.id  // add the marker on the map and a tag to the marker to be able to identify which marker/ Placemark was clicked on later on
          map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom)) // zoom in to the placemark (should actually be refatored to be out of iteration as it only needs to be done once as only one placemark can be in focus)
        }
      }
    }


  }

  fun doMarkerSelected(marker: Marker){
    val tag = marker.tag as Long
    doAsync {
      val currentPlacemark = app.placemarks.findById(tag)
      uiThread {
        if(currentPlacemark != null){
          view?.showPlacemark(currentPlacemark)
        }
      }
    }
  }

  fun loadPlacemarks(){
    view?.showPlacemarks(app.placemarks.findAll())
  }
}