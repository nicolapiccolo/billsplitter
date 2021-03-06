package it.unito.billsplitter.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.unito.billsplitter.R

class LandingActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("LANDING ACTIVITY")

        setContentView(R.layout.activity_landing)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        println("back pressed")
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("Exit", true)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)
        finish()
    }

    fun Login(view : View){
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)

    }

    fun Register(view : View){
        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)
    }
}