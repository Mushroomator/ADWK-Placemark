package de.tp.placemark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.tp.placemark.R
import de.tp.placemark.helpers.readImage
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.helpers.showImagePicker
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.Location
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class PlacemarkActivity : AppCompatActivity(), AnkoLogger {

  var placemark = PlacemarkModel()
  lateinit var app: MainApp

  val IMAGE_REQUEST = 1 // ID to identify Activity-Response
  val LOCATION_REQUEST = 2

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Placemark Activity started.")
    setContentView(R.layout.activity_placemark)

    // get application (application is actually getApplication() and gets an attribute of superclass Application of Activity)
    app = application as MainApp  // as is the "unsafe" cast operator, which will do no type-checking at all;

    // set and enable toolbar
    toolbarPlacemarkActivity.title = title;
    setSupportActionBar(toolbarPlacemarkActivity)

    var edit = false;
    val editExtraKey = "placemark_edit"
    if (this.intent.hasExtra(editExtraKey)){
      edit = true
      if (placemark.image != null){
        btnChooseImage.setText(R.string.button_changeImage)
      }
      placemark = this.intent.extras?.getParcelable<PlacemarkModel>(editExtraKey)!!
      placemarkTitle.setText(placemark.title)
      placemarkDescription.setText(placemark.description)
      placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
      btnAdd.text = getString(R.string.button_savePlacemark)
    }

    // set on click listener for Button
    btnAdd.setOnClickListener() {
      placemark.title = placemarkTitle.text.toString()
      placemark.description = placemarkDescription.text.toString()
      if (placemark.title.isNotEmpty()) {
        if (edit){
          app.placemarks.update(placemark)
        }
        else{
          app.placemarks.create(placemark.copy())
          info("Placemark added:\n ${placemark}")
          app.placemarks.logAll()
          setResult(AppCompatActivity.RESULT_OK)
        }
        finish()
      }
      else {
        toast(getString(R.string.toast_enterPlacemarkTitle))
      }
    };

    btnChooseImage.setOnClickListener {
      showImagePicker(this, IMAGE_REQUEST)
    }

    btnSetLocation.setOnClickListener{
      val location = Location(52.245696, -7.139102, 15f)  // set WIT as default location
      if (placemark.zoom != 0f) {// if zoom is 0 --> no location has been set yet --> use the default locattion --> otherwise: use location stored in placemerkStore
        location.lat =  placemark.lat
        location.lng = placemark.lng
        location.zoom = placemark.zoom
      }
      startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when(requestCode){
      IMAGE_REQUEST -> {
        if (data != null) {
          placemark.image = data.getData().toString()
          placemarkImage.setImageBitmap(readImage(this, resultCode, data))
          btnChooseImage.setText(R.string.button_changeImage)
        }
      }
      LOCATION_REQUEST -> {
        if(data != null){
          this.location = data.extras?.getParcelable<Location>("location")!!
          placemark.lat = location.lat
          placemark.lng = location.lng
          placemark.zoom = location.zoom
        }
      }
    }
    super.onActivityResult(requestCode, resultCode, data)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_placemark_activity, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_cancel -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}