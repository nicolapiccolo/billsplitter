package it.unito.billsplitter.model

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.parse.ParseUser
import it.unito.billsplitter.R
import kotlinx.android.synthetic.main.contact_card.view.*
//contatti rubrica
class Contact(var name: String, var number: String) {

    companion object{
        fun converFormatPhone(phone: String): String{
            val (digits) = phone.partition { it.isDigit() }
            return digits
        }

        fun getRandomMaterialColor(typeColor: String = "300", context: Context): Int {
            var returnColor: Int = Color.GRAY

            val arrayId: Int = context.getResources().getIdentifier("mdcolor_$typeColor", "array", context.getPackageName())
            if (arrayId != 0) {
                val colors: TypedArray = context.getResources().obtainTypedArray(arrayId)
                val index = (Math.random() * colors.length()).toInt()
                returnColor = colors.getColor(index, Color.GRAY)
                colors.recycle()
            }
            return returnColor
        }

        fun setColor(color: Int, context: Context){
            val unwrappedDrawable: Drawable? = AppCompatResources.getDrawable(context, R.drawable.circle_icon)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, color)
        }
    }
}
