package it.unito.billsplitter.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.parse.ParseObject
import it.unito.billsplitter.*
import it.unito.billsplitter.controller.task.DeleteDataAsyncTask
import it.unito.billsplitter.controller.task.LoadFragmentAsyncTask
import it.unito.billsplitter.controller.task.UpdateDataAsyncTask
import it.unito.billsplitter.controller.task_inteface.LoadFragmentListener
import it.unito.billsplitter.controller.task_inteface.UpdatePayListener
import it.unito.billsplitter.controller.task_inteface.UpdateDataListener
import it.unito.billsplitter.view.fragment.DetailMySplitFragment
import it.unito.billsplitter.view.fragment.DetailOtherSplitFragment
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.confirm_action_dialog.*
import kotlinx.android.synthetic.main.confirm_action_dialog.view.*


class DetailActivity : AppCompatActivity(),LoadFragmentListener, UpdateDataListener, UpdatePayListener {

    companion object{
        const val ID = 1
        var modified: Boolean = false
    }

    //private var modified: Boolean = false
    private lateinit var menu : Menu
    private var isMySplit: Boolean = false
    private var menuFragment: MenuClick? = null
    private var split: ParseObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //per capire quando l'utente ha modificato lo split

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
            android.R.id.home -> {
                println("HOME: " + modified)
                val intent = Intent()
                if(modified) {
                    setResult(RESULT_OK, intent)
                }
                else {
                    setResult(RESULT_CANCELED, intent)
                }
                finish()
                true
            }

            R.id.action_modify -> {
                //addSomething()
                showProgressBar(true)
                menuFragment?.modifySplit(split)
                val members = Model.instance.getListMember(split!!)
                intent = Intent(this, SplittingActivity::class.java)
                intent.putExtra("split",split)
                intent.putParcelableArrayListExtra("members", members)
                intent.putExtra("modify",true)
                startActivity(intent)
                true
            }
            R.id.action_close -> {
                //startSettings()
                confirmDialogClose()
                true
            }
            R.id.action_delete -> {
                confirmDialogDelete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmDialogClose(){
        val mDialogView = LayoutInflater.from(this!!).inflate(R.layout.confirm_action_dialog, null)
        //AlertDialogBuilder
        val mBuilder = android.app.AlertDialog.Builder(this!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mAlertDialog.txtConfirmTitle.setText(R.string.closeTitle)
        mAlertDialog.txtDialogMessage.setText(R.string.closeMessage)
        mAlertDialog.btnSend.setOnClickListener {
            showProgressBar(true)
            UpdateDataAsyncTask(this).execute(split)
            mAlertDialog.dismiss()
        }
        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun confirmDialogDelete(){
        val mDialogView = LayoutInflater.from(this!!).inflate(R.layout.confirm_action_dialog, null)
        //AlertDialogBuilder
        val mBuilder = android.app.AlertDialog.Builder(this!!)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        mAlertDialog.txtConfirmTitle.setText(R.string.deleteTitle)
        mAlertDialog.txtDialogMessage.setText(R.string.deleteMessage)
        mAlertDialog.btnSend.setOnClickListener {
            showProgressBar(true)
            DeleteDataAsyncTask(this).execute(split)
            mAlertDialog.dismiss()
        }
        mDialogView.btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

    private fun showMenu(show: Boolean){
        if(menu!=null){
            menu.setGroupVisible(R.id.detail_menu_group,show)
        }
    }

    private fun setFragment(mySplit: MySplit){
        val bundle = Bundle()
        bundle.putParcelable("split", mySplit)
        bundle.putString("id_split",split?.objectId)

        showMenu(isMySplit)

        if(isMySplit){
            menuFragment = DetailMySplitFragment()
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

    override fun sendResult(result: Boolean) {
        println("RELOAD")
        recreate()
        DetailActivity.modified = true
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

interface CellClickListenerDetail {
    fun onCellClickListener(data: SplitMember?)
}
