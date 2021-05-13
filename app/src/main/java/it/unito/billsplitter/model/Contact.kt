package it.unito.billsplitter.model

import android.graphics.Bitmap
import com.parse.ParseUser
import kotlinx.android.synthetic.main.contact_card.view.*
//contatti rubrica
class Contact(var name: String, var number: String) {

    companion object{
        fun converFormatPhone(phone: String): String{
            val (digits) = phone.partition { it.isDigit() }
            return digits
        }
    }
}
