package it.unito.billsplitter.view.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
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
import kotlinx.android.synthetic.main.progress_dialog.*


class DetailActivity : AppCompatActivity(),LoadFragmentListener, UpdateDataListener, UpdatePayListener {

    companion object{
        const val ID = 1
        var modified: Boolean = false
    }

    //private var modified: Boolean = false
    private lateinit var menu : Menu
    private var isMySplit: Boolean = false
    private var split: ParseObject? = null
    private lateinit var mySplit: MySplit
    private var progressDialog: AlertDialog? = null


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
                modifyPercentage()
                true
            }
            R.id.action_close -> {
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

        mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom

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

        mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom

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

    private fun modifyPercentage(){
        intent = Intent(this, SplittingActivity::class.java)
        intent.putExtra("split",split)
        intent.putParcelableArrayListExtra("members", mySplit.memberList)
        intent.putExtra("modify",true)
        startActivity(intent)
    }

    public fun showMenu(show: Boolean){
        if(menu!=null){
            menu.setGroupVisible(R.id.detail_menu_group,show)
        }
    }

    private fun setFragment(mySplit: MySplit){
        val bundle = Bundle()
        bundle.putParcelable("split", mySplit)
        bundle.putString("id_split",split?.objectId)


        if(isMySplit){
            val fragment = DetailMySplitFragment.newIstance()
            fragment.arguments = bundle
            replaceFragment(fragment)
            //showMenu(isMySplit)
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
        //recreate()
        progressDialog = showProgressDialog(this,"Confirming...")

        LoadFragmentAsyncTask(this).execute(split)
        DetailActivity.modified = true
    }



    override fun sendData(result: Boolean) {
        val intent = Intent()
        setResult(RESULT_OK, intent);
        finish()
    }

    override fun sendData(mySplit: MySplit) {

        progressDialog?.dismiss()

        this.mySplit = mySplit
        setFragment(mySplit)
    }

    private fun getAlertDialog(context: Context, layout: Int, setCancellationOnTouchOutside: Boolean): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View =
            layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }

    private fun showProgressDialog(context: Context, message: String): AlertDialog {
        val dialog = getAlertDialog(context, R.layout.progress_dialog, setCancellationOnTouchOutside = false)
        dialog.show()
        dialog.text_progress_bar.text = message
        return dialog
    }
}

interface CellClickListenerDetail {
    fun onCellClickListener(data: SplitMember?)
}
