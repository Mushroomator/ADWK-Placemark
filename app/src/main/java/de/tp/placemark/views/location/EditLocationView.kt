package de.tp.placemark.views.location

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import de.tp.placemark.R
import de.tp.placemark.models.Location
import kotlinx.android.synthetic.main.activity_edit_location.*
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.info
import org.wit.placemark.views.BaseView

class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

  private lateinit var map: GoogleMap
  var location = Location()
  lateinit var presenter: EditLocationPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_location)

    // create presenter
    presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

    // create and setup toolbar
    init(toolbarEL, true)

    location = intent.extras?.getParcelable<Location>("location")!! // get location object passed on by PlacemarkActivity

    mapViewEL.onCreate(savedInstanceState)
    mapViewEL.getMapAsync{
      map = it
      // registered listener for marker drag and click event
      map.setOnMarkerDragListener(this)
      map.setOnMarkerClickListener(this)
      presenter.initMap(map)

      // update lat, lng labels accordingly
      updateLatLngLabels(location.lat, location.lng)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_edit_location_acitvity, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_saveLocation -> presenter.doOnBackPressed()
      }
    return super.onOptionsItemSelected(item)
  }

  private fun updateLatLngLabels(lat: Double, lng: Double){
    // update latitude and longitude as the marker is dragged
    tvLatVal.setText("%.6f".format(lat))
    tvLngVal.setText("%.6f".format(lng))
  }

  override fun onBackPressed() {
    presenter.doOnBackPressed()
    super.onBackPressed()
  }

  override fun onMarkerDragStart(marker: Marker) {

  }

  /**
   * Fired when marker is dragged
   */
  override fun onMarkerDrag(marker: Marker) {
    updateLatLngLabels(marker.position.latitude, marker.position.longitude)
  }

  override fun onMarkerDragEnd(marker: Marker) {
    presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude, map.cameraPosition.zoom)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    presenter.doUpdateMarker(marker)
    return false
  }

  override fun onDestroy() {
    super.onDestroy()
    mapViewEL.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    mapViewEL.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapViewEL.onResume()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapViewEL.onLowMemory()
  }
}