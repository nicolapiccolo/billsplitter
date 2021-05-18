package it.unito.billsplitter.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapterDetail
import it.unito.billsplitter.activity.CellClickListenerDetail
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import kotlinx.android.synthetic.main.fragment_other_split.*
import kotlinx.android.synthetic.main.fragment_other_split.icon_text
import kotlinx.android.synthetic.main.fragment_other_split.s_recyclerView
import kotlinx.android.synthetic.main.fragment_other_split.s_txtDate
import kotlinx.android.synthetic.main.fragment_other_split.s_txtName
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTitle
import kotlinx.android.synthetic.main.fragment_other_split.s_txtTotal
import org.json.JSONException
import java.math.BigDecimal

class DetailOtherSplitFragment : Fragment(), CellClickListenerDetail {

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


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return inflater.inflate(R.layout.fragment_other_split, container, false)
    }


    private fun displaySplit(split: MySplit){

        val owner = split.owner.getString("username")?.capitalize()
        s_txtName.text = owner
        icon_text.text = owner!![0].toString()
        s_txtTitle.text = split.name
        s_txtTotal.text = split.total
        s_txtDate.text = split.date
        s_txtShare.text = split.share
    }

    override fun onCellClickListener(data: SplitMember?) {
        println("Not yet implemented")
    }
}