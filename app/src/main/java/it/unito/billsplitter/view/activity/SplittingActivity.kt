package it.unito.billsplitter.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseUser
import it.unito.billsplitter.controller.task.CreateDataAsyncTask
import it.unito.billsplitter.controller.task_inteface.CreateDataListener
import it.unito.billsplitter.R
import it.unito.billsplitter.controller.task.UpdateShareAsynkTask
import it.unito.billsplitter.model.*
import kotlinx.android.synthetic.main.activity_setting_percentage.*
import kotlinx.android.synthetic.main.splitting_card.view.*


class SplittingActivity: AppCompatActivity(), CreateDataListener {

    companion object{
        const val ID = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_percentage)
        recyclerViewSplitting.layoutManager = LinearLayoutManager(this)
        if(!getIntent().getBooleanExtra("modify", false)){
            val title = getIntent().getStringExtra("title").toString()
            val total = getIntent().getStringExtra("total")?.toFloat()
            val contacts = this.intent.getParcelableArrayListExtra<ParseUser>("selectedContacts")
            val members = setMembers(contacts,total!!.toFloat())
            val mysplit = MySplit(title,total.toString(),"","",User.getCurrentUser()!!, members)
            totalPrice.setText(mysplit.total)
            recyclerViewSplitting.adapter = SplittingAdapter(members,this, mysplit.total.toFloat())


            d_btnSend.setOnClickListener {
                recyclerViewSplitting.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                CreateDataAsyncTask(this).execute(mysplit,members)
            }
        }
        else{
            val mysplit = getIntent().getParcelableExtra<ParseObject>("split")
            val members = getIntent().getParcelableArrayListExtra<SplitMember>("members")
            val total = Split.getFormatFloat(mysplit?.get("total").toString()).toString()
            totalPrice.setText(total)
            recyclerViewSplitting.adapter = SplittingAdapter(members!!,this, total.toFloat())


            d_btnSend.setOnClickListener {
                recyclerViewSplitting.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                UpdateShareAsynkTask(this).execute(mysplit,members)
            }

        }

