package it.unito.billsplitter.model

import com.parse.ParseObject
import com.parse.ParseUser
import java.io.Serializable

class MySplit(var name: String, var total: String, val share: String, var date: String, var owner: ParseUser, var memberList: ArrayList<SplitMember>, var percentage: Int = 0): Serializable{
}