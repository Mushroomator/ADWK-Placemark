package de.tp.placemark.views.placemark

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.placemark.helpers.checkLocationPermissions
import de.tp.placemark.helpers.createDefaultLocationRequest
import de.tp.placemark.helpers.isPermissionGranted
import de.tp.placemark.helpers.showImagePicker
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.Location
import de.tp.placemark.models.PlacemarkModel
import org.jetbrains.anko.AnkoLogger
import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW


class PlacemarkPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
  // ID to identify launched activities
  private val IMAGE_REQUEST = 1
  private val LOCATION_REQUEST = 2

  var placemark = PlacemarkModel()
  var edit = false

  // Location
  var map: GoogleMap? = null
  var defaultLocation = Location(52.245696, -7.139102, 15f)  // set WIT as default location
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  init {
    // get application (application is actually getApplication() and gets an attribute of superclass Application of Activity)
    app = view.application as MainApp  // as is the "unsafe" cast operator, which will do no type-checking at all;

    // determine mode and show placemark if required
    val editExtraKey = "placemark_edit"
    if (view.intent.hasExtra(editExtraKey)) {
      edit = true
      placemark = view.intent.extras?.getParcelable<PlacemarkModel>(editExtraKey)!!
      view.showPlacemark(placemark)
    }
    else{
      if(checkLocationPermissions(view)){
        doSetCurrentLocation()
      }

      placemark.lat = defaultLocation.lat
      placemark.lng = defaultLocation.lng
    }
  }

  /**
   * Callback called after request for a permission has been issued
   * @param requestCode ID/ code for the request
   * @param permissions requested permissions
   */
  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      // permissions denied, so use the default location
      locationUpdate(defaultLocation.lat, defaultLocation.lng)
    }
  }

  @SuppressLint("MissingPermission")
  fun doRestartLocationUpdates(){
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if(locationResult != null && locationResult.lastLocation != null){
          val l = locationResult.locations.last()
          locationUpdate(l.latitude, l.longitude)
        }
      }
    }
    if(!edit){
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation(){
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(it.latitude, it.longitude)
    }
  }

  /**
   * Add or save a placemark and return to caller view.
   * @param title title of placemark
   * @param description description of placemark
   */
  fun doAddOrSave(title: String, description: String) {
    placemark.title = title
    placemark.description = description
    if (edit) {
      app.placemarks.update(placemark)
    } else {
      app.placemarks.create(placemark.copy())
    }
    app.placemarks.logAll()
    view?.setResult(AppCompatActivity.RESULT_OK)
    view?.finish()
  }

  /**
   * Show Android's built in image picker activity
   */
  fun doSelectImage() {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
    }
  }

  /**
   * Save current state of placemark before calling other activities.
   * @param title title of a placemark
   * @param description description of a placemark
   * @author Thomas Pilz
   */
  fun cachePlacemark(title: String, description: String){
    // save current state (entered title + description), otherwise it will be lost as soon as the activity resumes
    placemark.title = title
    placemark.description = description
  }

  /**
   * Start EditLocationView to set placemark location. Use default location as a starting point.
   */
  fun doSetLocation(){
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placemark.lat, placemark.lng, placemark.zoom))
  }

  /**
   * Handle finish of called activities.
  */
  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent){
    when(requestCode){
      IMAGE_REQUEST -> {
        if (data != null) {
          placemark.image = data.data.toString()
          view?.showPlacemark(placemark)
        }
      }
      LOCATION_REQUEST -> {
        if(data != null){
          val location = data.extras?.getParcelable<Location>("location")!!
          placemark.lat = location.lat
          placemark.lng = location.lng
          placemark.zoom = location.zoom
          locationUpdate(placemark.lat, placemark.lng)
          view?.showPlacemark(placemark)
        }
      }
    }
  }

  fun doConfigureMap(map: GoogleMap){
    this.map = map
    locationUpdate(placemark.lat, placemark.lng)
  }

  fun locationUpdate(lat: Double, lng: Double){
    placemark.lat = lat
    placemark.lng = lng
    placemark.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options = MarkerOptions().title(placemark.title).position(LatLng(placemark.lat, placemark.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(placemark.lat, placemark.lng), placemark.zoom))
    view?.showPlacemark(placemark)
  }

  /**
   * Cancel view. End without modifying
   */
  fun doCancel() {
    view?.finish()
  }

  /**
   * Delete the placemark
   */
  fun doDelete() {
    app.placemarks.delete(placemark)
    view?.finish()
  }

}
