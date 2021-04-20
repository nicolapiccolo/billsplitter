package it.unito.billsplitter.model

import android.content.Intent
import android.widget.Toast
import bolts.Task
import com.parse.ParseException
import com.parse.ParseUser
import it.unito.billsplitter.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class User {
    companion object{

        var username :String = ""
        var email :String = ""


        fun loginUser(email: String, password: String): ParseUser?  {
            TODO("Implementare login")
            /*ParseUser.logInInBackground(email, password)  { user: ParseUser?, e: ParseException ->
                if (user != null) {
                    // Hooray! The user is logged in.

                } else {
                    // Login failed. Look at the ParseException to see what happened.

                }
            }*/
        }

        fun createUser(username: String,password: String,email: String): ParseUser {
            val user = ParseUser()
            user.username = username
            user.setPassword(password)
            user.email = email

            // Other fields can be set just like any other ParseObject,
            // using the "put" method, like this: user.put("attribute", "its value");
            // If this field does not exists, it will be automatically created
            user.signUpInBackground()
            return user
        }

        fun getCurrentUser(): ParseUser? {
            // After login, Parse will cache it on disk, so
            // we don't need to login every time we open this
            // application
            val user = ParseUser.getCurrentUser()
            username = user?.username.toString()
            email = user?.email.toString()


            return user
        }


    }
}