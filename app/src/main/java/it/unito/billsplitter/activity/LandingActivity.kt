package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.unito.billsplitter.R

class LandingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
    }

    fun Login(view : View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun Register(view : View){
        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}