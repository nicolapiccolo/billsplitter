package it.unito.billsplitter.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.parse.ParseObject
import it.unito.billsplitter.LoadFragmentAsyncTask
import it.unito.billsplitter.fragment.AsyncTaskFragmentListener
import it.unito.billsplitter.fragment.DetailMySplitFragment
import it.unito.billsplitter.fragment.DetailOtherSplitFragment
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import kotlinx.android.synthetic.main.activity_detail.*
import it.unito.billsplitter.R
import it.unito.billsplitter.UpdateDataAsyncTask
import it.unito.billsplitter.UpdateTaskListener


class DetailActivity : AppCompatActivity(), AsyncTaskFragmentListener, UpdateTaskListener {

    companion object{
        const val ID = 1
    }
    private lateinit var menu : Menu
    private var isMySplit: Boolean = false
    private var menuFragment: MenuClick? = null
    private var split: ParseObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.detail_toolbar) as Toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false);

        split = (intent.getParcelableExtra("split") as? ParseObject)!!

        if(split != null){

            isMySplit = Model.instance.isMySplit(split!!)

            LoadFragmentAsyncTask(this).execute(split)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (menu != null) {
            this.menu = menu
        }
        menuInflater.inflate(R.menu.detail_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.action_modify -> {
                //addSomething()
                showProgressBar(true)
                menuFragment?.modifySplit(split)
                true
            }
            R.id.action_close -> {
                //startSettings()
                confirmDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Closing Split")
        builder.setMessage("Are you sure to close this split? ")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            showProgressBar(true)
            UpdateDataAsyncTask(this).execute(split)
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }

        builder.show()
    }

    private fun showMenu(show: Boolean){
        if(menu!=null){
            menu.setGroupVisible(R.id.detail_menu_group,show)
        }
    }

    private fun setFragment(mySplit: MySplit){
        val bundle = Bundle()
        bundle.putSerializable("split", mySplit)

        showMenu(isMySplit)

        if(isMySplit){
            menuFragment = DetailMySplitFragment() as MenuClick
            val fragment = DetailMySplitFragment.newIstance()
            fragment.arguments = bundle
            replaceFragment(fragment)
        }
        else{
            val fragment = DetailOtherSplitFragment.newIstance()
            fragment.arguments = bundle
            replaceFragment(fragment)
        }

        showProgressBar(false)

    }

    private fun showProgressBar(b: Boolean){
        if(b){
            layout_container.visibility = View.GONE
            detail_progressBar.visibility = View.VISIBLE
        }
        else{
            layout_container.visibility = View.VISIBLE
            detail_progressBar.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_container, fragment)
        fragmentTransaction.commit()
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            detail_progressBar.setProgress(progress)
        }
    }

    override fun sendData(result: Boolean) {
        val intent = Intent()
        setResult(RESULT_OK, intent);
        finish()
    }

    override fun sendData(mySplit: MySplit) {
        setFragment(mySplit)
    }
}

interface MenuClick{
    fun closeSplit(s: ParseObject?)
    fun modifySplit(s: ParseObject?)
}