        btnBack.setOnClickListener {
            finish()
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
        intent.putExtra("RESULT",SplittingActivity.ID.toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    class SplittingAdapter(items: ArrayList<SplitMember>, ctx: Context, total: Float ): RecyclerView.Adapter<SplittingAdapter.SplittingViewHolder>(){

        private var list = items
        private var context = ctx
        //true for seekbar, false for edittext
        private var focus:Boolean = false
        private var seekbarList:ArrayList<SeekBar> = ArrayList()
        private var txtPrices: ArrayList<EditText> = ArrayList()
        private var lockedPerc: Int = 0
        private val total:Float = total

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplittingViewHolder {
            return SplittingViewHolder(LayoutInflater.from(context).inflate(R.layout.splitting_card, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        private fun remainingPercentage(position: Int,seekbarList: ArrayList<SeekBar>): Int{
            var rem: Int=100
            for (i in 0..seekbarList.size-1){
                if(i==position || !seekbarList[i].isEnabled)
                    rem -=seekbarList[i].progress
            }
            return rem
        }

        private fun remainingShare(position: Int, txtPrices: ArrayList<EditText>):Float{
            var rem: Float= total
            for (i in 0..txtPrices.size-1){
                if(i==position || !txtPrices[i].isEnabled)
                    rem -= Split.getFormatFloat(txtPrices[i].text.toString())

            }
            return rem
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
            var remain = remainingPercentage(position, seekbarList)
            val c = remainingSeek(seekbarList,position)
            if(c==0){
                seekbarList[position].setProgress(100-lockedPerc)
            }
            else{
                remain/=c
                if (remain<=0) {
                    seekbarList[position].setProgress(100-c-lockedPerc)
                }
                for (i in 0..seekbarList.size - 1) {
                    if (i != position && seekbarList[i].isEnabled) {
                        seekbarList[i].setProgress(remain)
                    }
                }
            }
        }

        private fun updateShare(seekbarList: ArrayList<SeekBar>, txtPrices: ArrayList<EditText>){
            for (i in 0..seekbarList.size - 1) {
                list[i].share = ((total*seekbarList[i].progress)/100).toString()
                txtPrices[i].setText((Split.getFormatFloat(list[i].share)).toString())
            }
        }

        private fun updateBars(txtPrices: ArrayList<EditText>, seekbarList: ArrayList<SeekBar>){
            for (i in 0..txtPrices.size - 1) {
                list[i].share = txtPrices[i].text.toString()
                seekbarList[i].setProgress(getPercentage(list[i].share.toFloat()))
                println(txtPrices[i].text)
            }
        }

        private fun updateFromEditText(seekbarList: ArrayList<SeekBar>, txtPrices: ArrayList<EditText>, position: Int){
            var remain = remainingShare(position, txtPrices)
            val c = remainingSeek(seekbarList,position)
            if(c==0){
                txtPrices[position].setText((getShare(100-lockedPerc)).toString())
            }
            else{
                remain = remain/c
                if (remain<=0){
                    txtPrices[position].setText((getShare(100-c-lockedPerc)).toString())
                    for (i in 0..seekbarList.size - 1) {
                        if(i!=position && seekbarList[i].isEnabled){
                            txtPrices[i].setText((getShare(seekbarList[i].progress)).toString())
                        }
                    }
                }
                else{
                    for (i in 0..seekbarList.size - 1) {
                        if(i!=position && seekbarList[i].isEnabled){
                            txtPrices[i].setText(remain.toString())
                        }
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: SplittingViewHolder, position: Int){
            if (list[position].owner){
                holder.name.text = "you pay for"
                holder.icon_text.text = "Y"
                holder.image.setImageResource(R.drawable.circle_icon_stock)
            }
            else {
                holder.name.text = list[position].name + " pay for"
                holder.icon_text.text = list[position].name[0].toString().capitalize()
            }
            Contact.setColor(Contact.getRandomMaterialColor("300",context),context)
            println("SHARE: " + list[position].share)
            holder.price.setText((Split.getFormatFloat(list[position].share)).toString())
            holder.seekBar.setProgress((100/list.size))
            holder.percentage.text = (100/list.size).toString()
            txtPrices.add(holder.price)
            seekbarList.add(holder.seekBar)
            holder.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    holder.percentage.text = progress.toString()
                    if(focus){
                        holder.price.setText((Split.getFormatFloat(((total*progress)/100).toString())).toString())
                        focus=!focus
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    focus = true
                }
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    updateSeekBar(seekbarList,position)
                    updateShare(seekbarList, txtPrices)
                }
            })
            holder.loker.setOnClickListener {
                //unlock
                if(holder.state) {
                    holder.state = false
                    holder.loker.setBackgroundResource(R.drawable.ic_password)
                    holder.seekBar.setEnabled(true)
                    holder.price.setEnabled(true)
                    lockedPerc-=holder.seekBar.progress
                }else{
                    //lock
                    holder.state = true
                    holder.loker.setBackgroundResource(R.drawable.ic_lock)
                    holder.seekBar.setEnabled(false)
                    holder.price.setEnabled(false)
                    lockedPerc+=holder.seekBar.progress
                }
            }


            holder.price.setOnFocusChangeListener(object : OnFocusChangeListener {
                override fun onFocusChange(view: View, hasFocus: Boolean) {
                    if (!hasFocus) {
                        val share = getPercentage(holder.price.text.toString().toFloat())
                            holder.seekBar.setProgress(share)
                            updateFromEditText(seekbarList,txtPrices,position)
                            updateBars(txtPrices, seekbarList)
                    }
                }
            })
        }


        class SplittingViewHolder(v: View): RecyclerView.ViewHolder(v){
            val image = v.iconContact!!
            val icon_text = v.icon_textC!!
            val name = v.txtSubtitle!!
            val percentage = v.txtPercentage!!
            var price = v.txtPrice!!
            val seekBar = v.seekBar
            val loker = v.btnLocker
            var state: Boolean = false
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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
