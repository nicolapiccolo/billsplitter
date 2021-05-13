package it.unito.billsplitter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser
import it.unito.billsplitter.CreateDataAsyncTask
import it.unito.billsplitter.CreateTaskListener
import it.unito.billsplitter.R
import it.unito.billsplitter.model.MySplit
import it.unito.billsplitter.model.SplitMember
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_setting_percentage.*
import kotlinx.android.synthetic.main.splitting_card.view.*


class SplittingActivity: AppCompatActivity(),CreateTaskListener{

    companion object{
        const val ID = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_percentage)

        recyclerViewSplitting.layoutManager = LinearLayoutManager(this)
        val title = getIntent().getStringExtra("title").toString()
        val total = getIntent().getStringExtra("total")?.toFloat()
        val contacts = this.intent.getParcelableArrayListExtra<ParseUser>("selectedContacts")
        val members = setMembers(contacts,total!!.toFloat())
        val mysplit = MySplit(title,total.toString(),"","",User.getCurrentUser()!!, members)
        totalPrice.setText(mysplit.total)
        recyclerViewSplitting.adapter = SplittingAdapter(members,this, mysplit.total.toFloat())


        btnSend.setOnClickListener {
            recyclerViewSplitting.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            CreateDataAsyncTask(this).execute(mysplit,members)
        }
    }

    fun setMembers(contacts: ArrayList<ParseUser>?, total: Float):ArrayList<SplitMember>{
        var members: ArrayList<SplitMember> = ArrayList()
        val amount = total/ (contacts!!.size)
        contacts?.forEach {
            var m = SplitMember(it.get("username").toString(),amount.toString(),false,it,false)
            if (User.getCurrentUser()!!.objectId!!.equals(it.objectId!!))
                m.owner=true
            members.add(m)
        }
        return members
    }

    fun backHome(view: View){
        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("RESULT",SplittingActivity.ID)
        setResult(RESULT_OK, intent);
        startActivity(intent)
    }


    class SplittingAdapter(items: ArrayList<SplitMember>, ctx: Context, total: Float ): RecyclerView.Adapter<SplittingAdapter.SplittingViewHolder>(){

        private var list = items
        private var context = ctx
        private var seekbarList:ArrayList<SeekBar> = ArrayList()
        private var txtPrices: ArrayList<TextView> = ArrayList()
        private var lockedPerc: Int = 0
        private val total:Float = total

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplittingViewHolder {
            return SplittingViewHolder(LayoutInflater.from(context).inflate(R.layout.splitting_card, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        private fun remaining(position: Int,seekbarList: ArrayList<SeekBar>): Int{
            var rem: Int=100; var c:Int=0
            for (i in 0..seekbarList.size-1){
                if(i==position || !seekbarList[i].isEnabled)
                    rem -=seekbarList[i].progress
                else
                    c++
            }
            if(c==0)
                c=1
            return rem/c
        }

        private fun getPercentage(share: Float):Int{
            val perc = (share*100)/total
            return perc.toInt()
        }

        private fun getShare(percentage: Int): Float{
            return (total*percentage)/100
        }

        private fun remainingSeek(seekbarList: ArrayList<SeekBar>, position: Int):Int{
            var c=0
            for (i in 0..seekbarList.size-1){
                if(i!=position && seekbarList[i].isEnabled)
                    c++
            }
            return c
        }

        private fun updateSeekBar(seekbarList: ArrayList<SeekBar>, position: Int){
            val remain = remaining(position, seekbarList)
            if (remain<=0) {
                seekbarList[position].setProgress(100-remainingSeek(seekbarList, position)-lockedPerc)
            }
            for (i in 0..seekbarList.size - 1) {
                if (i != position && seekbarList[i].isEnabled) {
                    seekbarList[i].setProgress(remain)
                }
            }
        }

        private fun updateShare(seekbarList: ArrayList<SeekBar>, txtPrices: ArrayList<TextView>){
            for (i in 0..seekbarList.size - 1) {
                list[i].share = ((total*seekbarList[i].progress)/100).toString()
                txtPrices[i].text = list[i].share
            }
        }

        private fun updateFromEditText(seekbarList: ArrayList<SeekBar>, txtPrices: ArrayList<TextView>, position: Int){
            for (i in 0..seekbarList.size - 1) {
                if(i!=position && seekbarList[i].isEnabled){
                    txtPrices[i].text = getShare(seekbarList[i].progress).toString()
                }
            }
        }

        override fun onBindViewHolder(holder: SplittingViewHolder, position: Int){
            if (list[position].owner)
                holder.name.text = "you pay for"
            else
                holder.name.text = list[position].name+" pay for"
            holder.price.setText(list[position].share)
            holder.seekBar.setProgress((100/list.size))
            holder.percentage.text = (100/list.size).toString()
            txtPrices.add(holder.price)
            seekbarList.add(holder.seekBar)
            holder.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    holder.percentage.text = progress.toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    updateSeekBar(seekbarList,position)
                    updateShare(seekbarList, txtPrices)
                }
            })
            holder.loker.setOnClickListener {
                //unlock
                if(holder.state) {
                    holder.state = false
                    holder.price.setEnabled(true)
                    holder.loker.setBackgroundResource(R.drawable.ic_password)
                    holder.seekBar.setEnabled(true)
                    lockedPerc-=holder.seekBar.progress
                }else{
                    //lock
                    holder.state = true
                    holder.price.setEnabled(false)
                    holder.loker.setBackgroundResource(R.drawable.ic_lock)
                    holder.seekBar.setEnabled(false)
                    lockedPerc+=holder.seekBar.progress
                }
            }

            holder.price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    val share = getPercentage((s.toString()).toFloat())
                    println(share)
                    holder.seekBar.setProgress(share)
                    updateSeekBar(seekbarList, position)
                }
            })

        }


        class SplittingViewHolder(v: View): RecyclerView.ViewHolder(v){
            val image = v.imgAccount!!
            val name = v.txtSubtitle!!
            val percentage = v.txtPercentage!!
            var price = v.edtPrice!!
            val seekBar = v.seekBar
            val loker = v.btnLocker
            var state: Boolean = false
        }
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendData(result: Boolean) {
        setContentView(R.layout.activity_money_request)
    }
}
