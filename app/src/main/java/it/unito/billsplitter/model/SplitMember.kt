package it.unito.billsplitter.model

import android.content.res.TypedArray
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.parse.ParseUser
import java.io.Serializable


class SplitMember(var name: String, var share: String, var paid: Boolean, val user: ParseUser, val owner: Boolean = false):Serializable {
}