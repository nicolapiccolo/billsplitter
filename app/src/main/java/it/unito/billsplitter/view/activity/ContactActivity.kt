package it.unito.billsplitter.view.activity


import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.Drawable

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextServicesManager
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.get
import it.unito.billsplitter.model.Model

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseUser
import it.unito.billsplitter.controller.task.LoadContactAsyncTask
import it.unito.billsplitter.controller.task_inteface.LoadContactListener
import it.unito.billsplitter.R
import it.unito.billsplitter.model.Contact
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_contacts_split.*
import kotlinx.android.synthetic.main.contact_card.view.*


import kotlinx.android.synthetic.main.splitting_card.view.*
import kotlin.collections.ArrayList


class ContactActivity : AppCompatActivity(), LoadContactListener {

    private lateinit var selectedContacts: ArrayList<ParseUser>
    private var title: String = ""
    private var total: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getIntent().getStringExtra("title").toString()
        total = getIntent().getStringExtra("total").toString()

        setContentView(R.layout.activity_contacts_split)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        //We need to verify permission

        checkPermission()

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setView() {
        selectedContacts = ArrayList()
        selectedContacts.add(User.getCurrentUser()!!)
        val contactList: ArrayList<Contact> = ArrayList()
        val contacts =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ECLAIR) {
                contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
            } else {
                TODO("VERSION.SDK_INT < ECLAIR")
            }
        while (contacts!!.moveToNext()) {
            val name =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = Contact(name, number)
            contactList.add(obj)
        }

        hideView()
        LoadContactAsyncTask(this).execute(contactList)

        btnAdd.setOnClickListener {
            if (selectedContacts.size==1)
                Toast.makeText(this, getString(R.string.emptyFriend), Toast.LENGTH_SHORT).show()
            else {

                intent = Intent(this, SplittingActivity::class.java)
                intent.putParcelableArrayListExtra("selectedContacts", selectedContacts)
                intent.putExtra("title", title)
                intent.putExtra("total", total)
                intent.putExtra("modify",false)
                startActivity(intent)
            }

        }

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setSearchableInfo(manager.getSearchableInfo(componentName))
        val ctx = this
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.toLowerCase() as CharSequence
                val searchedContacs: ArrayList<Contact> = ArrayList()
                contactList.forEach {
                    val number = Contact.converFormatPhone(it.number)
                    if (it.name.toLowerCase().contains(newText) || number.startsWith(newText)) {
                        searchedContacs.add(it)
                    }
                }
                LoadContactAsyncTask(ctx).execute(searchedContacs)
                return false
            }

        })
        btnBack.setOnClickListener {
            finish()
        }
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            Toast.makeText(baseContext, "You must accept to continue", Toast.LENGTH_LONG).show()
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),100)
        } else {
            setView()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setView()
                } else {
                    Toast.makeText(baseContext, "You must accept to continue", Toast.LENGTH_LONG).show()
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),100)
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
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
            holder.name.text = list[position].get("username").toString().capitalize()
            holder.number.text = list[position].get("phone").toString()
            holder.icon_text.text = list[position].get("username").toString().capitalize()[0].toString()
            Contact.setColor(Contact.getRandomMaterialColor("400",context),context)
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
            val icon = v.icon!!
            val icon_text = v.icon_text!!
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
