package it.unito.billsplitter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.model.Model
import it.unito.billsplitter.model.Split


class RvAdapter(private val cellClickListener: CellClickListener, private val dataList: ArrayList<Split>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.card, p0, false)
        return ViewHolder(v);
    }

    override fun getItemCount():Int{

        Log.d("SIZE","Size: " +dataList.size)
        return dataList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        Log.d("BIND","first")
        val data = dataList.get(p1)
        Log.d("BIND","second")



        p0.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(data)
        }

        p0.title?.text = data.name
        p0.date?.text = data.date
        p0.share?.text = ((data.total as Float).toString())



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.findViewById<TextView>(R.id.c_txtName)
        val date = itemView.findViewById<TextView>(R.id.c_txtDate)
        val share = itemView.findViewById<TextView>(R.id.c_txtTotal)
    }


}