package it.unito.billsplitter.view.fragment


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.parse.ParseException
import com.parse.ParseInstallation
import com.parse.ParseUser
import it.unito.billsplitter.R
import it.unito.billsplitter.view.activity.*
import kotlinx.android.synthetic.main.activity_forgot_password.view.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {

                ParseUser.logInInBackground(txtEmail.text.toString(), txtPassword.text.toString()) { user: ParseUser?, e: ParseException? ->
                    if (user != null) {
                        // Hooray! The user is logged in.
                        val installation = ParseInstallation.getCurrentInstallation()
                        installation.put("user", ParseUser.getCurrentUser())
                        installation.saveInBackground()

                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("RESULT", LoginActivity.ID.toString())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        // Login failed. Look at the ParseException to see what happened.
                        Toast.makeText(context, e?.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }

        btnRegister.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener{
            val intent = Intent(context, LandingActivity::class.java)
            startActivity(intent)
        }

        btnForgotPassword.setOnClickListener{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.activity_forgot_password, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context)
                    .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            mDialogView.d_btnSend.setOnClickListener {
                mAlertDialog.dismiss()
                ParseUser.requestPasswordResetInBackground(mDialogView.txtRestoreEmail.toString()) {e: ParseException? ->
                    if(e == null){
                        Toast.makeText(context, "New Password sent correctly. Check your Email!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, LandingActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, e?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mDialogView.d_btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

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