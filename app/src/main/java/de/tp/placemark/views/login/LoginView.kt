package de.tp.placemark.views.login

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class loginView : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        // Enables Always-on
        setAmbientEnabled()
    }
}