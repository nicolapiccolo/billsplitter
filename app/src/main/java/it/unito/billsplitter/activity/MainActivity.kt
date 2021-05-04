package it.unito.billsplitter.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import it.unito.billsplitter.AsyncTaskListener
import it.unito.billsplitter.LoadDataAsyncTask
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapter
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),CellClickListener,AsyncTaskListener {

    private lateinit var adapter: RvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User.getCurrentUser()
        if (user==null) {
            intent = Intent(this, SlidingActivity::class.java)
            startActivity(intent)
        }
        else{
            setContentView(R.layout.activity_main)

            //Toast.makeText(baseContext, "Welcome back ${User.username}", Toast.LENGTH_LONG).show()
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            hideView()
            LoadDataAsyncTask(this).execute(false) //thread caricamento dati

            btnCreate.setOnClickListener {
                intent = Intent(this, CreateSplitActivity::class.java)
                startActivity(intent)
            }

            swiperefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue))
            swiperefresh.setColorSchemeColors(Color.WHITE)

            swiperefresh.setOnRefreshListener {
                hideView()
                LoadDataAsyncTask(this).execute(true)
                swiperefresh.isRefreshing = false
            }

        }

    }

    override fun onCellClickListener(data: ParseObject?) {
        //Model.instance.getTotalofSplit("")
        //Toast.makeText(baseContext, "Card Click", Toast.LENGTH_LONG).show()
        intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("split", data)
        startActivityForResult(intent,DetailActivity.ID)
    }


    private fun populateList(list: ArrayList<Split>){
        adapter = RvAdapter(this, list)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        progressBar.setVisibility(View.GONE)
        recyclerView.setVisibility(View.VISIBLE);
    }

    private fun setTotal(give: String, have: String){
        txtCredit.text = have
        txtDebt.text = give

        txtCredit.setVisibility(View.VISIBLE)
        txtDebt.setVisibility(View.VISIBLE)
        txtSlash.setVisibility(View.VISIBLE)
    }

    private fun hideView(){
        txtCredit.setVisibility(View.GONE)
        txtDebt.setVisibility(View.GONE)
        txtSlash.setVisibility(View.GONE)


        recyclerView.setVisibility(View.GONE)
        progressBar.setVisibility(View.VISIBLE)
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendData(list: ArrayList<Split>, give: String, have: String) {
        populateList(list)
        setTotal(give,have)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that it is the SecondActivity with an OK result
        if (requestCode == DetailActivity.ID) {
            if (resultCode == Activity.RESULT_OK) {
                println("AGGIORNA DATI")
                hideView()
                LoadDataAsyncTask(this).execute(true) //thread caricamento dati
            }
        }
    }
}



interface CellClickListener {
    fun onCellClickListener(data: ParseObject?)
}