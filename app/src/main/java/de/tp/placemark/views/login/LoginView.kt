package de.tp.placemark.views.login

import android.os.Bundle
import android.view.View
import de.tp.placemark.R
import kotlinx.android.synthetic.main.activity_login_view.*
import org.jetbrains.anko.toast
import org.wit.placemark.views.BaseView

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        init(toolbar, false)

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        hideProgress()

        signUp.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if(email.isEmpty() || email == "" || pw.isEmpty() || pw == ""){
                toast("Please provide email and password!")
            }
            else{
                presenter.doSignup(email, pw)
            }
        }

        logIn.setOnClickListener {
            val email = etEmail.text.toString()
            val pw = etPassword.text.toString()
            if(email.isEmpty() || pw.isEmpty()){
                toast("Please provide email and password!")
            }
            else{
                presenter.doLogin(email, pw)
            }
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}