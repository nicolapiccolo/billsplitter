package it.unito.billsplitter.model

import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class Split(var name: String, var total: Float, var id_user: String) {}

class Model private constructor()   {

    fun getSplitElement(position: Int) : Split {
        var split = Split("",0f,"")
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())

        query.findInBackground { splitList, e ->
            if(e==null){
                println("LIST: ")
                println(splitList.get(position).objectId)
                println(splitList.get(position).get("name"))
                println(splitList.get(position).get("total"))

                split.name = splitList.get(position).get("name") as String
                split.total = splitList.get(position).get("total") as Float
            }else{
                println(e.message)
            }
        }
        return split
    }

    fun getSize(): Int {
        var size = 0
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())
        query.findInBackground { splitList, e ->
            if(e==null){
                size = splitList.size
                println("LIST Size: " + size)
            }else{
                println(e.message)
            }
        }
        return size
    }

    companion object {
        @JvmStatic val instance = Model()
    }



}