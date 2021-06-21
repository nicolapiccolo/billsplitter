package it.unito.billsplitter.controller

interface AsyncHistoryTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: ArrayList<String>)
}