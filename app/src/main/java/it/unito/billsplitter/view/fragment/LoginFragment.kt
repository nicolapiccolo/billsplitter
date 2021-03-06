package it.unito.billsplitter.view.fragment


import android.app.AlertDialog
import android.content.Context
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
import kotlinx.android.synthetic.main.progress_dialog.*


class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnLogin.setOnClickListener {

            val progressDialog = showProgressDialog(requireContext(),"Login...")
            ParseUser.logInInBackground(
                txtEmail.text.toString(),
                txtPassword.text.toString()
            ) { user: ParseUser?, e: ParseException? ->
                progressDialog.dismiss()

                if (user != null) {
                    // Hooray! The user is logged in.

                    val installation = ParseInstallation.getCurrentInstallation()
                    installation.put("user", ParseUser.getCurrentUser())
                    installation.saveInBackground()

                    Toast.makeText(context, R.string.loginSuccess, Toast.LENGTH_SHORT).show()
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

        btnBack.setOnClickListener {
            val intent = Intent(context, LandingActivity::class.java)
            startActivity(intent)
        }

        btnForgotPassword.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.activity_forgot_password, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            mDialogView.d_btnSend.setOnClickListener {
                mAlertDialog.dismiss()
                ParseUser.requestPasswordResetInBackground(mDialogView.txtRestoreEmail.toString()) { e: ParseException? ->
                    if (e == null) {
                        Toast.makeText(
                            context,
                            "New Password sent correctly. Check your Email!",
                            Toast.LENGTH_SHORT
                        ).show()
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

    private fun getAlertDialog( context: Context, layout: Int, setCancellationOnTouchOutside: Boolean): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View =
            layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }

    private fun showProgressDialog(context: Context, message: String): AlertDialog {
        val dialog = getAlertDialog(context, R.layout.progress_dialog, setCancellationOnTouchOutside = false)
        dialog.show()
        dialog.text_progress_bar.text = message
        return dialog
    }

    interface OnFirstPageFragmentInteractionListener {
        fun onLoginButtonPressed(username: String)


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
}