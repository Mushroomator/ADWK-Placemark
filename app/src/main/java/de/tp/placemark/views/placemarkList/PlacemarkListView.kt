package de.tp.placemark.views.placemarkList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.placemark.R
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark_list.*

class PlacemarkListView: AppCompatActivity(), PlacemarkListener {

  lateinit var presenter: PlacemarkListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_list)

    // create presenter
    presenter = PlacemarkListPresenter(this)

    // set and enable toolbar
    toolbar.title = title;  // call getTitle() method of Activity (superclass of AppCompatActivity)
    setSupportActionBar(toolbar)

    // create layout manager and configure recycler view
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager  // recyclerView is from the layout
    loadPlacemarks()
  }

  private fun loadPlacemarks() {
    showPlacemarks(presenter.getPlacemarks())
  }

  fun showPlacemarks (placemarks: List<PlacemarkModel>) {
    recyclerView.adapter = PlacemarkAdapter(placemarks, this)
    recyclerView.adapter?.notifyDataSetChanged()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_add -> presenter.doAddPlacemark()
      R.id.item_map -> presenter.doShowPlacemarksMap()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onPlacemarkClick(placemark: PlacemarkModel) {
    // get rid of compiler warning that there might be a null value
    presenter.doEditPlacemark(placemark)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    recyclerView.adapter?.notifyDataSetChanged() // notify recyclerView that data has been changed
    super.onActivityResult(requestCode, resultCode, data)
  }
}