package de.tp.placemark.activities

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import de.tp.placemark.R
import de.tp.placemark.main.MainApp
import kotlinx.android.synthetic.main.activity_placemark_list.*
import org.jetbrains.anko.startActivityForResult

class PlacemarkListActivity: AppCompatActivity() {
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_placemark_list)
    app = application as MainApp

    // set and enable toolbar
    toolbar.title = title;  // call getTitle() method of Activity (superclass of AppCompatActivity)
    setSupportActionBar(toolbar)

    // create layout manager and configure recycler view
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager  // recyclerView is from the layout
    recyclerView.adapter = PlacemarkAdapter(app.placemarks)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
      R.id.item_add -> startActivityForResult<PlacemarkActivity>(0)
    }
    return super.onOptionsItemSelected(item)
  }
}