package it.unito.billsplitter.activity

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.parse.ParseObject
import it.unito.billsplitter.LoadFragmentAsyncTask
import it.unito.billsplitter.R
import it.unito.billsplitter.fragment.AsyncTaskFragmentListener
import it.unito.billsplitter.fragment.DetailMySplitFragment
import it.unito.billsplitter.fragment.DetailOtherSplitFragment
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.Split
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*


class DetailActivity : AppCompatActivity(), AsyncTaskFragmentListener {

    private var isMySplit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.detail_toolbar) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        val split : ParseObject? = intent.getParcelableExtra("split") as? ParseObject

        if(split != null){

            isMySplit = Model.instance.isMySplit(split)

            LoadFragmentAsyncTask(this).execute(split)
        }
    }

    private fun setFragment(mySplit: MySplit){
        val bundle = Bundle()
        bundle.putSerializable("split",mySplit)

        if(isMySplit){
            val fragment = DetailMySplitFragment.newIstance()
            fragment.arguments = bundle
            replaceFragment(fragment)
        }
        else{
            val fragment = DetailOtherSplitFragment.newIstance()
            fragment.arguments = bundle
            replaceFragment(fragment)
        }

        layout_container.visibility = View.VISIBLE
        detail_progressBar.visibility = View.GONE

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_container, fragment)
        fragmentTransaction.commit()
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendData(mySplit: MySplit) {
        setFragment(mySplit)
    }
}

