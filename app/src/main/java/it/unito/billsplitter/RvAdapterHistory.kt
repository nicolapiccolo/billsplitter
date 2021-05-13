package it.unito.billsplitter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_history_split.view.*


class RvAdapterHistory(private val list: ArrayList<String> ) : RecyclerView.Adapter<RvAdapterHistory.ViewHolder>(){

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.fragment_history_split, parent, false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string: List<String> = list[position].split("_")
        holder.title.text = string[0]
        holder.date.text = string[1]
        holder.total.text = string[2]
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val title = v.txtSplitTitle
        val date = v.txtDateSplit
        val total = v.txtSplitTotal
    }
}