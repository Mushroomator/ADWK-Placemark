package de.tp.placemark.views.login

import com.google.firebase.auth.FirebaseAuth
import de.tp.placemark.models.firebase.PlacemarkFireStore
import org.jetbrains.anko.toast
import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW

class LoginPresenter(view: BaseView): BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: PlacemarkFireStore? = null

    init {
        // cast to PlacemarkFireStore if Firebase is used because otherwise fetchPlacemarks() function cannot be called
        if(app.placemarks is PlacemarkFireStore){
            fireStore = app.placemarks as PlacemarkFireStore
        }
    }

    /**
     * Login the user with email and password stored in Firebase. Displays a toast when login fails.
     * @param email entered email address
     * @param password entered password
     * @author Thomas Pilz
     */
    fun doLogin(email: String, password: String){
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!){
            if(it.isSuccessful){
                // get placemark from cloud storage if cloud storage is used
                if(fireStore != null){
                    fireStore!!.fetchPlacemarks {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                }
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            }
            else{
                view?.toast("Sign In Failed: ${it.exception?.message}")
                view?.hideProgress()
            }
        }
    }

    fun doSignup(email: String, password: String){
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!){
            if(it.isSuccessful){
                // no need to get placemarks since there are none when a user signs up
                // WRONG: one needs to initialize the new user ID otherwise placemarks do not get saved to the correct user
                if(fireStore != null){
                    fireStore!!.fetchPlacemarks {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                }
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            }
            else{
                view?.toast("Sign Up Failed: ${it.exception?.message}")
            }
            view?.hideProgress()
        }
    }
}