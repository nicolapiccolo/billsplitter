package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import it.unito.billsplitter.R
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = User.getCurrentUser()

        if (user==null) {
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            println(User.username)
            Toast.makeText(baseContext,"Welcome back ${User.username}", Toast.LENGTH_LONG).show()

            val s : Split = Model.instance.getSplitElement(0)
            println(s)

            //txtSplit.text = s.name
            setContentView(R.layout.activity_main)
        }
    }


}