package it.unito.billsplitter.controller.task_inteface

import com.parse.ParseUser


interface LoadContactListener {
    fun giveProgress(progress: Int?)
    fun sendData(list: ArrayList<ParseUser>)
}