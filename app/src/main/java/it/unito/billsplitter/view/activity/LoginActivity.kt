package it.unito.billsplitter.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.unito.billsplitter.R
import it.unito.billsplitter.view.fragment.LoginFragment


class LoginActivity : AppCompatActivity(),LoginFragment.OnFirstPageFragmentInteractionListener {

    companion object{
        const val ID = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    /*
    fun getCurrentUser() {
        // After login, Parse will cache it on disk, so
        // we don't need to login every time we open this
        // application
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            Toast.makeText(this,"Benvenuto ${currentUser.username}",Toast.LENGTH_LONG)
        } else {
            // show the signup or login screen
        }
    */
    override fun onLoginButtonPressed(username: String) {
        TODO("Login")
    }
}