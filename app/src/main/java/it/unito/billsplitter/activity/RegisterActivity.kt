
package it.unito.billsplitter.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.parse.ParseException
import com.parse.ParseInstallation
import com.parse.ParseUser
import it.unito.billsplitter.ChangePasswordAsync
import it.unito.billsplitter.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.change_password_dialog.*
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.dialog_phone.*
import kotlinx.android.synthetic.main.dialog_phone.btnSend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class RegisterActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    companion object{
        const val ID = 1
    }

    private var phone :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getPhoneNumber()
    }

    private fun getPhoneNumber(){
        var mGoogleApiClient = GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Auth.CREDENTIALS_API)
        .build()

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()


        runBlocking {
            val job = launch(Dispatchers.Default) {
                mGoogleApiClient.connect()

                val intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest)
                try {
                    startIntentSenderForResult(intent.intentSender, 1008, null, 0, 0, 0, null)
                } catch (e: SendIntentException) {
                    Log.e("", "Could not start hint picker Intent", e)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1008 -> if (resultCode == RESULT_OK) {
                val cred: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                //                    cred.getId====: ====+919*******
                if(cred != null){
                    phone = cred.id
                    Log.e("cred.getId", phone)
                }
            } else {
                Toast.makeText(this,"Sim Card Not Found", Toast.LENGTH_SHORT).show()
                Log.e("cred.getId", "1008: Card not Found")
                showDialog()
                return
            }
        }
    }


    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this!!).inflate(R.layout.dialog_phone, null)
        //AlertDialogBuilder
        val mBuilder = android.app.AlertDialog.Builder(this!!)
                .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mAlertDialog.btnSend.setOnClickListener {
            phone = mAlertDialog.editTextPhone.toString()
            mAlertDialog.dismiss()
        }
        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun Register(view : View){

        val user = ParseUser()
        user.setUsername(txtName.text.toString())
        user.setPassword(txtPassword.text.toString())
        user.setEmail(txtEmail.text.toString())
        user.put("phone",phone)

        if(phone.isEmpty()){
            Toast.makeText(this, "Per completare la registrazione devi inserire il numero di telefono", Toast.LENGTH_SHORT).show()
            showDialog()
        }


        user.signUpInBackground() { e: ParseException? ->
            if(e == null){
                val installation = ParseInstallation.getCurrentInstallation()
                installation.put("user", ParseUser.getCurrentUser())
                installation.saveInBackground()

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("RESULT",RegisterActivity.ID.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, e?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Login(view: View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun Back(view: View){
        intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}