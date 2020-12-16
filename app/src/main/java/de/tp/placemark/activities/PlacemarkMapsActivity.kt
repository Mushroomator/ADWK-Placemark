package de.tp.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import de.tp.placemark.R
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.main.MainApp
import kotlinx.android.synthetic.main.activity_placemark_list.*
import kotlinx.android.synthetic.main.activity_placemark_list.toolbar
import kotlinx.android.synthetic.main.activity_placemark_maps.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class PlacemarkMapsActivity : AppCompatActivity(), AnkoLogger, GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_maps)

        // obtain ref to MainApp (central application object)
        app = application as MainApp

        // configure toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title

        // pass on lifecycle event onCreate
        mapView.onCreate(savedInstanceState);
        // get actual map asynchronously and assign attribute accordingly to then configure the map
        mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        app.placemarks.findAll().forEach{
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id  // add the marker on the map and a tag to the marker to be able to identify which marker/ Placemark was clicked on later on
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom)) // zoom in to the placemark (should actually be refatored to be out of iteration as it only needs to be done once as only one placemark can be in focus)
        }
        map.setOnMarkerClickListener(this)
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        var currentPlacemark = app.placemarks.findById(tag)
        if(currentPlacemark != null){
            currentTitle.text = currentPlacemark.title
            currentDescription.text = currentPlacemark.description
            currentImage.setImageBitmap(readImageFromPath(this, currentPlacemark.image))
        }
        return true; // false means default behavior = camera zooms on marker on popup will be displayed; true: custom event
    }

    // Lifecycle events
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}