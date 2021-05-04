package it.unito.billsplitter.fragment

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseObject
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapter
import it.unito.billsplitter.RvAdapterDetail
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.activity.MainActivity
import it.unito.billsplitter.activity.MenuClick
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import kotlinx.android.synthetic.main.fragment_my_split.*
import it.unito.billsplitter.model.Split
import kotlinx.android.synthetic.main.fragment_my_split.s_recyclerView
import kotlinx.android.synthetic.main.fragment_my_split.s_txtDate
import kotlinx.android.synthetic.main.fragment_my_split.s_txtName
import kotlinx.android.synthetic.main.fragment_my_split.s_txtTitle
import kotlinx.android.synthetic.main.fragment_my_split.s_txtTotal


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



        //val split: ParseObject = arguments?.getParcelable("split")!! //ParseObject cliccato
       //displaySplit(split)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
       return inflater.inflate(R.layout.fragment_my_split, container, false)
    }



    private fun displaySplit(split: MySplit){
        s_txtName.text = split.owner
        s_txtTitle.text = split.name
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtGet.text = split.share
        progressTotal.setProgress(split.percentage)
        //s_txtShare.text = split.share
    }


    override fun onCellClickListener(data: ParseObject?) {

    }

    override fun closeSplit(s: ParseObject?) {
        if(s!=null){
            Model.instance.closeSplit(s)
        }
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