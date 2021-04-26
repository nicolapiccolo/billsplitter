package it.unito.billsplitter.model

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Split(var name: String, var total: Float, var date: String, var owner: String) {
    companion object{
        fun formatDate(d: Date): String{
            val df: DateFormat = SimpleDateFormat("EEE, d MMM yyyy")
            return df.format(d)
        }
    }
}

class Model private constructor()   {

    private var dataList : ArrayList<Split> = ArrayList()

    fun getOtherSplit(mySplit: List<String>) {

        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_user", User.getCurrentUser())

        val queryList: List<ParseObject> = query.find()

        queryList.forEach {


            val splitObj = (it.get("id_split") as ParseRelation<*>).query.find().get(0)

            //println("mysplit1: " + mySplit.get(0))
            //println("splitobj: " + splitObj.objectId)
            //println("check: " + splitObj.objectId in mySplit)


            println("DATE: " + Split.formatDate(it.createdAt as Date))
            var split = Split("", 0f, "", "")

            if(!mySplit.isEmpty()) {
                if (!(splitObj.objectId in mySplit)) {


                    //getTotalofSplit(it)

                    //println("other: " + splitObj.get("name"))
                    split.name = getNameSplit(splitObj)
                    split.total = (it.get("share") as Double).toFloat()
                    split.date = Split.formatDate(it.createdAt as Date)

                    dataList.add(split)

                }
            }
            else{

                split.name = getNameSplit(splitObj)
                split.total = (it.get("share") as Double).toFloat()
                split.date = Split.formatDate(it.createdAt as Date)


                dataList.add(split)
            }

        }

    }

    fun getAllSplit(): ArrayList<Split> {

        val mySplit = getMySplit()
        getOtherSplit(mySplit)

        return dataList
    }

    fun getMySplit(): List<String>{

        var listId = ArrayList<String>()
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())



        val queryList: List<ParseObject> = query.find()


        queryList.forEach{
            var split = Split("", 0f, "", "")


            //getTotalofSplit(it)
            split.name = it.get("name") as String
            split.total =  getTotalofMySplit(it)
            split.date = Split.formatDate(it.createdAt as Date)



            listId.add(it.objectId)
            dataList.add(split)
        }

        return listId
    }

    fun getTotalofMySplit(id_split: ParseObject): Float{
        var total: Float = 0f
        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereNotEqualTo("id_user", User.getCurrentUser())
        query.whereEqualTo("paid", false)
        val queryList: List<ParseObject> = query.find()

        queryList.forEach{
            total = total + (it.get("share")as Double).toFloat()
        }

        println("LIST: " + total)
        return total
    }


    fun getNameSplit(id_split: ParseObject): String{
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        return query.find().get(0).get("name").toString()
    }


    /*
    fun getSplitElement(position: Int) : Split {
        var split = Split("", 0f, "", "")
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())

        query.findInBackground { splitList, e ->
            if(e==null){
                println("Get split element: " + splitList.get(position).objectId)
                //println(splitList.get(position).get("name"))
                //println(splitList.get(position).get("total"))
                //println(splitList.get(position).get("created_at"))


                split.name = splitList.get(position).get("name") as String
                split.total = (splitList.get(position).get("total") as Double).toFloat()
                split.date = (splitList.get(position).get("createdAt") as? String).toString()
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

    */



    companion object {
        @JvmStatic val instance = Model()
    }



}