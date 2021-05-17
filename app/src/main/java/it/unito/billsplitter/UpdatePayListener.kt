package it.unito.billsplitter

interface UpdatePayListener {
    fun giveProgress(progress: Int?)
    fun sendResult(result: Boolean)
}