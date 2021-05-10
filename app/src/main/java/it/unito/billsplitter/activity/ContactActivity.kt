package it.unito.billsplitter.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.BlockedNumberContract
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import it.unito.billsplitter.model.Model
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.Parse
import com.parse.ParseUser
import it.unito.billsplitter.LoadContactAsyncTask
import it.unito.billsplitter.LoadContactTaskListener
import it.unito.billsplitter.R
import it.unito.billsplitter.RvAdapter
import it.unito.billsplitter.model.Contact
import it.unito.billsplitter.model.Split
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_contacts_split.*
import kotlinx.android.synthetic.main.activity_contacts_split.progressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_card.*
import kotlinx.android.synthetic.main.contact_card.view.*
import kotlinx.android.synthetic.main.contact_card.view.imgAccount
import kotlinx.android.synthetic.main.splitting_card.view.*
import kotlin.collections.ArrayList

class ContactActivity : AppCompatActivity(), LoadContactTaskListener {

    private lateinit var selectedContacts : ArrayList<ParseUser>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_split)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)

        val title = getIntent().getStringExtra("title")
        val total = getIntent().getStringExtra("total")

        //We need to verify permission
        selectedContacts = ArrayList()
        val contactList : ArrayList<Contact> = ArrayList()
        val contacts = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR) {
            contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        } else {
            TODO("VERSION.SDK_INT < ECLAIR")
        }
        while (contacts!!.moveToNext()){
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = Contact(name,number)
            contactList.add(obj)
        }

        hideView()
        LoadContactAsyncTask(this).execute(contactList)

        btnAdd.setOnClickListener {
           if (selectedContacts.isEmpty())
               Toast.makeText(this, "Friends list can't be empty!", Toast.LENGTH_SHORT).show()
           else{
               selectedContacts.add(User.getCurrentUser()!!)
               intent = Intent(this, SplittingActivity::class.java)
               intent.putParcelableArrayListExtra("selectedContacts",selectedContacts)
               intent.putExtra("title",title)
               intent.putExtra("total",total)
               startActivity(intent)
           }
        }

        btnBack.setOnClickListener{
            finish()
        }
    }

    class ContactAdapter(items : List<ParseUser>,ctx: Context, selectedContacts: ArrayList<ParseUser>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

        private var list = items
        private var context = ctx
        var selectedContacts = selectedContacts

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name.text = list[position].get("username").toString()
            holder.number.text = list[position].get("phone").toString()
            /*if(list[position].image != null)
                holder.profile.setImageBitmap(list[position].image)
            else
                holder.profile.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))*/

            holder.checkBox_select.setOnCheckedChangeListener {buttonView, isChecked ->
                val contact = list[position]
                //contact.image=list[position].image
                if (isChecked) {
                    if(!selectedContacts.contains(contact))
                        selectedContacts.add(contact)
                } else{
                    remove(contact, selectedContacts)
                }
            }
        }

        fun isEqual(x: ParseUser, y: ParseUser): Boolean{
            if(x.objectId!!.equals(y.objectId)) return true
            else return false
        }

        fun remove(x: ParseUser, list: ArrayList<ParseUser>){
            for(i in 0..list.size-1){
                if(isEqual(x,list[i]))
                    list.removeAt(i)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false))
        }


        class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
            val name = v.txtContactName!!
            val number = v.txtContactNumber!!
            val profile = v.imgAccount!!
            val checkBox_select:CheckBox = itemView.checkBox_select
        }
    }

    private fun hideView(){
        recyclerViewContacts.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun populateList(contact: ArrayList<ParseUser>){
        val adapter = ContactAdapter(contact,this, this.selectedContacts)
        recyclerViewContacts.adapter = adapter
        adapter.notifyDataSetChanged()

        progressBar.setVisibility(View.GONE)
        recyclerViewContacts.setVisibility(View.VISIBLE);
    }

    override fun giveProgress(progress: Int?) {
        if (progress != null) {
            progressBar.setProgress(progress)
        }
    }

    override fun sendData(list: ArrayList<ParseUser>) {
        populateList(list)
    }

}