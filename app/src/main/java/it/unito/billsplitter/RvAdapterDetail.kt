package it.unito.billsplitter

import it.unito.billsplitter.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import it.unito.billsplitter.activity.CellClickListenerDetail
import it.unito.billsplitter.activity.CellClickListener
import it.unito.billsplitter.model.Contact
import it.unito.billsplitter.model.SplitMember


class RvAdapterDetail(private val cellClickListener: CellClickListenerDetail, private val dataList: ArrayList<SplitMember>) : RecyclerView.Adapter<RvAdapterDetail.ViewHolder>() {

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


        if(!data.owner){
            p0.itemView.setOnLongClickListener {
                cellClickListener.onCellClickListener(data)
                true
            }


        p0.name?.text = data.name.capitalize()
        p0.share?.text = data.share
        p0.img?.text = data.name[0].toString().capitalize()

        if (data.owner){
            p0.name?.setTextColor(ContextCompat.getColor(context, R.color.darkgrey))
            p0.share?.setTextColor(ContextCompat.getColor(context, R.color.darkgrey))
            p0.icon?.setImageResource(R.drawable.circle_icon_stock)
        }

        else if(data.paid){
            p0.share?.setTextColor(ContextCompat.getColor(context, R.color.green))
            p0.icon?.setImageResource(R.drawable.img_account)
            p0.img?.text = ""
        }

        Contact.setColor(Contact.getRandomMaterialColor("300",context),context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.m_txtName)
        val share = itemView.findViewById<TextView>(R.id.m_txtShare)
        val img = itemView.findViewById<TextView>(R.id.icon_text)
        val icon = itemView.findViewById<ImageView>(R.id.icon)
    }


}