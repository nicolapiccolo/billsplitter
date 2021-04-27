package it.unito.billsplitter.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.unito.billsplitter.R
import it.unito.billsplitter.model.Contact
import it.unito.billsplitter.model.User
import kotlinx.android.synthetic.main.activity_contacts_split.*
import kotlinx.android.synthetic.main.contact_card.view.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_split)
        recyclerViewContacts.layoutManager = LinearLayoutManager(this)


        //We need to verify permission
        val contactList : MutableList<Contact> = ArrayList()
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        while (contacts!!.moveToNext()){
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = Contact()
            obj.name = name
            obj.number = number

            val photo_uri = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if(photo_uri != null){
                obj.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo_uri))
            }
            contactList.add(obj)
        }
        recyclerViewContacts.adapter = ContactAdapter(contactList,this)
        contacts.close()
    }

    class ContactAdapter(items : List<Contact>,ctx: Context) : RecyclerView.Adapter<ContactAdapter.ViewHolder>(){

        private var list = items
        private var context = ctx

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
            holder.name.text = list[position].name
            holder.number.text = list[position].number
            if(list[position].image != null)
                holder.profile.setImageBitmap(list[position].image)
            else
                holder.profile.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.ic_launcher_round))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false))
        }


        class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
            val name = v.txtContactName!!
            val number = v.txtContactNumber!!
            val profile = v.imgAccount!!
        }
    }
    
}