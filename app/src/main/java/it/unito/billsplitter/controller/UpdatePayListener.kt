package it.unito.billsplitter.controller

interface UpdatePayListener {
    fun giveProgress(progress: Int?)
    fun sendResult(result: Boolean)
}