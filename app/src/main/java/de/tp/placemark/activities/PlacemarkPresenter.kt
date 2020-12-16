package de.tp.placemark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import de.tp.placemark.helpers.showImagePicker
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.Location
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.intentFor


class PlacemarkPresenter(val view: PlacemarkView) {
  // ID to identify launched activities
  val IMAGE_REQUEST = 1
  val LOCATION_REQUEST = 2

  var placemark = PlacemarkModel()
  var location = Location(52.245696, -7.139102, 15f)  // set WIT as default location
  var app: MainApp
  var edit = false

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
    view.setResult(AppCompatActivity.RESULT_OK)
    view.finish()
  }

  /**
   * Start MapActivity to set placemark location. Use default location as a starting point.
   */
  fun doSetLocation(){
    if (placemark.zoom != 0f) {// if zoom is 0 --> no location has been set yet --> use the default locattion --> otherwise: use location stored in placemerkStore
      location.lat = placemark.lat
      location.lng = placemark.lng
      location.zoom = placemark.zoom
    }
    view.startActivityForResult(view.intentFor<PlacemarkMapsActivity>().putExtra("location", location), LOCATION_REQUEST)
  }

  fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    when(requestCode){
      IMAGE_REQUEST -> {
        if (data != null) {
          placemark.image = data.data.toString()
          view.showPlacemark(placemark)
        }
      }
      LOCATION_REQUEST -> {
        if(data != null){
          val location = data.extras?.getParcelable<Location>("location")!!
          placemark.lat = location.lat
          placemark.lng = location.lng
          placemark.zoom = location.zoom
        }
      }
    }
  }

  /**
   * Cancel view. End without modifying
   */
  fun doCancel() {
    view.finish()
  }

  /**
   * Delete the placemark
   */
  fun doDelete() {
    app.placemarks.delete(placemark)
    view.finish()
  }

  /**
   * Show Android's built in image picker
   */
  fun doSelectImage() {
    showImagePicker(view, IMAGE_REQUEST)
  }


}
