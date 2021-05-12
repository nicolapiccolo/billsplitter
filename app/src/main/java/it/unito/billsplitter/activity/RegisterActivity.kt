package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseException
import com.parse.ParseInstallation
import com.parse.ParseUser
import it.unito.billsplitter.R
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object{
        const val ID = 1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun Register(view : View){

        val user = ParseUser()
        user.setUsername(txtName.text.toString())
        user.setPassword(txtPassword.text.toString())
        user.setEmail(txtEmail.text.toString())
        user.signUpInBackground() { e: ParseException? ->
            if(e == null){
                val installation = ParseInstallation.getCurrentInstallation()
                installation.put("user", ParseUser.getCurrentUser())
                installation.saveInBackground()

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("RESULT",RegisterActivity.ID.toString())
                startActivity(intent)
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
}