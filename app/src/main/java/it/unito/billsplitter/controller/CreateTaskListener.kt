package it.unito.billsplitter.controller

interface CreateTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}