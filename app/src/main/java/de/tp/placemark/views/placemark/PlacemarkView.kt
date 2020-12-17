package de.tp.placemark.views.placemark

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import de.tp.placemark.R
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.views.location.EditLocationView
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class PlacemarkView : AppCompatActivity(), AnkoLogger {

  lateinit var presenter: PlacemarkPresenter
  var placemark = PlacemarkModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark)

    // initialize presenter
    presenter = PlacemarkPresenter(this)

    // set and enable toolbar
    toolbarPlacemarkView.title = title;
    setSupportActionBar(toolbarPlacemarkView)

    // set on click listener for Add-Button
    btnAdd.setOnClickListener() {
      if(placemarkTitle.text.toString().isEmpty()){
        toast(R.string.toast_enterPlacemarkTitle)
      }
      else {
        presenter.doAddOrSave(placemarkTitle.text.toString(), placemarkDescription.text.toString())
      }
    };

    btnChooseImage.setOnClickListener { presenter.doSelectImage() }

    btnSetLocation.setOnClickListener{ presenter.doSetLocation() }
  }

  /**
   * Show placemark on screen
   * @param placemark Placemark to be displayed
   */
  fun showPlacemark(placemark: PlacemarkModel){
    placemarkTitle.setText(placemark.title)
    placemarkDescription.setText(placemark.description)
    placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
    if(placemark.image != null){
      btnChooseImage.setText(R.string.button_changeImage)
    }
    btnAdd.setText(R.string.button_savePlacemark)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    presenter.doActivityResult(requestCode, requestCode, data)
    super.onActivityResult(requestCode, resultCode, data)
    intentFor<EditLocationView>()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_placemark_activity, menu)
    if(presenter.edit && menu != null){
      menu.getItem(0).setVisible(true)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_cancel -> presenter.doCancel()
      R.id.item_delete -> presenter.doDelete()
    }
    return super.onOptionsItemSelected(item)
  }
}