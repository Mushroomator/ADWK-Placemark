package de.tp.placemark.views.placemark

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import de.tp.placemark.R
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.models.Location
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.placemark.views.BaseView

class PlacemarkView : BaseView(), AnkoLogger {

  lateinit var presenter: PlacemarkPresenter
  lateinit var map: GoogleMap
  var placemark = PlacemarkModel()

  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark)

    // initialize presenter
    presenter = initPresenter(PlacemarkPresenter(this)) as PlacemarkPresenter

    // set and enable toolbar
    init(toolbarPlacemarkView, true)

    // initialize MapView
    mapViewPV.onCreate(savedInstanceState)
    mapViewPV.getMapAsync{
      map = it
      presenter.doConfigureMap(map)

      // call activity EditLocation when map is clicked
      map.setOnMapClickListener {
        presenter.cachePlacemark(placemarkTitle.text.toString(), placemarkDescription.text.toString())
        presenter.doSetLocation()
      }
    }

    btnChooseImage.setOnClickListener {
      presenter.cachePlacemark(placemarkTitle.text.toString(), placemarkDescription.text.toString())
      presenter.doSelectImage()
    }
  }

  /**
   * Show placemark on screen
   * @param placemark Placemark to be displayed
   */
  override fun showPlacemark(placemark: PlacemarkModel){
    // only set title and text if they are empty (prevent overriding these values everytime the location is updated)
    if(placemarkTitle.text.isEmpty()) placemarkTitle.setText(placemark.title)
    if(placemarkDescription.text.isEmpty()) placemarkDescription.setText(placemark.description)
    // replace with glide
    // placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
    Glide.with(this).load(placemark.image).into(placemarkImage);

    btnChooseImage.setText(R.string.button_changeImage)
    showLocation(placemark.location)
  }

  /**
   * Display location on screen
   * @param location location to be displayed
   */
  override fun showLocation(location: Location) {
    etLat.setText("%.6f".format(location.lat))
    etLng.setText("%.6f".format(location.lng))
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_placemark_activity, menu)
    if(presenter.edit && menu != null){
      // display delete button
      menu.getItem(1).setVisible(true)
      // change item_save text to save instead of add
      menu.getItem(0).setTitle(R.string.menu_savePlacemark)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_save -> {
        if(placemarkTitle.text.toString().isEmpty()){
          toast(R.string.toast_enterPlacemarkTitle)
        }
        else {
          presenter.doAddOrSave(placemarkTitle.text.toString(), placemarkDescription.text.toString())
        }
      }
      R.id.item_cancel -> presenter.doCancel()
      R.id.item_delete -> presenter.doDelete()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onDestroy() {
    super.onDestroy()
    mapViewPV.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    mapViewPV.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapViewPV.onResume()
    presenter.doRestartLocationUpdates()  // (Re-)start location updates (they will automatically stop as soon as the ativity is destroyed (implemented that way in Google API))
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapViewPV.onLowMemory()
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapViewPV.onSaveInstanceState(outState)
  }

}