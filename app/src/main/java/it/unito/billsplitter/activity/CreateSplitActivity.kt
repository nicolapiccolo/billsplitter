package it.unito.billsplitter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.unito.billsplitter.R
import kotlinx.android.synthetic.main.activity_title_split.*
import kotlinx.android.synthetic.main.activity_total_split.*

class CreateSplitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_split)
    }

    fun nextTitle(view: View){
        if(edtInsertTitle.text.toString().equals(""))
            Toast.makeText(this, "Title can't be empty!", Toast.LENGTH_SHORT).show()
        else
            setContentView(R.layout.activity_total_split)
    }

    fun nextTotal(view: View){
        if (edtInsertTotal.text.toString().equals(""))
            Toast.makeText(this, "Total can't be empty!", Toast.LENGTH_SHORT).show()
        else{
            intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }
    }
}