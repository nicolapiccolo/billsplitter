package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import it.unito.billsplitter.R
import it.unito.billsplitter.model.User


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User.getCurrentUser()
        if (user==null) {
            intent = Intent(this, SlidingActivity::class.java)
            startActivity(intent)
        }
        else{
            println(User.username)
            Toast.makeText(baseContext,"Welcome back ${User.username}", Toast.LENGTH_LONG).show()
            setContentView(R.layout.activity_main)
        }


    }
}