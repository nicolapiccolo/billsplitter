package it.unito.billsplitter.controller

interface UpdateTaskListener {
    fun giveProgress(progress: Int?)
    fun sendData(result: Boolean)
}