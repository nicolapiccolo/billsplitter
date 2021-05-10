package it.unito.billsplitter.model

import android.widget.Toast
import com.parse.*
import java.util.*
import kotlin.collections.ArrayList


class Model private constructor()   {

    var dataList : ArrayList<Split> = ArrayList()

    private var total_give : Float = 0f //pending split, totale da dare
    private var total_have : Float = 0f//pending split, totale da avere


    fun getGive():String {return Split.formatTotal(total_give)}
    fun getHave():String {return Split.formatTotal(total_have)}



    fun getOtherSplit(mySplit: List<String>) {

        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_user", User.getCurrentUser())
        query.addDescendingOrder("createdAt")

        val queryList: List<ParseObject> = query.find()

        queryList.forEach {


            val splitObj = (it.get("id_split") as ParseRelation<*>).query.find().get(0)

            //println("mysplit1: " + mySplit.get(0))
            //println("splitobj: " + splitObj.objectId)
            //println("check: " + splitObj.objectId in mySplit)


            var split = Split("", "", "", "", null)

            if (!splitObj.getBoolean("close")) {
                if (!mySplit.isEmpty()) {
                    if (!(splitObj.objectId in mySplit)) {

                        val share = (-(it.get("share") as Double).toFloat())

                        split.obj = splitObj
                        split.name = getNameSplit(splitObj)
                        split.total = Split.formatTotal(share)
                        split.date = Split.formatDate(it.createdAt as Date)
                        split.owner = getOwnerSplit(splitObj).username.toString()
                        println("OW: " + split.owner)

                        total_give += share
                        dataList.add(split)

                    }
                } else {

                    val share = (-(it.get("share") as Double).toFloat())

                    split.obj = splitObj
                    split.name = getNameSplit(splitObj)
                    split.total = Split.formatTotal(share)
                    split.date = Split.formatDate(it.createdAt as Date)
                    split.owner = getOwnerSplit(splitObj).username.toString()

                    total_give += share
                    dataList.add(split)
                }
            }
        }

    }

    fun getAllSplit(): ArrayList<Split> {

        total_have = 0f
        total_give = 0f
        dataList = ArrayList()

        val mySplit = getMySplit()
        getOtherSplit(mySplit)

        return dataList
    }

    fun getMySplit(): List<String>{

        var listId = ArrayList<String>()
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())
        query.whereEqualTo("close",false)
        query.addDescendingOrder("createdAt")



        val queryList: List<ParseObject> = query.find()


        queryList.forEach{
            var split = Split("", "", "", "", null)


            val share = getTotalofMySplit(it)

            split.obj = it
            split.name = it.get("name") as String
            split.total =  Split.formatTotal(share)
            split.date = Split.formatDate(it.createdAt as Date)
            split.owner = "Me"



            listId.add(it.objectId)

            total_have += share
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
            total = total + (it.getDouble("share")).toFloat()
        }

