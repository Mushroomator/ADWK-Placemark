package de.tp.placemark.views.placemarkList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.placemark.R
import de.tp.placemark.models.PlacemarkModel
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.info
import org.wit.placemark.views.BaseView

class PlacemarkListView: BaseView(), PlacemarkListener {

  lateinit var presenter: PlacemarkListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_list)

    // create presenter
    presenter = initPresenter(PlacemarkListPresenter(this)) as PlacemarkListPresenter

    // set and enable toolbar
    init(toolbar, false)

    // create layout manager and configure recycler view
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager  // recyclerView is from the layout
    presenter.loadPlacemarks()
  }

  override fun showPlacemarks (placemarks: List<PlacemarkModel>) {
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
    super.onActivityResult(requestCode, resultCode, data)
    presenter.loadPlacemarks() // notify recyclerView that data has been changed
  }
}