package it.unito.billsplitter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import it.unito.billsplitter.R

class CreateSplitActivity : AppCompatActivity() {

    private lateinit var pages: IntArray
    private var index:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pages = intArrayOf(R.layout.activity_title_split,R.layout.activity_total_split,R.layout.activity_contacts_split)
        setContentView(pages[index])
    }

    fun Next(view: View){
        if(index+1 < pages.size){
            index+=1
            setContentView(pages[index])
        }
        else{
            //TODO
        }
    }

}