        return total
    }

    fun getTotalPaidMySplit(id_split: ParseObject): Float{
        var total: Float = 0f
        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereNotEqualTo("id_user", User.getCurrentUser())
        query.whereEqualTo("paid", true)
        val queryList: List<ParseObject> = query.find()

        queryList.forEach{
            total = total + (it.getDouble("share")).toFloat()
        }

        return total
    }

    fun getTotalHaveMySplit(id_split: ParseObject): Float{
        var total: Float = 0f
        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereNotEqualTo("id_user", User.getCurrentUser())
        val queryList: List<ParseObject> = query.find()

        queryList.forEach{
            total = total + (it.getDouble("share")).toFloat()
        }

        return total
    }


    fun getTotalSplit(id_split: ParseObject): String{
        return Split.formatTotal((id_split.getNumber("total") as Number).toFloat(),false)
    }

    fun getNameSplit(id_split: ParseObject): String{
        return id_split.get("name").toString().capitalize()
    }

    fun getDateSplit(id_split: ParseObject): String{
        return Split.formatDate(id_split.createdAt)
    }

    fun getOwnerSplit(id_split: ParseObject): ParseUser {
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        var user = (query.find().get(0).get("id_user") as ParseRelation<*>).query.find().get(0)
        return user as ParseUser
    }

    fun getOwnerObjectSplit(id_split: ParseObject): String{
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        return (query.find().get(0).get("id_user") as ParseRelation<*>).query.find().get(0).objectId

    }
    fun getMyShare(id_split: ParseObject): String{
        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereEqualTo("id_user", User.getCurrentUser())

        return Split.formatTotal(query.find().get(0).getDouble("share").toFloat(),false)
    }

    fun getListMember(id_split: ParseObject): ArrayList<SplitMember>{

        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)

        val queryList: List<ParseObject> = query.find()

        var listMember = ArrayList<SplitMember> ()
        val ownerId: String = getOwnerObjectSplit(id_split)

        println("OWNER ID: " + ownerId)
        queryList.forEach{
            val paid: Boolean = it.getBoolean("paid")
            val share: Float = it.getDouble("share").toFloat()

            var username: String = ""
            var owner = false
            val user: ParseUser = (it.get("id_user") as ParseRelation<*>).query.find().get(0) as ParseUser

            if(user.objectId.equals(ownerId)) owner = true
            if(user.objectId.equals(User.getCurrentUser()?.objectId)) username = "You"
            else username = user.username

            val member : SplitMember = SplitMember(username,Split.formatTotal(share,false),paid,user,owner)

            listMember.add(member)
        }

        return listMember
    }

    fun isMySplit(id_split: ParseObject): Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        val id_user = (query.find().get(0).get("id_user") as ParseRelation<*>).query.find().get(0)
        println("Split user: " + id_user.objectId)
        println("App user: " + User.getCurrentUser()?.objectId)
        return (id_user.objectId == User.getCurrentUser()?.objectId)
    }

    fun getPercentagePaid(total: Float, paid: Float): Int{
        println("${paid}/${total}")
        return  ((paid * 100)/total).toInt()
    }


    fun converFormatPhone(phone: String): String{
        val (digits) = phone.partition { it.isDigit() }
        return digits
    }

    fun contactContained(phone: String, contactList: ArrayList<Contact>): Boolean {
        contactList.forEach {
            if(converFormatPhone(it.number).equals(phone)){
                return true
            }
        }
        return false
    }

    fun joinContacts(contactList: ArrayList<Contact>): ArrayList<ParseUser>{
        val query = ParseUser.getQuery()
        val queryList: List<ParseUser> = query.find()
        var contacs: ArrayList<ParseUser> = ArrayList()
        queryList.forEach{
            if (contactContained(it.get("phone").toString(),contactList)){
                contacs.add(it)
            }
        }
        return contacs
    }

    private fun createTransaction(m: SplitMember, id_split: ParseObject): Boolean{
        val member = ParseObject("Transaction")
        member.put("paid",m.paid)
        member.put("share",m.share.toFloat())
        val relation_user = member.getRelation<ParseUser>("id_user")
        relation_user.add(m.user)
        val relation = member.getRelation<ParseObject>("id_split")
        relation.add(id_split)

        member.save()
        return true
    }

    fun createSplit(split: MySplit, members: ArrayList<SplitMember>): Boolean{
        val mysplit = ParseObject("Split")
        mysplit.put("name", split.name)
        mysplit.put("total", split.total.toFloat())
        val relation = mysplit.getRelation<ParseUser>("id_user")
        relation.add(split.owner)
        mysplit.save()

        members.forEach {
            createTransaction(it, mysplit)
        }
        return true
    }

    fun closeSplit(id_split: ParseObject){
        val query =  ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        val obj = query.find().get(0)

        obj.put("close",true)
        obj.save()
    }


    fun changePassword(newpassword: String){
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            currentUser.put("username", newpassword)

            // Saves the object.
            currentUser.saveInBackground ()
        }

      fun deleteTransaction(id_split: ParseObject): Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split",id_split)

        val queryList: List<ParseObject> = query.find()

        queryList.forEach{
            it.delete()
        }

        return true
    }

    fun deleteSplit(id_split: ParseObject):Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        val obj = query.find().get(0)
        obj.delete()

        return true

    }
  
    companion object {
        @JvmStatic val instance = Model()
    }



}