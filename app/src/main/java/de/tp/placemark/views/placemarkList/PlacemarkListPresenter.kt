package de.tp.placemark.views.placemarkList

import com.google.firebase.auth.FirebaseAuth
import de.tp.placemark.main.MainApp
import de.tp.placemark.models.PlacemarkModel
import de.tp.placemark.views.map.PlacemarkMapView
import de.tp.placemark.views.placemark.PlacemarkView
import org.jetbrains.anko.*
import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW

class PlacemarkListPresenter(view: BaseView) : BasePresenter(view) {

    fun loadPlacemarks() {
        doAsync {
            val placemarks = app.placemarks.findAll()
            uiThread {
                view?.showPlacemarks(placemarks)
            }
        }
    }

    fun doAddPlacemark() {
        view?.navigateTo(VIEW.PLACEMARK)
    }

    fun doEditPlacemark(placemark: PlacemarkModel) {
        view?.navigateTo(VIEW.PLACEMARK, 0, "placemark_edit", placemark)
    }

    fun doShowPlacemarksMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun doLogout() {
        val auth = FirebaseAuth.getInstance()
        // user should be logged in otherwise not possible to be here but better check if user is actually signed in
        if (auth.currentUser != null) {
            auth.signOut()
            // clear cached placemarks
            app.placemarks.clear()
            view?.navigateTo(VIEW.LOGIN)
        }
    }
}