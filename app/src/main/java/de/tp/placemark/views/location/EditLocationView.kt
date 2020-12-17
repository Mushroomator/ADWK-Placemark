package de.tp.placemark.views.location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import de.tp.placemark.R
import de.tp.placemark.models.Location

class EditLocationView : AppCompatActivity(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

  private lateinit var map: GoogleMap
  var location = Location()
  lateinit var presenter: EditLocationPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_map)

    // create presenter
    presenter = EditLocationPresenter(this)

    location = intent.extras?.getParcelable<Location>("location")!! // get location object passed on by PlacemarkActivity
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync{
      map = it
      // registered listener for marker drag and click event
      map.setOnMarkerDragListener(this)
      map.setOnMarkerClickListener(this)
      presenter.initMap(map)
    }
  }

  override fun onBackPressed() {
    presenter.doOnBackPressed()
    super.onBackPressed()
  }

  override fun onMarkerDragStart(marker: Marker) {

  }

  override fun onMarkerDrag(marker: Marker) {

  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude, map.cameraPosition.zoom)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
  }
}