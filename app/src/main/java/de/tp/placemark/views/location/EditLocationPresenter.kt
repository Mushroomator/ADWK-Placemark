package de.tp.placemark.views.location


import android.app.Activity
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.placemark.models.Location
import de.tp.placemark.views.location.EditLocationView

class EditLocationPresenter(val view: EditLocationView) {

  var location = Location()
  init {
    location = view.intent.extras?.getParcelable<Location>("location")!!
  }

  /**
   * Initialize Google Map
   */
  fun initMap(map: GoogleMap){
    val loc = LatLng(location.lat, location.lng)
    val options = MarkerOptions()
            .title("Placemark")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
    map.addMarker(options)
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
  }

  /**
   * Update the location
   */
  fun doUpdateLocation(lat: Double, lng: Double, zoom: Float) {
    location.lat = lat
    location.lng = lng
    location.zoom = zoom
  }

  /**
   * Return to previous activity and pass the (modified) location
   */
  fun doOnBackPressed() {
    val resultIntent = Intent()
    resultIntent.putExtra("location", location)
    view.setResult(Activity.RESULT_OK, resultIntent)
    view.finish()
  }

  /**
   * Update the location of the marker and the text according to its position
   */
  fun doUpdateMarker(marker: Marker) {
    val loc = LatLng(location.lat, location.lng)
    marker.setSnippet("GPS : " + loc.toString())
  }
}