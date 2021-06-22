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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.billsplitter.R
import it.unito.billsplitter.controller.adapter.RvAdapterDetail
import it.unito.billsplitter.controller.task.LoadPayPalAsyncTask
import it.unito.billsplitter.controller.task_inteface.LoadPayPalListener
import it.unito.billsplitter.view.activity.CellClickListenerDetail
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.PayPalAccount
import it.unito.billsplitter.model.SplitMember
import it.unito.billsplitter.model.User
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
import kotlinx.android.synthetic.main.login_paypal_dialog.view.d_btnCancel

class DetailOtherSplitFragment : Fragment(), CellClickListenerDetail, LoadPayPalListener {

    companion object {
        fun newIstance(): Fragment{
            return DetailOtherSplitFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getParcelable("split") as MySplit?)!!

        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)

        s_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = RvAdapterDetail(this, mySplit.memberList)
        s_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        displaySplit(mySplit)

        s_btnSend.setOnClickListener{

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

    private fun pay(){
        if (User.isLinked()){

        }
    }

    private fun payPalDialog(ctx: Context){
        val mDialogView = LayoutInflater.from(ctx).inflate(R.layout.login_paypal_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(ctx)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mAlertDialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        mAlertDialog.d_btnLogin.setOnClickListener {
            if(!mDialogView.d_txtEmail.text.equals("") && !mDialogView.d_txtPassword.text.equals("")){

                val email = mDialogView.d_txtEmail.text.toString()
                val password = mDialogView.d_txtPassword.text.toString()

                val account = PayPalAccount(email,password)
                //showProgressBar(true)
                LoadPayPalAsyncTask(ctx).execute(account)

                if(User.isPayPalLinked){
                    mAlertDialog.dismiss()
                    //intent = Intent(this, CreateSplitActivity::class.java)
                    //startActivityForResult(intent, CreateSplitActivity.ID)
                }
            }
            else
                Toast.makeText(ctx, "Fields can't be empty!", Toast.LENGTH_SHORT).show()

        }
        mDialogView.d_btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    override fun onCellClickListener(data: SplitMember?) {
        println("Not yet implemented")
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendResult(result: Boolean) {
        if (result){
            Toast.makeText(context, "PayPal Linked Successfully", Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(context, "Incorrect Credentials! Please Retry", Toast.LENGTH_SHORT).show()
    }
}