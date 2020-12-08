package de.tp.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.tp.placemark.R
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class PlacemarkActivity : AppCompatActivity(), AnkoLogger {

  var placemark = PlacemarkModel()
  val placemarks = ArrayList<PlacemarkModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    info("Placemark Activity started.")
    setContentView(R.layout.activity_placemark)

    // set on click listener for Button
    btnAdd.setOnClickListener() {
      placemark.title = placemarkTitle.text.toString()
      placemark.description = placemarkDescription.text.toString()
      if (placemark.title.isNotEmpty()) {
        info("Placemark added:\n ${placemark}")
        placemarks.add(placemark.copy())
        for (i in placemarks.indices){
          info("Placemark[${i}:${this.placemarks[i]}")
        }
      }
      else {
        toast("Please Enter a title")
      }
    };
  }
}