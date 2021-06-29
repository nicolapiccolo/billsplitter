package it.unito.billsplitter.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseObject
import it.unito.billsplitter.R
import it.unito.billsplitter.controller.adapter.RvAdapterDetail
import it.unito.billsplitter.controller.task.UpdatePayAsyncTask
import it.unito.billsplitter.controller.task_inteface.UpdateDataListener
import it.unito.billsplitter.view.activity.CellClickListenerDetail
import it.unito.billsplitter.view.activity.MenuClick
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import kotlinx.android.synthetic.main.fragment_my_split.*
import kotlinx.android.synthetic.main.fragment_my_split.icon_text
import kotlinx.android.synthetic.main.fragment_my_split.s_btnSend
import kotlinx.android.synthetic.main.fragment_my_split.s_recyclerView
import kotlinx.android.synthetic.main.fragment_my_split.s_txtDate
import kotlinx.android.synthetic.main.fragment_my_split.s_txtName
import kotlinx.android.synthetic.main.fragment_my_split.s_txtTitle
import kotlinx.android.synthetic.main.fragment_my_split.s_txtTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class DetailMySplitFragment : Fragment(), CellClickListenerDetail, MenuClick, UpdateDataListener {

    private lateinit var id_split: String
    private lateinit var split: ParseObject


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getParcelable("split") as MySplit?)!!
        id_split = (arguments?.getString("id_split"))!!

        getSplit()

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

    private fun getSplit(){
        runBlocking {
            val job = launch(Dispatchers.Default) {
                split = Model.instance.getSplit(id_split)!!
            }
        }
    }


    private fun displaySplit(split: MySplit){
        s_txtName.text = "You"

        icon_text.text = "Y"

        val name = split.name.replace(" ", "\n")
        s_txtTitle.text = name
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtGet.text = split.share
        progressTotal.setProgress(split.percentage)
        //s_txtShare.text = split.share
    }


    private fun sendNotification(member: ArrayList<SplitMember>, id_split: String){
        if(shouldSend()){
            Model.instance.sendPaymentNotification(member,id_split)
            Model.instance.sendEmailNotification(member,id_split)
            showSnackBar(getString(R.string.notificationSend))
        }
        else
            showSnackBar(getString(R.string.notificationAlreadySend))
    }

    override fun closeSplit(s: ParseObject?) {
        println("CLOSE SPLIT")
    }

    override fun modifySplit(s: ParseObject?) {
        println("MODIFY SPLIT")
    }

    companion object {
        fun newIstance():Fragment = DetailMySplitFragment()
        const val NUMBER_MILLISECONDS_BETWEEN_UPDATES = (1000L * 60L * 10L)
    }

    override fun onCellClickListener(data: SplitMember?) {
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

    private fun showSnackBar(message : String){
        Snackbar.make(s_btnSend, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun saveLastUpdateTime() {
        s_btnSend.isEnabled = false
        runBlocking {
            val job = launch {
                Model.instance.setDate(split)
                getSplit()
            }
            job.join() // wait until child coroutine completes
            println("-- Done --")
            s_btnSend.isEnabled = true

        }
    }

    private fun getLastUpdate(): Long? {
       if (split != null){
           val date = split.getDate("notify")
           if (date!=null) return date.time
           else return -1
       }
       else return -1
    }


    private fun shouldSend(): Boolean{
        val lastUpdate: Long? = getLastUpdate()
        println(lastUpdate)
        val now : Long = Date().time

        if(lastUpdate == -1L){
            saveLastUpdateTime()
            return true
        }
        else if((now - lastUpdate!!) > NUMBER_MILLISECONDS_BETWEEN_UPDATES){
            saveLastUpdateTime()
            return true
        }
        else
            return false
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