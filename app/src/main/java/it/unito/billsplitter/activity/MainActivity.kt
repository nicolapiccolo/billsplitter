package it.unito.billsplitter.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.billsplitter.AsyncTaskListener
import it.unito.billsplitter.LoadDataAsyncTask
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapter
import it.unito.billsplitter.model.Model
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

            Toast.makeText(baseContext, "Welcome back ${User.username}", Toast.LENGTH_LONG).show()
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE)

            LoadDataAsyncTask(this).execute() //thread caricamento dati

        }
    }

    override fun onCellClickListener(data: Split) {
        //Model.instance.getTotalofSplit("")
        Toast.makeText(baseContext, "Card Click", Toast.LENGTH_LONG).show()
    }


    private fun populateList(list: ArrayList<Split>){
        adapter = RvAdapter(this, list)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendData(list: ArrayList<Split>) {
        populateList(list)
        recyclerView.setVisibility(View.VISIBLE);
    }
}



interface CellClickListener {
    fun onCellClickListener(data: Split)
}