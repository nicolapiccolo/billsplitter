package it.unito.billsplitter.model

import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseUser

class User {
    companion object{
        fun loginUser(email: String, password: String): ParseUser {
            ParseUser.logInInBackground(email, password)
            return ParseUser()
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
            return ParseUser.getCurrentUser()
        }
    }
}