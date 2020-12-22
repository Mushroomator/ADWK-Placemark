package de.tp.placemark.views.placemark

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import de.tp.placemark.R
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.wit.placemark.views.BaseView

class PlacemarkView : BaseView(), AnkoLogger {

  lateinit var presenter: PlacemarkPresenter
  lateinit var map: GoogleMap
  var placemark = PlacemarkModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark)

    // initialize presenter
    presenter = initPresenter(PlacemarkPresenter(this)) as PlacemarkPresenter

    // set and enable toolbar
    init(toolbarPlacemarkView)

    // initialize MapView
    mapViewPV.onCreate(savedInstanceState)
    mapViewPV.getMapAsync{
      map = it
      presenter.doConfigureMap(map)
    }

    btnChooseImage.setOnClickListener {
      presenter.cachePlacemark(placemarkTitle.text.toString(), placemarkDescription.text.toString())
      presenter.doSelectImage()
    }

    btnSetLocation.setOnClickListener{
      presenter.cachePlacemark(placemarkTitle.text.toString(), placemarkDescription.text.toString())
      presenter.doSetLocation()
    }
  }

  /**
   * Show placemark on screen
   * @param placemark Placemark to be displayed
   */
  override fun showPlacemark(placemark: PlacemarkModel){
    placemarkTitle.setText(placemark.title)
    placemarkDescription.setText(placemark.description)
    placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
    btnChooseImage.setText(R.string.button_changeImage)
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