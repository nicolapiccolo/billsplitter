package it.unito.billsplitter.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import it.unito.billsplitter.R
import kotlinx.android.synthetic.main.activity_title_split.*

class CreateSplitActivity : AppCompatActivity() {

    private var title: String=""
    private var total: String=""


    private var isTitle = true
    private var toolbar: Toolbar? = null

    companion object{
        const val ID = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_split)
        setTitle()
    }

    private fun setTitle(){
        txt.setText("Enter the Bill Title")
        edtInsertTitle.visibility=View.VISIBLE
        edtInsertTotal.visibility = View.GONE
        isTitle = true
        toolbar = findViewById<Toolbar>(R.id.title_toolbar) as Toolbar
        setActionBar(toolbar!!)

    }
    private fun setTotal(){
        txt.setText("Enter the Bill Total")
        edtInsertTitle.visibility=View.GONE
        edtInsertTotal.visibility = View.VISIBLE
        isTitle = false
        toolbar = findViewById<Toolbar>(R.id.title_toolbar) as Toolbar
        setActionBar(toolbar!!)
    }

    private fun setActionBar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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



    fun nextBtn(view: View){
        if (isTitle){
            if(edtInsertTitle.text.toString().equals(""))
                Toast.makeText(this, "Title can't be empty!", Toast.LENGTH_SHORT).show()
            else{
                title=edtInsertTitle.text.toString()
                setTotal()
            }
        }
        else{
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

}