package de.tp.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.tp.placemark.R
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class PlacemarkActivity : AppCompatActivity(), AnkoLogger {

  var placemark = PlacemarkModel()
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Placemark Activity started.")
    setContentView(R.layout.activity_placemark)

    // get application (application is actually getApplication() and gets an attribute of superclass Application of Activity)
    app = application as MainApp  // as is the "unsafe" cast operator, which will do no type-checking at all;

    // set and enable toolbar
    toolbarPlacemarkActivity.title = title;
    setSupportActionBar(toolbarPlacemarkActivity)

    // set on click listener for Button
    btnAdd.setOnClickListener() {
      placemark.title = placemarkTitle.text.toString()
      placemark.description = placemarkDescription.text.toString()
      if (placemark.title.isNotEmpty()) {
        info("Placemark added:\n ${placemark}")
        app.placemarks.add(placemark.copy())
        for (i in app.placemarks.indices){
          info("Placemark[${i}:${app.placemarks[i]}")
        }
        setResult(AppCompatActivity.RESULT_OK)
        finish()
      }
      else {
        toast("Please Enter a title")
      }
    };
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