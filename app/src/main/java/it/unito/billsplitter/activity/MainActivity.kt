package it.unito.billsplitter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.parse.Parse
import it.unito.billsplitter.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}