package it.unito.billsplitter.controller

import it.unito.billsplitter.model.Split

interface AsyncTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(list: ArrayList<Split>, give: String, have: String)
}