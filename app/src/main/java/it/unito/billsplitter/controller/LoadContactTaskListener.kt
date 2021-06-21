package it.unito.billsplitter.controller

import com.parse.ParseUser


interface LoadContactTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(list: ArrayList<ParseUser>)
}