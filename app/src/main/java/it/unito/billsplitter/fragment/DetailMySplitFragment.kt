package it.unito.billsplitter.fragment

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parse.ParseObject
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapter
import it.unito.billsplitter.RvAdapterDetail
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.MySplit
import kotlinx.android.synthetic.main.fragment_my_split.*
import it.unito.billsplitter.model.Split
import kotlinx.android.synthetic.main.activity_main.*


class DetailMySplitFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mySplit: MySplit = (arguments?.getSerializable("split") as MySplit?)!!

        println("MS: " + mySplit.name)
        println("MS: " + mySplit.memberList)



        //val split: ParseObject = arguments?.getParcelable("split")!! //ParseObject cliccato
       //displaySplit(split)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
       return inflater.inflate(R.layout.fragment_my_split, container, false)
    }


    /*
    private fun displaySplit(split: ParseObject){
        s_txtName.text = Model.instance.getOwnerSplit(split)
        s_txtTitle.text = Model.instance.getNameSplit(split)
        s_txtTotal.text = Model.instance.getTotalSplit(split)
        s_txtDate.text = Model.instance.getDateSplit(split)
    }
     */

    companion object {
        fun newIstance():Fragment = DetailMySplitFragment()
    }


}

interface AsyncTaskFragmentListener {
    fun giveProgress(progress: Int?)
    fun sendData(mySplit: MySplit)
}