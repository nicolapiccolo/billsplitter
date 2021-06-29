package it.unito.billsplitter.model

import android.os.Parcelable
import com.parse.ParseObject
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Split(var name: String, var total: String, var date: String, var owner: String, var obj: ParseObject?) :Serializable {

    companion object{
        fun formatDate(d: Date): String{
            val df: DateFormat = SimpleDateFormat("EEE, d MMM yyyy")
            return df.format(d)
        }

        fun formatTotal(t: Float,sign: Boolean = true): String{

            if(sign){
                if(t<0){
                    var value = t
                    value *= -1
                    return "-€${"%.2f".format(value)}"
                }
                else
                {
                    return "+€${"%.2f".format(t)}"
                }
            }
            else
                return "€${"%.2f".format(t)}"
        }

        fun getFloat(n: String): Float {

            val nn = n.split("€")[1].replace(",",".")

            return nn.toFloat()
        }

        fun getFormatFoat(n: String): Float{
            var number = ""
            if("€" in n)
                number = n.replace("€","")
            else
                number = "${"%.2f".format(n.toFloat())}".replace(",",".")
            return number.toFloat()
        }

    }
}