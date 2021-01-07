package de.tp.placemark.views.login

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.placemark.views.BasePresenter
import org.wit.placemark.views.BaseView
import org.wit.placemark.views.VIEW

class LoginPresenter(view: BaseView): BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

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
                view?.navigateTo(VIEW.LIST)
            }
            else{
                view?.toast("Sign In Failed: ${it.exception?.message}")
            }
            view?.hideProgress()
        }
    }

    fun doSignup(email: String, password: String){
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!){
            if(it.isSuccessful){
                view?.navigateTo(VIEW.LIST)
            }
            else{
                view?.toast("Sign Up Failed: ${it.exception?.message}")
            }
            view?.hideProgress()
        }
    }
}