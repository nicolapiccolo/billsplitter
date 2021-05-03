package it.unito.billsplitter

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.SplitMember

class RvAdapterDetail(private val cellClickListener: CellClickListener, private val dataList: ArrayList<SplitMember>) : RecyclerView.Adapter<RvAdapterDetail.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.fragment_split_user, p0, false)
        context = p0.context
        return ViewHolder(v);
    }

    override fun getItemCount():Int{

        Log.d("SIZE", "Size: " + dataList.size)
        return dataList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val data = dataList.get(p1)

        if (data.owner){
            p0.name?.setTextColor(ContextCompat.getColor(context, R.color.darkgrey))
            p0.share?.setTextColor(ContextCompat.getColor(context, R.color.darkgrey))
        }

        else if(data.paid){
            p0.share?.setTextColor(ContextCompat.getColor(context, R.color.green))
        }


        p0.itemView.setOnClickListener {
            //cellClickListener.onCellClickListener(data)
        }

        p0.name?.text = data.name.capitalize()
        p0.share?.text = data.share

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.m_txtName)
        val share = itemView.findViewById<TextView>(R.id.m_txtShare)
        val img = itemView.findViewById<ImageView>(R.id.m_imgAccount)

    }


}