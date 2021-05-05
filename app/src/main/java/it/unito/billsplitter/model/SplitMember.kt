package it.unito.billsplitter.model

import com.parse.ParseObject
import com.parse.ParseUser
import java.io.Serializable

class SplitMember(var name: String, var share: String, var paid: Boolean, val user: ParseUser, var owner: Boolean = false): Serializable {
}