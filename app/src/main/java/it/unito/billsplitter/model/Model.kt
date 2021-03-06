package it.unito.billsplitter.model

import android.widget.Toast
import com.parse.*
import com.parse.ParseQuery
import java.util.*
import kotlin.collections.ArrayList


class Model private constructor()   {

    var dataList : ArrayList<Split> = ArrayList()

    private var total_give : Float = 0f //pending split, totale da dare
    private var total_have : Float = 0f//pending split, totale da avere


    fun getGive():String {return Split.formatTotal(total_give)}
    fun getHave():String {return Split.formatTotal(total_have)}



    fun getSplit(id_split: String): ParseObject? {
        println("ID: "+ id_split)
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split)

        return query.find().get(0)
    }

    fun findUser(email: String): ParseUser?{
        var result: ParseUser? = null
        val query = ParseUser.getQuery()
        query.whereEqualTo("email", email)
        /*query.findInBackground { users: List<ParseUser>, e: ParseException? ->
            if (e == null) {
                // The query was successful, returns the users that matches
                // the criteria.
                for (user1 in users) {
                    result = user1
                }
            } else {
                // Something went wrong.
                println("error")
            }
        }
        return result
        */
        for (user in query.find())
            result = user
        return result

    }

    fun getSplitByName(name: String):ParseObject?{
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("name", name)
        return query.find().get(0)
    }

    fun getOtherSplit(mySplit: List<String>) {

        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_user", User.getCurrentUser())
        query.whereEqualTo("paid",false)
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

                        val share = -it.getNumber("share")?.toFloat()!!

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

                    val share = -it.getNumber("share")?.toFloat()!!

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
            total += it.getNumber("share")?.toFloat()!!
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
            total += it.getNumber("share")?.toFloat()!!
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
            total += it.getNumber("share")?.toFloat()!!
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

        return Split.formatTotal(query.find().get(0).getNumber("share")?.toFloat()!!,false)
    }

    fun getMyHistory(): ArrayList<String>{
        var history = ArrayList<String>()
        val query = ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("id_user", User.getCurrentUser())
        query.whereEqualTo("close",true)
        query.addDescendingOrder("createdAt")
        val queryList: List<ParseObject> = query.find()
        queryList.forEach {
            val total = "+???"+(it.getDouble("total")).toFloat()
            val hist = getNameSplit(it)+"_"+Split.formatDate(it.createdAt as Date)+"_"+total
            history.add(hist)
        }
        val q = ParseQuery.getQuery<ParseObject>("Transaction")
        q.whereEqualTo("id_user", User.getCurrentUser())
        q.whereEqualTo("paid",true)
        q.addDescendingOrder("createdAt")
        val qList: List<ParseObject> = q.find()
        qList.forEach {
            val share = "-???"+(it.getDouble("share")).toFloat()
            val splitObj = (it.get("id_split") as ParseRelation<*>).query.find().get(0)
            val h = getNameSplit(splitObj)+"_"+Split.formatDate(it.createdAt as Date)+"_"+share
            history.add(h)
        }
        return history
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
            val share: Float = it.getNumber("share")?.toFloat()!!

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

    fun contactContained(phone: String, contactList: ArrayList<Contact>): Boolean {
        contactList.forEach {
            if(Contact.converFormatPhone(it.number).equals(phone)){
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
        val relation_user = member.getRelation<ParseUser>("id_user")

        if(m.user.objectId == ParseUser.getCurrentUser().objectId) m.paid = true

        member.put("paid",m.paid)
        member.put("share",m.share.toFloat())
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

        sendNewSplitNotification(members,mysplit.objectId)
        return true
    }

    fun updatePercentages(id_split: ParseObject, members: ArrayList<SplitMember>): Boolean{
        members.forEach {
            updateShare(id_split, it.user, it.share.toFloat())
        }
        return true
    }

    private fun updateShare(id_split: ParseObject, id_user: ParseUser, share: Float){
        val query = ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereEqualTo("id_user", id_user)
        val obj = query.find().get(0)
        obj.put("share",share)
        obj.save()
    }

    fun closeSplit(id_split: ParseObject){
        val query =  ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        val obj = query.find().get(0)

        obj.put("close",true)
        obj.save()
    }


    fun changePassword(newpassword: String) {
        val currentUser = ParseUser.getCurrentUser()
        if (currentUser != null) {
            // Other attributes than "email" will remain unchanged!
            currentUser.put("username", newpassword)

            // Saves the object.
            currentUser.saveInBackground()
        }
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

    fun setPaid(id_split: ParseObject,id_user: ParseUser, value: Boolean): Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)
        query.whereEqualTo("id_user" , id_user)

        val obj = query.find().get(0)
        if(obj!=null){
            obj.put("paid",value)
            obj.save()

            if (checkClose(id_split)) {
                id_split.put("close",true)
                id_split.save()
            }

            return true
        }
        else return false
    }

    fun checkClose(id_split: ParseObject): Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Transaction")
        query.whereEqualTo("id_split", id_split)

        val queryList: List<ParseObject> = query.find()

        if(queryList.size > 0){
            queryList.forEach{
                if (!it.getBoolean("paid")) return false
            }
        } else return false

        println("TO CLOSE")
        return true
    }

    fun setDate(id_split: ParseObject) :Boolean{
        val query =  ParseQuery.getQuery<ParseObject>("Split")
        query.whereEqualTo("objectId", id_split.objectId)

        val obj = query.find().get(0)
        if(obj!=null){
            obj.put("notify", Date())
            obj.save()
            return true
        }
        else return false
    }

    fun getPayPalAccount(email :String, password: String): ParseObject?{
        val query = ParseQuery.getQuery<ParseObject>("PayPalAccount")
        query.whereEqualTo("email",email)
        query.whereEqualTo("password",password)

        val result_size = query.find().size
        if(result_size > 0){
            val account = query.find().get(0)
            val relation = User.getCurrentUser()?.getRelation<ParseObject>("id_paypal")
            relation?.add(account)
            User.getCurrentUser()?.save()
            User.id_paypal = account

            return account
        }
        else
            return null
    }

    fun checkBalancePayPal(share: Float, id_paypal: ParseObject): Boolean{
        //controllo se saldo disponibile copre la spesa
        val balance = id_paypal.getNumber("balance")!!.toFloat()

        return (balance>=share)
    }


    fun payWithPayPalTo(share: Float, id_user: ParseUser,id_sender: ParseUser, id_split: ParseObject): Boolean{
        val sender_id =  User.id_paypal?.objectId

        if(sender_id!=null){
            val query = ParseQuery.getQuery<ParseObject>("PayPalAccount")
            query.whereEqualTo("objectId",sender_id)

            val sender_account = query.first

            println("PWP: user id: " + id_user.objectId)

            val receiver_account_id = id_user.getRelation<ParseObject>("id_paypal")

            val receiver_account = receiver_account_id.query.find().get(0)

            println("PWP: receiver account id: " + receiver_account.objectId)



            if(sender_account!=null && receiver_account!=null){
                //salvo i totali attuali
                val total_sender = sender_account.getNumber("balance")!!.toFloat()
                val total_receiver = receiver_account.getNumber("balance")!!.toFloat()

                println("SENDER TOTAL: " + total_sender)
                println("RECEIVER TOTAL: " + total_receiver)


                //modifico saldo sender
                sender_account.put("balance", total_sender - share)
                sender_account.save()

                
                //modifico saldo receiver
                receiver_account.put("balance",total_receiver + share)
                receiver_account.save()

                //println("\n AFTER PAY")
                //println("SENDER TOTAL: " + sender_account.getNumber("balance")!!.toFloat())
                //println("RECEIVER TOTAL: " + receiver_account.getNumber("balance")!!.toFloat())

                return  setPaid(id_split, id_sender,true)
            }
            else
            {
                println("Sender or receiver is null")
                return false
            }

        }
        else{
            println("SENDER ID IS NULL!")
            return false
        }

    }

    fun sendPaymentNotification(member: ArrayList<SplitMember>, id_split: String){
        var list: ArrayList<String> = ArrayList<String>()

        member.forEach{
            if(!it.user.objectId.equals(User.getCurrentUser()?.objectId) && !it.paid) list.add(it.user.objectId)
        }

        val map = HashMap<String, Any>()
        map["userList"] = list
        map["id_split"] = id_split
        // here you can send parameters to your cloud code functions
        // such parameters can be the channel name, array of users to send a push to and more...

        ParseCloud.callFunctionInBackground("test", map, FunctionCallback<Any?> { `object`, e ->
            // handle callback
            if (e == null)
                println(`object`)
            else
                println(e.message)
        })
    }


    fun sendNewSplitNotification(member: ArrayList<SplitMember>, id_split: String){
        var list: ArrayList<String> = ArrayList<String>()

        member.forEach{
            if(!it.user.objectId.equals(User.getCurrentUser()?.objectId) && !it.paid) list.add(it.user.objectId)
        }

        val map = HashMap<String, Any>()
        map["userList"] = list
        map["id_split"] = id_split
        // here you can send parameters to your cloud code functions
        // such parameters can be the channel name, array of users to send a push to and more...

        ParseCloud.callFunctionInBackground("test2", map, FunctionCallback<Any?> { `object`, e ->
            // handle callback
            if (e == null)
                println(`object`)
            else
                println(e.message)
        })
    }

    fun sendEmailNotification(member: ArrayList<SplitMember>, id_split: String){
        var list: ArrayList<String> = ArrayList<String>()

        val map = HashMap<String, Any>()

        val split = getSplit(id_split)

        map["owner"] = getOwnerSplit(split!!)?.username.capitalize()

        member.forEach{
            if(!it.user.objectId.equals(User.getCurrentUser()?.objectId) && !it.paid) list.add(it.user.objectId)

            map["userId"] = it.user.objectId.toString()
            map["splitId"] = id_split

            ParseCloud.callFunctionInBackground("sendgridEmail", map, FunctionCallback<Any?> { `object`, e ->
                // handle callback
                if (e == null)
                    println(`object`)
                else
                    println(e.message)
            })
        }
    }




    companion object {
        @JvmStatic val instance = Model()
    }



}