package de.tp.placemark.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import de.tp.placemark.R
import de.tp.placemark.helpers.readImageFromPath
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark_list.toolbar
import kotlinx.android.synthetic.main.activity_placemark_map.*
import org.jetbrains.anko.AnkoLogger

class PlacemarkMapView : AppCompatActivity(), AnkoLogger, GoogleMap.OnMarkerClickListener {

    lateinit var presenter: PlacemarkMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_map)

        // obtain ref to presenter
        presenter = PlacemarkMapPresenter(this)

        // configure toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title

        // pass on lifecycle event onCreate
        mapView.onCreate(savedInstanceState);
        // get actual map asynchronously and assign attribute accordingly to then configure the map
        mapView.getMapAsync { presenter.doPopulateMap(it) }
    }

    fun showPlacemark(placemark: PlacemarkModel){
        currentTitle.text = placemark.title
        currentDescription.text = placemark.description
        currentImage.setImageBitmap(readImageFromPath(this, placemark.image))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
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