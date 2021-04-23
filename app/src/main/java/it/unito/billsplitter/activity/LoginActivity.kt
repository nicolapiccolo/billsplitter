package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser
import it.unito.billsplitter.R
import it.unito.billsplitter.fragment.LoginFragment
import it.unito.billsplitter.model.User


class LoginActivity : AppCompatActivity(),LoginFragment.OnFirstPageFragmentInteractionListener {
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