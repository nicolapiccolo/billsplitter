package it.unito.billsplitter.controller.adapter

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.unito.billsplitter.R
import it.unito.billsplitter.view.activity.CellClickListener
import it.unito.billsplitter.model.Split


class RvAdapterMain(private val cellClickListener: CellClickListener, private val dataList: ArrayList<Split>) : RecyclerView.Adapter<RvAdapterMain.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.card, p0, false)
        context = p0.context
        return ViewHolder(v);
    }

    override fun getItemCount():Int{

        Log.d("SIZE", "Size: " + dataList.size)
        return dataList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val data = dataList.get(p1)

        p0.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data.obj)
        }

        p0.title?.text = data.name.capitalize()
        p0.date?.text = data.date.capitalize()
        p0.owner?.text = data.owner.capitalize()


        val s = data.total.split(",").toTypedArray().get(0)
        println("SP: " + s)

        val ss1 = SpannableString(data.total)
        ss1.setSpan(RelativeSizeSpan(0.5f), s.length, data.total.length,0) // set size

        p0.share?.text = ss1




        if(data.owner.equals("Me")){
            p0.senreq.text = "Request From "
            p0.owner?.text = "Everyone"



            val color_white = ContextCompat.getColor(context, R.color.white)
            val color_green = ContextCompat.getColor(context, R.color.green)


            p0.title.setTextColor(color_white)
            p0.date.setTextColor(color_white)
            p0.share.setTextColor(color_white)
            p0.with.setTextColor(color_white)

            p0.senreq.setTextColor(color_green)
            p0.owner.setTextColor(color_green)

            p0.layout.background = ContextCompat.getDrawable(context, R.drawable.ic_card_green)
            p0.inner_layout.background = ContextCompat.getDrawable(context,
                R.drawable.inner_layout_card_green
            )


        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.c_txtName)
        val date = itemView.findViewById<TextView>(R.id.c_txtDate)
        val share = itemView.findViewById<TextView>(R.id.c_txtTotal)
        val owner = itemView.findViewById<TextView>(R.id.c_txtOwner)
        val senreq = itemView.findViewById<TextView>(R.id.c_sendreq)
        val with = itemView.findViewById<TextView>(R.id.c_txtWith)

        val layout = itemView.findViewById<LinearLayout>(R.id.c_linear_layout)
        val inner_layout = itemView.findViewById<ConstraintLayout>(R.id.c_layout)
    }


}