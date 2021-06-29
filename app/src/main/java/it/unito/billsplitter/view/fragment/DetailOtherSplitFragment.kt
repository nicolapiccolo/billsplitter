package it.unito.billsplitter.view.fragment

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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import com.parse.ParseUser
import it.unito.billsplitter.R
import it.unito.billsplitter.controller.adapter.RvAdapterDetail
import it.unito.billsplitter.controller.task.LoadPayPalAsyncTask
import it.unito.billsplitter.controller.task.PaySplitAsyncTask
import it.unito.billsplitter.controller.task_inteface.LoadPayPalListener
import it.unito.billsplitter.controller.task_inteface.PaySplitListener
import it.unito.billsplitter.model.*
import it.unito.billsplitter.view.activity.CellClickListenerDetail
import it.unito.billsplitter.view.activity.CreateSplitActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.change_password_dialog.view.*
import kotlinx.android.synthetic.main.fragment_other_split.*
import kotlinx.android.synthetic.main.fragment_other_split.icon_text
import kotlinx.android.synthetic.main.fragment_other_split.s_recyclerView
import kotlinx.android.synthetic.main.fragment_other_split.s_txtDate
import kotlinx.android.synthetic.main.fragment_other_split.s_txtName
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTitle
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTotal
import kotlinx.android.synthetic.main.login_paypal_dialog.*
import kotlinx.android.synthetic.main.login_paypal_dialog.view.*
import kotlinx.android.synthetic.main.money_sent_dialog.*
import kotlinx.android.synthetic.main.pay_error_dialog.*
import kotlinx.android.synthetic.main.progress_dialog.*
import kotlinx.android.synthetic.main.send_money_dialog.*

class DetailOtherSplitFragment : Fragment(), CellClickListenerDetail, LoadPayPalListener, PaySplitListener {

    private lateinit var id_split: String
    private lateinit var mySplit: MySplit
    private var progressDialog: AlertDialog? = null

    companion object {
        fun newIstance(): Fragment{
            return DetailOtherSplitFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mySplit  = (arguments?.getParcelable("split") as MySplit?)!!
        id_split = (arguments?.getString("id_split"))!!


        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)

        s_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = RvAdapterDetail(this, mySplit.memberList)
        s_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        displaySplit(mySplit)

        s_btnSend.setOnClickListener{
            pay(Split.getFloat(mySplit.share),mySplit.owner, id_split)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_other_split, container, false)
    }


    private fun displaySplit(split: MySplit){

        val name = split.name.replace(" ", "\n")
        s_txtTitle.text = name


        println("Nff: " + name)
        val owner = split.owner.getString("username")?.capitalize()
        s_txtName.text = owner
        icon_text.text = owner!![0].toString()
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtShare.text = split.share
    }

    private fun pay(share : Float, owner : ParseUser, id_split : String){

        if (User.isLinked()){
            //println("-- BALANCE -- : " + User.id_paypal?.getNumber("balance"))
            val payment  = Payment(share, User.id_paypal!! ,owner, id_split)

            payPalSend(requireContext(),payment)
            //PaySplitAsyncTask(requireContext()).execute(payment)
        }
        else{
            payPalLogin(requireContext())
        }
    }

    private fun payPalLogin(ctx: Context){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.login_paypal_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mBuilder.setCancelable(false)

        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_btnLogin.setOnClickListener {
            if(!mDialogView.d_txtEmail.text.equals("") && !mDialogView.d_txtPassword.text.equals("")){

                val email = mDialogView.d_txtEmail.text.toString()
                val password = mDialogView.d_txtPassword.text.toString()

                val account = PayPalAccount(email,password)
                //showProgressBar(true)

                //mAlertDialog.login_progressBar.visibility = View.VISIBLE
                //mAlertDialog.d_centerLayout.visibility = View.GONE

                var controller = LoadPayPalAsyncTask()
                controller.setAsyncListener(this)
                controller.execute(account)

                mAlertDialog.dismiss()

                progressDialog = showProgressDialog(requireContext(),"Login...")

                //mAlertDialog.login_progressBar.visibility = View.GONE
                //mAlertDialog.d_centerLayout.visibility = View.VISIBLE


                //if(User.isPayPalLinked){
                 //   mAlertDialog.dismiss()
                //}
            }
            else
                Toast.makeText(ctx, "Fields can't be empty!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun payPalSend(ctx: Context, payment: Payment){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.send_money_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
                .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_txtBalance.text = Split.formatTotal(User.id_paypal!!.getNumber("balance")!!.toFloat(),false)

        mAlertDialog.txtShare.text = Split.formatTotal(payment.share,false)
        mAlertDialog.txtSendTo.text = payment.owner.username.capitalize()


        mAlertDialog.d_btnSend.setOnClickListener {

            progressDialog = showProgressDialog(requireContext(),"Payment Processing")
            mAlertDialog.dismiss()

            var controller = PaySplitAsyncTask()
            controller.setAsyncListener(this)
            controller.execute(payment)
        }
    }

    private fun payPalSuccess(ctx: Context){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.money_sent_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_btnBackHome.setOnClickListener {
            mAlertDialog.dismiss()

            val intent = Intent()
            activity?.setResult(AppCompatActivity.RESULT_OK, intent);
            activity?.finish()
        }
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


    fun getAlertDialog( context: Context, layout: Int, setCancellationOnTouchOutside: Boolean): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View =
            layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }

    fun showProgressDialog(context: Context, message: String): AlertDialog {
        val dialog = getAlertDialog(context, R.layout.progress_dialog, setCancellationOnTouchOutside = false)
        dialog.show()
        dialog.text_progress_bar.text = message
        return dialog
    }

    override fun onCellClickListener(data: SplitMember?) {
        println("Not yet implemented")
    }

    /*
    override fun giveProgress(progress: Int?) {
        println("-- inprogress")
        if (progress != null) {

            progressDialog = showProgressDialog(requireContext(),"Login...")
            //progressBar.setProgress(progress)
        }
    }*/

    override fun giveProgressPayment(progress: Int?) {
        println("--PAYMENT inprogress ")
    }

    override fun sendResultPayment(result: Boolean) {
        progressDialog?.dismiss()

        if (result){
            payPalSuccess(requireContext())
        }
        else{
            payPalError(requireContext(),getString(R.string.insufficientBalance))
        }
    }

    override fun giveProgressLogin(progress: Int?) {
        println("--LOGIN inprogress ")
    }

    override fun sendResult(result: Boolean) {

        progressDialog?.dismiss()

        if (result){
            Toast.makeText(context, R.string.payPalSuccess, Toast.LENGTH_SHORT).show()
            pay(Split.getFloat(mySplit.share),mySplit.owner, id_split)
        }
        else{
            Toast.makeText(context, R.string.payPaError, Toast.LENGTH_SHORT).show()
            payPalError(requireContext(),getString(R.string.incorrectCredential))
        }
    }
}

class Payment(var share: Float, var account: ParseObject, val owner: ParseUser, val id_split: String)