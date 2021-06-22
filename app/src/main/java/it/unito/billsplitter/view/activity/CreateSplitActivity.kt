package it.unito.billsplitter.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import it.unito.billsplitter.R
import kotlinx.android.synthetic.main.activity_title_split.*
import kotlinx.android.synthetic.main.activity_total_split.*

class CreateSplitActivity : AppCompatActivity() {

    var title: String=""
    var total: String=""


    private var isTitle = true
    private var toolbar: Toolbar? = null

    companion object{
        const val ID = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle()
    }

    private fun setTitle(){
        setContentView(R.layout.activity_title_split)

        isTitle = true
        toolbar = findViewById<Toolbar>(R.id.title_toolbar) as Toolbar
        setActionBar(toolbar!!)
    }
    private fun setTotal(){
        setContentView(R.layout.activity_total_split)
        isTitle = false
        toolbar = findViewById<Toolbar>(R.id.total_toolbar) as Toolbar
        setActionBar(toolbar!!)
    }

    private fun setActionBar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("ITEM")
        when (item.itemId) {
            android.R.id.home -> {
                //val resId:Int = resources.getIdentifier("action_bar_container", "id", "android")
                if(isTitle){
                    Toast.makeText(this,"BACK TO MAIN", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    setTitle()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun nextTitle(view: View){
        if(edtInsertTitle.text.toString().equals(""))
            Toast.makeText(this, "Title can't be empty!", Toast.LENGTH_SHORT).show()
        else{
            title=edtInsertTitle.text.toString()
            setTotal()
        }

    }

    fun nextTotal(view: View){
        if (edtInsertTotal.text.toString().equals(""))
            Toast.makeText(this, "Total can't be empty!", Toast.LENGTH_SHORT).show()
        else{
            intent = Intent(this, ContactActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("total", edtInsertTotal.text.toString())
            startActivity(intent)
        }
    }
}