package it.unito.billsplitter.model

import android.os.Parcel
import android.os.Parcelable
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.Serializable

class MySplit(var name: String, var total: String, val share: String, var date: String, var owner: ParseUser, var memberList: ArrayList<SplitMember>, var percentage: Int = 0): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(ParseUser::class.java.classLoader)!!,
        arrayListOf<SplitMember>().apply {
            parcel.readList(this, SplitMember::class.java.classLoader)
        },
        parcel.readInt()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(total)
        parcel.writeString(share)
        parcel.writeString(date)
        parcel.writeParcelable(owner, flags)
        parcel.writeInt(percentage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MySplit> {
        override fun createFromParcel(parcel: Parcel): MySplit {
            return MySplit(parcel)
        }

        override fun newArray(size: Int): Array<MySplit?> {
            return arrayOfNulls(size)
        }
    }
}