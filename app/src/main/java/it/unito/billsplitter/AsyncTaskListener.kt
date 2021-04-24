package it.unito.billsplitter

import it.unito.billsplitter.model.Split

interface AsyncTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(list: ArrayList<Split>)
}