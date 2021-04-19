package it.unito.billsplitter.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.parse.ParseException
import com.parse.ParseUser
import it.unito.billsplitter.R
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            User.loginUser(txtEmail.text.toString(), txtPassword.text.toString())
        }

        btnRegister.setOnClickListener {
            User.createUser("nicola", "nicola", "nikola9piccolo9@gmail.com")
        }
    }


    /*
    fun loginUser(email: String, password: String) {
        ParseUser.logInInBackground(email, password) { user: ParseUser?, e: ParseException ->
            if (user != null) {
                // Hooray! The user is logged in.
                //Toast.makeText(context,"Login successful",Toast.LENGTH_SHORT).show()

            } else {
                // Login failed. Look at the ParseException to see what happened.
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun createUser(username: String,password: String,email: String) {
        val user = ParseUser()
        user.username = username
        user.setPassword(password)
        user.email = email

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created
        user.signUpInBackground { e: ParseException? ->
            if (e == null) {
                // Hooray! Let them use the app now.
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                    println(e.message)
                println(e.code)
                println(e.stackTrace)
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
*/




    interface OnFirstPageFragmentInteractionListener {
        fun onLoginButtonPressed(username: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}