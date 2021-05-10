package it.unito.billsplitter.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseException
import com.parse.ParseObject
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapterDetail
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.activity.MenuClick
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.fragment_my_split.*


class DetailMySplitFragment : Fragment(), CellClickListener, MenuClick {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getSerializable("split") as MySplit?)!!

        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)

        s_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = RvAdapterDetail(this, mySplit.memberList)
        s_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        displaySplit(mySplit)

        s_btnSend.setOnClickListener{
            sendNotification(mySplit.memberList)
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


    private fun sendNotification(member: ArrayList<SplitMember>){

        var list: ArrayList<String> = ArrayList<String>()

        member.forEach{
            if(!it.user.objectId.equals(User.getCurrentUser()?.objectId) && !it.paid) list.add(it.user.objectId)
        }

        val map = HashMap<String, ArrayList<String>>()
        map["userList"] = list
        // here you can send parameters to your cloud code functions
        // such parameters can be the channel name, array of users to send a push to and more...

        ParseCloud.callFunctionInBackground("test", map, FunctionCallback<Any?> { `object`, e ->
            // handle callback
            if (e == null)
                println(`object`)
            else
                println(e.message)
        })
    }


    override fun onCellClickListener(data: ParseObject?) {

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

}

interface AsyncTaskFragmentListener {
    fun giveProgress(progress: Int?)
    fun sendData(mySplit: MySplit)
}