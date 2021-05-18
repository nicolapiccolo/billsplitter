package it.unito.billsplitter.fragment

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.Auth
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseException
import com.parse.ParseObject
import it.unito.billsplitter.*
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.activity.CellClickListenerDetail
import it.unito.billsplitter.activity.DetailActivity
import it.unito.billsplitter.activity.MenuClick
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import it.unito.billsplitter.model.Model
import kotlinx.android.synthetic.main.fragment_my_split.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class DetailMySplitFragment : Fragment(), CellClickListenerDetail, MenuClick,UpdateTaskListener {

    private lateinit var id_split: String
    private lateinit var split: ParseObject


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getParcelable("split") as MySplit?)!!
        id_split = (arguments?.getString("id_split"))!!

        runBlocking {
            val job = launch(Dispatchers.Default) {
                split = Model.instance.getSplit(id_split)!!
            }
        }

        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)

        s_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = RvAdapterDetail(this, mySplit.memberList)
        s_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        displaySplit(mySplit)

        s_btnSend.setOnClickListener{
            sendNotification(mySplit.memberList,id_split)
        }


        //val split: ParseObject = arguments?.getParcelable("split")!! //ParseObject cliccato
       //displaySplit(split)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
       return inflater.inflate(R.layout.fragment_my_split, container, false)
    }



    private fun displaySplit(split: MySplit){

        s_txtName.text = "You"

        icon_text.text = "Y"

        s_txtTitle.text = split.name
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtGet.text = split.share
        progressTotal.setProgress(split.percentage)
        //s_txtShare.text = split.share
    }


    private fun sendNotification(member: ArrayList<SplitMember>, id_split: String){
        Model.instance.sendPaymentNotification(member,id_split)
    }

    override fun closeSplit(s: ParseObject?) {
        println("CLOSE SPLIT")
    }

    override fun modifySplit(s: ParseObject?) {
        println("MODIFY SPLIT")
    }

    companion object {
        fun newIstance():Fragment = DetailMySplitFragment()
    }

    override fun onCellClickListener(data: SplitMember?) {
        Toast.makeText(context, "Long Click to: ${data?.name}, is ${data?.paid}", Toast.LENGTH_LONG).show()

        if (data!=null) confirmDialogPaid(data)
    }

    private fun confirmDialogPaid(data: SplitMember) {
        val name = data.name.capitalize()
        val user = data.user
        val paid : Boolean = data.paid


        val builder = AlertDialog.Builder(context)
        if(!paid){
            builder.setTitle(R.string.titlePaid)
            builder.setMessage(R.string.contentPaid)
        }
        else{
            builder.setTitle(R.string.titleNotPaid)
            builder.setMessage(R.string.contentNotPaid)
        }

        builder.setPositiveButton(R.string.yes) { dialog, which ->
            //showProgressBar(true)
            //UpdateDataAsyncTask(this).execute(split)

            Toast.makeText(context, "Confirming payment to: ${name}", Toast.LENGTH_SHORT).show()
            UpdatePayAsyncTask(requireContext(),!paid).execute(split,user)
        }

        builder.setNegativeButton(R.string.no) { dialog, which ->
        }

        builder.show()
    }

    override fun giveProgress(progress: Int?) {
        println("give progress")
    }

    override fun sendData(result: Boolean) {
        if (result){
            println("update DONE")
        }
        else println("update NOT DONE")
    }
}