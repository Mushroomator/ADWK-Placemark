package de.tp.placemark.views.placemark

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import de.tp.placemark.helpers.showImagePicker
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.Location
import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.views.location.EditLocationView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW


class PlacemarkPresenter(view: BaseView): BasePresenter(view), AnkoLogger {
  // ID to identify launched activities
  private val IMAGE_REQUEST = 1
  private val LOCATION_REQUEST = 2

  var placemark = PlacemarkModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)  // set WIT as default location
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
    view?.setResult(AppCompatActivity.RESULT_OK)
    view?.finish()
  }

  /**
   * Show Android's built in image picker activity
   */
  fun doSelectImage(title: String, description: String) {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST)
      // save current state (entered title + description), otherwise it will be lost as soon as the activity resumes
      placemark.title = title
      placemark.description = description
    }
  }


  /**
   * Start EditLocationView to set placemark location. Use default location as a starting point.
   * Title and description will be saved in attribute placemark for the user to not loose any input he has given
   * @param title title of placemark (that is in the input field at the moment)
   * @param description description of placemark (that is in the input field at the moment)
   */
  fun doSetLocation(title: String, description: String){
    // save current state (entered title + description), else it will be lost as soon as the activity resumes
    placemark.title = title
    placemark.description = description
    if (placemark.zoom != 0f) {// if zoom is 0 --> no location has been set yet --> use the default location --> else: use location stored in placemerkStore
      view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
    }
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(placemark.lat, placemark.lng, placemark.zoom))
  }

  /**
   * Handle finish of called activities.
  * @param title title of placemark (that is in the input field at the moment)
  * @param description description of placemark (that is in the input field at the moment)
  */
  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent){
    info("data: $data")
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
          view?.showPlacemark(placemark)
        }
      }
    }
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
