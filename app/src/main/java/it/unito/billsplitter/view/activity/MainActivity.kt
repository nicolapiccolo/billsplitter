package it.unito.billsplitter.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import it.unito.billsplitter.*
import it.unito.billsplitter.controller.adapter.RvAdapterMain
import it.unito.billsplitter.controller.adapter.RvAdapterHistory
import it.unito.billsplitter.controller.task.LoadDataAsyncTask
import it.unito.billsplitter.controller.task.LoadHistAsyncTask
import it.unito.billsplitter.controller.task.LoadPayPalAsyncTask
import it.unito.billsplitter.controller.task_inteface.LoadHistListener
import it.unito.billsplitter.controller.task_inteface.LoadDataListener
import it.unito.billsplitter.controller.task_inteface.LoadPayPalListener
import it.unito.billsplitter.controller.task_inteface.UpdateDataListener
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.PayPalAccount
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.change_password_dialog.view.d_btnCancel
import kotlinx.android.synthetic.main.login_paypal_dialog.*
import kotlinx.android.synthetic.main.login_paypal_dialog.view.*
import kotlinx.android.synthetic.main.pay_error_dialog.*
import kotlinx.android.synthetic.main.progress_dialog.*


class MainActivity : AppCompatActivity(),CellClickListener, LoadDataListener, UpdateDataListener,
    LoadHistListener, LoadPayPalListener {

    private lateinit var adapter: RvAdapterMain
    private lateinit var bottomSheet: ProfileBottomSheetActivity
    private var progressDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(getIntent().getBooleanExtra("Exit", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        println("MAIN ACTIVITY")
        val user = User.getCurrentUser()

        if (user==null) {
            val intent = Intent(this, SlidingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)
        }
        else{
            setContentView(R.layout.activity_main)

            //Toast.makeText(baseContext, "Welcome back ${User.username}", Toast.LENGTH_LONG).show()
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerHistoryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            icon_text.text = user.username.capitalize()[0].toString()

            val intentExtra = intent.getStringExtra("RESULT")
            println("Extra : " +intentExtra)

            hideView()

            if(intentExtra != null) {
                println("RICARICO I DATI")
                LoadDataAsyncTask(this).execute(true)
                LoadHistAsyncTask(this).execute(true)
            }
            else {
                println("NON RICARICO I DATI")
                LoadDataAsyncTask(this).execute(false) //non ricarico i dati
                LoadHistAsyncTask(this).execute(true)

            }

            btnCreate.setOnClickListener {
                println(User.email)
                println(User.username)
                //check se ha paypal collegato
                if (User.isLinked()){
                    intent = Intent(this, CreateSplitActivity::class.java)
                    startActivityForResult(intent,CreateSplitActivity.ID)
                    overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)
                }
                else{
                    payPalDialog(this)
                }
            }

            swiperefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue))
            swiperefresh.setColorSchemeColors(Color.WHITE)

            swiperefresh.setOnRefreshListener {
                hideView()
                LoadDataAsyncTask(this).execute(true)
                LoadHistAsyncTask(this).execute(true)
                swiperefresh.isRefreshing = false
            }



            icon.setOnClickListener{

                Split.getFormatFloat("10,00")

                val progressDialog = showProgressDialog(this,"Loading...")
                bottomSheet = ProfileBottomSheetActivity(progressDialog)

                if(!bottomSheet.isAdded()){
                    bottomSheet.show(supportFragmentManager, "BottomSheetDialog")
                }

            }

        }

    }

    override fun onCellClickListener(data: ParseObject?) {
        //Model.instance.getTotalofSplit("")
        //Toast.makeText(baseContext, "Card Click", Toast.LENGTH_LONG).show()
        intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("split", data)
        startActivityForResult(intent,DetailActivity.ID)
        overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_id_bottom)
        DetailActivity.modified = false
    }


    private fun populateList(list: ArrayList<Split>){
        adapter = RvAdapterMain(this, list)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        progressBar.setVisibility(View.GONE)
        recyclerView.setVisibility(View.VISIBLE)

    }

    private fun populateHist(list: ArrayList<String>){
        val ad = RvAdapterHistory(list)
        recyclerHistoryView.adapter = ad
        txtViewHystory.setVisibility(View.VISIBLE)
        recyclerHistoryView.setVisibility(View.VISIBLE)
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
        txtNoSplit.setVisibility(View.GONE)
        txtViewHystory.setVisibility(View.GONE)
        txtNoHistory.visibility = View.GONE
        recyclerHistoryView.setVisibility(View.GONE)
        recyclerView.setVisibility(View.GONE)
        progressBar.setVisibility(View.VISIBLE)
    }

    private fun payPalError(ctx: Context, message: String){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.pay_error_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mAlertDialog.txtMessage.text = message
        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_btnClose.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun payPalDialog(ctx: Context){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.login_paypal_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationSlide

        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_btnLogin.setOnClickListener {
            if(!mDialogView.d_txtEmail.text.equals("") && !mDialogView.d_txtPassword.text.equals("")){

                    val email = mDialogView.d_txtEmail.text.toString()
                    val password = mDialogView.d_txtPassword.text.toString()

                    val account = PayPalAccount(email,password)
                    //showProgressBar(true)

                    progressDialog = showProgressDialog(this,"Login...")

                    var controller = LoadPayPalAsyncTask()
                    controller.setAsyncListener(this)
                    controller.execute(account)

                    mAlertDialog.dismiss()
            }
            else
                Toast.makeText(ctx, "Fields can't be empty!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun getAlertDialog( context: Context, layout: Int, setCancellationOnTouchOutside: Boolean): AlertDialog {
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

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun giveProgressLogin(progress: Int?) {
        TODO("Not yet implemented")
    }

    override fun sendResult(result: Boolean) {
        progressDialog?.dismiss()

        if (result){
            Toast.makeText(this, "PayPal Linked Successfully", Toast.LENGTH_SHORT).show()

            intent = Intent(this, CreateSplitActivity::class.java)
            startActivityForResult(intent,CreateSplitActivity.ID)

        }
        else{
            Toast.makeText(this, "Incorrect Credentials! Please Retry", Toast.LENGTH_SHORT).show()
            payPalError(this,getString(R.string.incorrectCredential))
        }
    }

    override fun sendData(result: Boolean) {
        bottomSheet.updateContext()
    }

    override fun sendData(list: ArrayList<String>) {
        if (!list.isEmpty()){
            txtNoHistory.visibility = View.GONE
            populateHist(list)
        }
        else{
            txtViewHystory.setVisibility(View.VISIBLE)
            txtNoHistory.visibility = View.VISIBLE
        }

    }

    override fun sendData(list: ArrayList<Split>, give: String, have: String) {
        if(!list.isEmpty()){
            populateList(list)
            setTotal(give,have)
        }
        else{
            adapter = RvAdapterMain(this, list)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            recyclerView.setVisibility(View.VISIBLE)
            txtNoSplit.visibility = View.VISIBLE
            val params: ViewGroup.LayoutParams = recyclerView.getLayoutParams()
            params.height = 1000
            recyclerView.setLayoutParams(params)
            progressBar.setVisibility(View.GONE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that it is the SecondActivity with an OK result
        println("${requestCode} + ${resultCode}")
        if (requestCode == DetailActivity.ID || requestCode == CreateSplitActivity.ID || requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                println("AGGIORNA DATI")
                hideView()
                LoadDataAsyncTask(this).execute(true) //thread caricamento dati
                LoadHistAsyncTask(this).execute(true) //thread caricamento history
            }
            else{
                println("NON AGGIORNARE")
            }
        }
    }
}



interface CellClickListener {
    fun onCellClickListener(data: ParseObject?)
}