package it.unito.billsplitter.model

import android.content.res.TypedArray
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.parse.ParseUser
import java.io.Serializable

class SplitMember(var name: String, var share: String, var paid: Boolean, val user: ParseUser, var owner: Boolean = false):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(ParseUser::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(share)
        parcel.writeByte(if (paid) 1 else 0)
        parcel.writeParcelable(user, flags)
        parcel.writeByte(if (owner) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SplitMember> {
        override fun createFromParcel(parcel: Parcel): SplitMember {
            return SplitMember(parcel)
        }

        override fun newArray(size: Int): Array<SplitMember?> {
            return arrayOfNulls(size)
        }
    }
}